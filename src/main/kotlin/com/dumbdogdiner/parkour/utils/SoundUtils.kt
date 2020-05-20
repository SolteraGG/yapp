package com.dumbdogdiner.parkour.utils

import com.dumbdogdiner.parkour.Base
import com.dumbdogdiner.parkour.ParkourPlugin
import com.okkero.skedule.BukkitDispatcher
import kotlinx.coroutines.*
import org.bukkit.Sound
import org.bukkit.entity.Player

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
     * Error sound.
     */
    fun error(player: Player) {
        GlobalScope.launch(BukkitDispatcher(plugin)) {
            playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS)
            playSound(player, Sound.ENTITY_ITEM_BREAK)
            playSound(player, Sound.ENTITY_FOX_HURT)
        }
    }

    private fun playSound(player: Player, soundName: Sound, pitch: Float = 1f) {
        if (ParkourPlugin.instance.config.getBoolean("disableSound")) {
            return
        }

        player.playSound(player.location, soundName, 1f, pitch)
    }
}
