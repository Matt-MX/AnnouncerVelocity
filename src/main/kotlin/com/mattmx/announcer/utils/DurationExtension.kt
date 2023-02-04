package com.mattmx.announcer.utils

import org.apache.commons.lang3.time.DurationFormatUtils
import java.time.Duration

fun Duration.formatted() =
    DurationFormatUtils.formatDuration(
        this.toMillis(),
        "**h:mm:ss**",
        true
    )