package com.dumbdogdiner.parkour.utils

import com.dumbdogdiner.parkour.Base
import com.dumbdogdiner.parkour.ParkourPlugin
import com.okkero.skedule.BukkitDispatcher
import kotlinx.coroutines.*
import org.bukkit.Sound
import org.bukkit.entity.Player

/**
 * Utility methods for sending adorable fox notification sounds omg this was such a good idea i can't~
 */
object SoundUtils : Base {
    /**
     * Success sound.
     */
    fun success(player: Player) {
        GlobalScope.launch(BukkitDispatcher(plugin)) {
            playSound(player, Sound.ENTITY_PLAYER_LEVELUP)
            delay(500)
            playSound(player, Sound.ENTITY_FOX_SCREECH)
        }
    }

    /**
     * Notification sound.
     */
    fun info(player: Player) {
        GlobalScope.launch(BukkitDispatcher(plugin)) {
            playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING)
            delay(500)
            playSound(player, Sound.ENTITY_FOX_AMBIENT)
        }
    }

    /**
     * Quieter, less important notification sound.
     */
    fun boop(player: Player) {
        GlobalScope.launch(BukkitDispatcher(plugin)) {
            playSound(player, Sound.BLOCK_NOTE_BLOCK_HARP, 1.5f)
            delay(500)
            playSound(player, Sound.ENTITY_FOX_SLEEP)
        }
    }

    /**
     * Error sound.
     */
    fun error(player: Player) {
        GlobalScope.launch(BukkitDispatcher(plugin)) {
            playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS)
            playSound(player, Sound.ENTITY_ITEM_BREAK)
            playSound(player, Sound.ENTITY_FOX_HURT)
        }
    }

    /**
     * Damn boiiii not baddd~
     */
    fun awesome(player: Player) {
        GlobalScope.launch(BukkitDispatcher(plugin)) {
            playSound(player, Sound.ENTITY_PLAYER_LEVELUP)
            delay(500)
            playSound(player, Sound.ENTITY_VILLAGER_CELEBRATE)
        }
    }

    /**
     * OMG WHATTTT
     */
    fun OWO(player: Player) {
        GlobalScope.launch(BukkitDispatcher(plugin)) {
            playSound(player, Sound.ENTITY_PLAYER_LEVELUP)
            playSound(player, Sound.ENTITY_WITHER_SPAWN)
        }
    }

    private fun playSound(player: Player, soundName: Sound, pitch: Float = 1f) {
        if (ParkourPlugin.instance.config.getBoolean("disableSound")) {
            return
        }

        player.playSound(player.location, soundName, 1f, pitch)
    }
}
