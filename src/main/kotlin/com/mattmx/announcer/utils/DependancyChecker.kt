package com.mattmx.announcer.utils

object DependencyChecker {
    fun protocolize(): Boolean {
        return try {
            Class.forName("dev.simplix.protocolize.api.Protocolize")
            true
        } catch (exception: ClassNotFoundException) {
            false
        }
    }
}