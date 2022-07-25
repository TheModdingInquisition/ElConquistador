package org.moddinginquisition.elconquistador.client

import ga.ozli.minecraftmods.groovylicious.api.ConfigUtils
import ga.ozli.minecraftmods.groovylicious.api.gui.Alignment
import ga.ozli.minecraftmods.groovylicious.api.gui.ExtensibleScreen
import ga.ozli.minecraftmods.groovylicious.dsl.ScreenBuilder
import groovy.transform.CompileStatic
import net.minecraft.client.Minecraft
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent
import net.minecraftforge.fml.loading.FMLPaths
import net.thesilkminer.mc.austin.api.EventBus
import net.thesilkminer.mc.austin.api.EventBusSubscriber

import static org.moddinginquisition.elconquistador.ElConquistador.MOD_ID

@CompileStatic
@EventBusSubscriber(modId = MOD_ID, bus = EventBus.MOD, dist = Dist.CLIENT)
class ClientModBusEvents {

    static final ExtensibleScreen configScreen = ScreenBuilder.makeScreen('ElConquistador Config Screen') {
        drawBackground = true

        label {
            text 'ElConquistador Config Screen'
            position x: 0, y: 20
            alignment Alignment.CENTRE
        }

        button {
            text 'Open common config'
            position(50, 50)
            size(150, 20)
            onPress {
                configScreen.openFile(new File(FMLPaths.CONFIGDIR.get().toString(), "$MOD_ID-common.toml"))
            }
        }

        button {
            text 'Open client config'
            position(50, 80)
            size(150, 20)
            onPress {
                // todo: support Configs.Client.configFile in Groovylicious
                configScreen.openFile(new File(FMLPaths.CONFIGDIR.get().toString(), "$MOD_ID-client.toml"))
            }
        }

        button {
            text 'Close'
            position {
                x = 50
                y = 120
            }
            size {
                width = 150
                height = 20
            }
            onPress {
                Minecraft.instance.screen = configScreen.returnToScreen
            }
        }
    }

    @SubscribeEvent
    static void onModConstruction(final FMLConstructModEvent event) {
        ConfigUtils.registerConfigScreen(MOD_ID, configScreen)
    }
}
