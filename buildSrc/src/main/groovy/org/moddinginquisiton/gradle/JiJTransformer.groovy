package org.moddinginquisiton.gradle

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.artifacts.transform.InputArtifact
import org.gradle.api.artifacts.transform.TransformAction
import org.gradle.api.artifacts.transform.TransformOutputs
import org.gradle.api.artifacts.transform.TransformParameters
import org.gradle.api.file.FileSystemLocation
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.bundling.Jar

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.util.jar.JarFile
import java.util.jar.JarInputStream
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipException

@CompileStatic
abstract class JiJTransformer implements TransformAction<TransformParameters.None> {

    private static final String LICENSE_URL = 'https://raw.githubusercontent.com/TheSilkMiner/APLPKIGB/dbe1c09bce8f32b7fbe4610f1de2d79b10a39d88/LICENSE'

    @InputArtifact
    abstract Provider<FileSystemLocation> getInputArtifact()

    static final List<String> BLACK_LIST = List.of(
            'META-INF/MANIFEST.MF'
    )

    @SuppressWarnings('DuplicatedCode')
    @Override
    void transform(TransformOutputs transformOutputs) {
        final input = getInputArtifact().get().asFile
        if (!input.toString().endsWith('.jar') || !input.toString().contains('aplp')) {
            transformOutputs.file(input)
            return
        }

        final outPath = transformOutputs.file('jijready_' + input.toPath().getFileName().toString()).toPath()
        Files.createDirectories(outPath.getParent())
        final inputJar = new JarFile(input)
        try (final inputStream = new JarInputStream(new FileInputStream(input))
             final out = new JarOutputStream(Files.newOutputStream(outPath), inputStream.getManifest())) {
            JarInputStream groovyStream = null
            JarInputStream langProviderStream = null
            JarInputStream mod = null
            JarInputStream ast = null
            final jarsJson = new JsonArray()
            final enumeration = inputJar.entries()
            final aplpVersion = input.toPath().getFileName().toString().split('-')[2]
            while (enumeration.hasMoreElements()) {
                final next = enumeration.nextElement()
                if (next.name.endsWith('jar')) {
                    if (next.name.contains('groovy')) {
                        groovyStream = new JarInputStream(inputJar.getInputStream(next))
                    } else if (next.name.contains('provider')) {
                        langProviderStream = new JarInputStream(inputJar.getInputStream(next))
                    } else if (next.name.contains('rt')) {
                        mod = new JarInputStream(inputJar.getInputStream(next))
                    } else if (next.name.contains('ast')) {
                        ast = new JarInputStream(inputJar.getInputStream(next))
                    }
                }
            }

            final langJarByte = new ByteArrayOutputStream()
            final langJar = new JarOutputStream(langJarByte, langProviderStream.getManifest())

            move(groovyStream, langJar)
            move(langProviderStream, langJar)
            move(ast, langJar)
            langJar.close()

            move(mod, out)

            final langEntry = new ZipEntry('META-INF/jarjar/aplp-lang.jar')
            out.putNextEntry(langEntry)
            out.write(langJarByte.toByteArray())
            out.closeEntry()

            jarsJson.add makeJarJson(
                'langprovider',
                aplpVersion,
                langEntry.name
            )

            try (final var is = URI.create(LICENSE_URL).toURL().openStream()) {
                out.putNextEntry(new ZipEntry('LICENSE'))
                out.write(is.readAllBytes())
                out.closeEntry()
            }

            if (!jarsJson.isEmpty()) {
                final entry = new ZipEntry('META-INF/jarjar/metadata.json')
                out.putNextEntry(entry)
                final var fullJson = new JsonObject()
                fullJson.add 'jars', jarsJson
                out.write(new Gson().toJson(fullJson).getBytes(StandardCharsets.UTF_8))
                out.closeEntry()

                println 'Successfully transformed APLP!'
            }
        }
    }

    private static void move(JarInputStream from, JarOutputStream to) {
        ZipEntry subEntry
        while ((subEntry = from.getNextEntry()) != null) {
            if (!BLACK_LIST.contains(subEntry.name)) {
                try {
                    to.putNextEntry(subEntry)
                    to.write(from.readAllBytes())
                    to.closeEntry()
                } catch (ZipException e) {
                    if (!e.getMessage().contains('duplicate entry'))
                        throw new RuntimeException('Encountered exception while transforming APLP', e)
                }
            }
        }
    }

    private static JsonObject makeJarJson(String artifact, String version, String path) {
        final subJson = new JsonObject()

        final identifier = new JsonObject()
        identifier.addProperty('group', 'net.thesilkminer.mc.austin')
        identifier.addProperty('artifact', artifact)
        subJson.add('identifier', identifier)

        final versionJson = new JsonObject()
        versionJson.addProperty('range', "[$version,)")
        versionJson.addProperty('artifactVersion', version)
        subJson.add('version', versionJson)

        subJson.addProperty('path', path)
        subJson.addProperty('isObfuscated', false)

        return subJson
    }

    static void doJijLast(Jar task, File aplpTransformed, Project project) {
        final tempLocation = project.file("${project.buildDir}/libs/temp-jij.jar").toPath()
        final Path jarJarFile = task.archiveFile.get().asFile.toPath()
        Files.copy(jarJarFile, tempLocation)
        Files.delete(jarJarFile)
        try (final ins = new JarInputStream(Files.newInputStream(tempLocation))
            final out = new JarOutputStream(Files.newOutputStream(jarJarFile), ins.getManifest())) {
            ZipEntry subEntry
            while ((subEntry = ins.getNextEntry()) != null) {
                if (subEntry.name.contains('aplp')) {
                    out.putNextEntry(new ZipEntry(subEntry.name))
                    out.write(new FileInputStream(aplpTransformed).readAllBytes())
                } else {
                    out.putNextEntry(subEntry)
                    out.write(ins.readAllBytes())
                }
                out.closeEntry()
            }
            out.close()
            Files.delete(tempLocation)
        } catch (IOException e) {
            e.printStackTrace()
        }
    }
}
