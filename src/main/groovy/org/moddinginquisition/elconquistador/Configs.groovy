package org.moddinginquisition.elconquistador

import ga.ozli.minecraftmods.groovylicious.transform.Config
import groovy.transform.CompileStatic
import groovy.transform.stc.POJO

@POJO
@CompileStatic
class Configs {

    @Config
    static class Common {
        static class KnownSlowModsNotifier {
            /**
             * If true, we'll notify you when we detect that you're using a mod known to cause lag and if there's a fixed updated version available.
             */
            static boolean enable = true

            /**
             * By default, we'll only notify you about known slow mods when there's an updated fixed version already available.<br>
             * If you want to be notified about all slow mods, set this to false.
             */
            static boolean onlyNotifyWhenUpdateAvailable = true
        }
    }

    @Config
    static class Client {
        /**
         * Low framerate notification threshold
         * @range 1..200
         */
        static short lowFramerateThreshold = 10

        static boolean showWelcomeScreen = true
    }
}
