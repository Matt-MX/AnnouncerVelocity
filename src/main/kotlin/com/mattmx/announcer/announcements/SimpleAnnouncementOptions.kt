package com.mattmx.announcer.announcements

import java.time.Duration

open class SimpleAnnouncementOptions(
    var delay: Duration,
    var permissionToHide: String? = null,
    var permissionToDisplay: String? = null,
    var targetServers: Regex? = null,
)