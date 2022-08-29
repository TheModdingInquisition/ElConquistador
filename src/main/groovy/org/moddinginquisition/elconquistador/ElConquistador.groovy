package org.moddinginquisition.elconquistador

import com.matyrobbrt.gml.GMod
import com.mojang.logging.LogUtils
import groovy.transform.CompileStatic
import org.slf4j.Logger

@GMod(MOD_ID)
@CompileStatic
class ElConquistador {
    static final String MOD_ID = 'elconquistador' // The value here should match an entry in the META-INF/mods.toml file
    static final Logger LOGGER = LogUtils.getLogger(ElConquistador)

    ElConquistador() {
        LOGGER.info 'El Conquistador is starting his voyage to the world of Minecraft!'
    }
}
