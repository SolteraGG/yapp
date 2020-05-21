package com.dumbdogdiner.parkour.listeners

import com.dumbdogdiner.parkour.Base
import org.bukkit.event.EventHandler

import org.bukkit.event.Listener
import org.bukkit.event.world.WorldLoadEvent
import org.bukkit.event.world.WorldUnloadEvent

/**
 * Listener for dealing with world unloads.
 *
 * Location.serialize throws an error when attempting to create a location in a world that
 * has not yet been loaded. This attempts to get around that.
 *
 * TODO: Implement dynamic course loading/unloading.
 */
class WorldListener : Listener, Base {
    @EventHandler
    fun onWorldLoad(e: WorldLoadEvent) {}

    @EventHandler
    fun onWorldUnload(e: WorldUnloadEvent) {}
}