package org.moddinginquisition.elconquistador

import groovy.transform.CompileStatic
import net.thesilkminer.mc.austin.api.Mojo
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Mojo(MOD_ID)
@CompileStatic
class ElConquistador {
    static final String MOD_ID = 'elconquistador' // The value here should match an entry in the META-INF/mods.toml file
    static final Logger LOGGER = LoggerFactory.getLogger(ElConquistador)

    ElConquistador() {
        LOGGER.info 'El Conquistador is starting his voyage to the world of Minecraft!'
    }
}
