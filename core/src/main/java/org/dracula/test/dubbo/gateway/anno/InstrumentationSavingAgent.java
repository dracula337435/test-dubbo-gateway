package org.dracula.test.dubbo.gateway.anno;

import java.lang.instrument.Instrumentation;

/**
 * @author dk
 */
public class InstrumentationSavingAgent {

    private static volatile Instrumentation instrumentation;

    /**
     * Save the {@link Instrumentation} interface exposed by the JVM.
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        instrumentation = inst;
    }

    public static Instrumentation getInstrumentation(){
        return instrumentation;
    }

}