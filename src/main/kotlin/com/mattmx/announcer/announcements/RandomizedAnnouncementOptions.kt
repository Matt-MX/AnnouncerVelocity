package com.mattmx.announcer.announcements

import java.time.Duration

class RandomizedAnnouncementOptions(
    val delayMin: Duration,
    val delayMax: Duration,
    permissionToHide: String? = null,
    permissionToDisplay: String? = null,
    targetServers: Regex? = null,
) : SimpleAnnouncementOptions(
    delayMin,
    permissionToHide,
    permissionToDisplay,
    targetServers
) {
    init {
        generateDelay()
    }

    fun generateDelay() : Duration {
        delay = Duration.ofMillis((delayMin.toMillis()..delayMax.toMillis()).random())
        return delay
    }

}