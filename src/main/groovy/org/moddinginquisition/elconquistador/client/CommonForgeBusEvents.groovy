package org.moddinginquisition.elconquistador.client

import groovy.transform.CompileStatic
import me.lucko.spark.api.Spark
import me.lucko.spark.api.SparkProvider
import me.lucko.spark.api.profiler.Profiler
import me.lucko.spark.api.profiler.ProfilerConfiguration
import me.lucko.spark.api.profiler.ProfilerConfigurationBuilder
import me.lucko.spark.api.profiler.report.ReportConfiguration
import me.lucko.spark.api.statistic.StatisticWindow
import me.lucko.spark.proto.SparkSamplerProtos
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.thesilkminer.mc.austin.api.EventBus
import net.thesilkminer.mc.austin.api.EventBusSubscriber

import java.time.Duration

import static org.moddinginquisition.elconquistador.ElConquistador.LOGGER
import static org.moddinginquisition.elconquistador.ElConquistador.MOD_ID

@CompileStatic
@EventBusSubscriber(modId = MOD_ID, bus = EventBus.FORGE)
class CommonForgeBusEvents {

    static int counter = 0

    //@SubscribeEvent
    static void onServerTick(final TickEvent.ServerTickEvent event) {
        if (counter > 1000) {
            LOGGER.info SV(SparkProvider.get().tps().poll(StatisticWindow.TicksPerSecond.SECONDS_5))

            counter = 0
            profile()
        }
        counter++
    }

    static void profile() {
        final Spark sparkApi = SparkProvider.get()
        final Profiler profiler = sparkApi.profiler(1)
        final ReportConfiguration reportConfiguration = ReportConfiguration.builder().sender('ElConquistadorMod').build()

        final Profiler.Sampler sampler = profiler.createSampler(
                ProfilerConfiguration.builder()
                        .duration(Duration.ofSeconds(10))
                        .build(), { e, msg -> LOGGER.info(msg) }
        )

        sampler.start()

        sampler.onCompleted(reportConfiguration).whenComplete { report, throwable ->
            final SparkSamplerProtos.SamplerData samplerData = report.data()
            LOGGER.info SV(samplerData.metadata.systemStatistics.cpu)
            LOGGER.info SV(samplerData)
//            it.data().threadsList.get(0)
//            LOGGER.info "Report uploaded to ${it.upload()}"
        }
    }
}
