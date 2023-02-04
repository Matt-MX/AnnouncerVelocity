package com.mattmx.announcer.event

import com.mattmx.announcer.announcements.Announcement

class AnnouncementFireEvent(
    val announcement: Announcement,
    var isCancelled: Boolean = false
)