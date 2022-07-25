package org.moddinginquisition.elconquistador.client

import groovy.transform.CompileStatic
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.TitleScreen
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.ScreenEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.thesilkminer.mc.austin.api.EventBus
import net.thesilkminer.mc.austin.api.EventBusSubscriber
import org.moddinginquisition.elconquistador.Configs

import static org.moddinginquisition.elconquistador.ElConquistador.*

@CompileStatic
@EventBusSubscriber(modId = MOD_ID, bus = EventBus.FORGE, dist = Dist.CLIENT)
class ClientForgeBusEvents {

    static int counter = 0

    @SubscribeEvent
    static void onScreenOpen(final ScreenEvent.Opening event) {
        if (Configs.Client.showWelcomeScreen && event.newScreen instanceof TitleScreen) {
            WelcomeScreen.screen.returnToScreen = event.currentScreen // let the welcome screen know where to go back to when closing
            event.newScreen = WelcomeScreen.screen // open the welcome screen
        }
    }

    @SubscribeEvent
    static void onClickTick(final TickEvent.ClientTickEvent event) {
        if (counter > 20) {
            final int fps = Minecraft.instance.fps
            if (fps !== 0 && fps < Configs.Client.lowFramerateThreshold) {
                LOGGER.info "Low framerate detected: ${fps}FPS"
            }
            counter = 0
        }
        counter++
    }
}
