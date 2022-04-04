package com.mattmx.announcer.util;

public class DependencyChecker {
    public static boolean protocolize() {
        try {
            Class.forName("dev.simplix.protocolize.api.Protocolize");
            return true;
        } catch (ClassNotFoundException exception) {
            return false;
        }
    }
}