package org.moddinginquisition.elconquistador

import groovy.transform.CompileStatic
import net.minecraftforge.versions.forge.ForgeVersion

import javax.annotation.Nullable

@CompileStatic
class OptifineHandler {
    static final boolean IS_OPTIFINE_INSTALLED
    static final @Nullable String OPTIFINE_VERSION // null if unknown or Optifine isn't installed
    static final @Nullable String INTENDED_FORGE_VERSION // null if unknown or Optifine isn't installed

    static {
        try {
            final Class<?> optifineConfigClass = Class.forName('net.optifine.Config')
            IS_OPTIFINE_INSTALLED = true
            OPTIFINE_VERSION = optifineConfigClass.getDeclaredField('OF_RELEASE').get(optifineConfigClass) as String

            // get the changelog file embedded inside the Optifine jar
            final changelogFileStream = Class.forName('optifine.OptiFineTransformationService').getResourceAsStream('/changelog.txt')

            // read it to a List<String>
            final lines = changelogFileStream.getText().split('\n').toList()

            // parse the changelog to find out the intended Forge version
            INTENDED_FORGE_VERSION = lines
                    .find { it.startsWith ' - compatible with Forge ' }
                    .split(' ')[5]
        } catch (final ClassNotFoundException | NoSuchFieldException e) {
            IS_OPTIFINE_INSTALLED = !(e instanceof ClassNotFoundException)
            if (e instanceof NoSuchFieldException) {
                OPTIFINE_VERSION = null
            }
            INTENDED_FORGE_VERSION = null
        }

        ElConquistador.LOGGER.debug SV(IS_OPTIFINE_INSTALLED, OPTIFINE_VERSION, INTENDED_FORGE_VERSION, ForgeVersion.version)
    }
}
