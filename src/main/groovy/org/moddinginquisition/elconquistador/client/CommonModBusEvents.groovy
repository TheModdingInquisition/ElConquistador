package org.moddinginquisition.elconquistador.client


import groovy.transform.CompileStatic
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent
import net.thesilkminer.mc.austin.api.EventBus
import net.thesilkminer.mc.austin.api.EventBusSubscriber
import org.moddinginquisition.elconquistador.Configs

import static org.moddinginquisition.elconquistador.ElConquistador.*

@CompileStatic
@EventBusSubscriber(modId = MOD_ID, bus = EventBus.MOD)
class CommonModBusEvents {

    @SubscribeEvent
    static void onLoadComplete(final FMLLoadCompleteEvent event) {
        LOGGER.info 'Hello from FMLLoadCompleteEvent'
    }
}
