package org.moddinginquisition.elconquistador.client

import ga.ozli.minecraftmods.groovylicious.api.gui.Alignment
import ga.ozli.minecraftmods.groovylicious.api.gui.ExtensibleScreen
import ga.ozli.minecraftmods.groovylicious.api.gui.Position
import ga.ozli.minecraftmods.groovylicious.dsl.ScreenBuilder
import groovy.transform.CompileStatic
import net.minecraft.Util
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.ConfirmLinkScreen
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.TitleScreen
import org.moddinginquisition.elconquistador.Configs

import static ga.ozli.minecraftmods.groovylicious.api.gui.ColoursRegistry.instance as Colours

@CompileStatic
class WelcomeScreen {
    static final ExtensibleScreen screen = ScreenBuilder.makeScreen('ElConquistador Welcome Screen') {
        drawBackground = true

        //plainTextButton {
        label {
            text 'El Conquistador installed successfully!'
            position {
                x = 50
                y = 10
            }
            size {
                width = 200
                height = 20
            }
//            onPress {
//                screen.confirmOpenLink('https://github.com/TheModdingInquisition/ElConquistador')
//            }
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
