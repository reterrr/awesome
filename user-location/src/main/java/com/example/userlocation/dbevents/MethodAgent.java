package com.example.userlocation.dbevents;

import javassist.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MethodAgent {
    private static final Map<String, Boolean> methodExecutionMap = new ConcurrentHashMap<>();

    public static void trackMethods(String className) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.get(className);

        for (CtMethod method : ctClass.getDeclaredMethods()) {
            String methodSignature = className + "." + method.getName();
            methodExecutionMap.put(methodSignature, false);

            method.insertBefore("MethodExecutionTracker.setExecuted(\"" + methodSignature + "\");");
        }

        ctClass.toClass();
    }

    private void setExecuted(String methodName) {
        methodExecutionMap.put(methodName, true);
    }

    public static boolean isExecuted(String methodName) {
        return methodExecutionMap.remove(methodName) != null;
    }
}
