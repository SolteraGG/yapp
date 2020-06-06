package com.dumbdogdiner.yapp.editor.pads

import com.dumbdogdiner.yapp.courses.Checkpoint
import com.dumbdogdiner.yapp.editor.EditingSession
import com.dumbdogdiner.yapp.structures.EffectPad
import org.bukkit.Effect
import org.bukkit.Location
import org.bukkit.potion.PotionEffect

/**
 * Editor class for effect pads.
 */
class EffectPadEditor(
    val session: EditingSession,
    val checkpoint: Checkpoint,
    val location: Location
) {
    private val pad = EffectPad(location, mutableListOf<PotionEffect>())

    init {
        start()
    }

    /**
     * Open the player's inventory UI.
     */
    fun start() {
    }

    /**
     * Add an effect to the pad.
     */
    fun addEffect(effect: EffectPad.Effect, duration: Int, strength: Int): Boolean {
        return pad.addEffect(effect, duration, strength)
    }
}
