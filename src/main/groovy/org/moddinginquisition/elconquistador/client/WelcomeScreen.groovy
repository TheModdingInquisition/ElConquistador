package org.moddinginquisition.elconquistador.client

import ga.ozli.minecraftmods.groovylicious.dsl.ScreenBuilder
import groovy.transform.CompileStatic
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.TitleScreen
import org.moddinginquisition.elconquistador.Configs

import static ga.ozli.minecraftmods.groovylicious.api.gui.ColoursRegistry.instance as Colours

@CompileStatic
class WelcomeScreen {
    static Screen screen = ScreenBuilder.makeScreen('ElConquistador Welcome Screen') {
        drawBackground = true

        label {
            text 'El Conquistador installed successfully!'
            position {
                x = 10
                y = 10
            }
            textColour Colours.WHITE
        }

        button {
            text 'Close'
            position {
                x = 50
                y = 50
            }
            size {
                width = 100
                height = 20
            }
            onPress {
                Configs.Client.showWelcomeScreen = false
                Minecraft.instance.screen = new TitleScreen()
            }
        }
    }
}
