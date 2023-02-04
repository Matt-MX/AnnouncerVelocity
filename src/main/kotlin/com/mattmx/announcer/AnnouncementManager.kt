package com.mattmx.announcer

import com.mattmx.announcer.announcements.Announcement
import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.scheduler.ScheduledTask
import java.time.Duration

object AnnouncementManager {
    private lateinit var server: ProxyServer
    private val announcements = arrayListOf<Announcement>()
    private lateinit var task: ScheduledTask

    fun init(server: ProxyServer, plugin: Any) {
        if (::server.isInitialized)
            return
        this.server = server

        task = server.scheduler.buildTask(plugin) { _ ->

        }.delay(Duration.ofMillis(10))
            .repeat(Duration.ofMillis(10)).schedule()
    }

    fun run() {
        announcements.toMutableList().forEach {
            if (it.shouldExecute())
                it.execute(server)
        }
    }

    fun cancel() {
        if (::task.isInitialized)
            task.cancel()
    }

    fun clear() = announcements.clear()

    fun all() = announcements.toMutableList()

    fun register(announcement: Announcement) {
        announcements += announcement
    }

    fun unregister(announcement: Announcement) {
        announcements.remove(announcement)
    }

    fun unregisterById(id: String) {
        val announcement = getById(id) ?: return
        unregister(announcement)
    }

    fun getById(id: String) = announcements.firstOrNull { it.getId() == id }

    @JvmName("getByIdWithType")
    fun <T : Announcement> getById(id: String) = announcements.firstOrNull { it.getId() == id } as T?

    operator fun contains(announcement: Announcement) = announcements.toMutableList().contains(announcement)

}