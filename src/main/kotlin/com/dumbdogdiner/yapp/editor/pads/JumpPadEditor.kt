package com.dumbdogdiner.yapp.editor.pads

import com.dumbdogdiner.yapp.Base
import com.dumbdogdiner.yapp.courses.Checkpoint
import com.dumbdogdiner.yapp.editor.EditingSession
import com.dumbdogdiner.yapp.structures.JumpPad
import com.dumbdogdiner.yapp.utils.MathUtils
import com.dumbdogdiner.yapp.utils.SoundUtils
import com.dumbdogdiner.yapp.utils.VectorUtils
import com.okkero.skedule.BukkitDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Location

/**
 * Editor class for jump pads.
 */
class JumpPadEditor(
    val session: EditingSession,
    val checkpoint: Checkpoint,
    val location: Location
) : Base {
    private val pad = JumpPad(
            location,
            VectorUtils.rotFromLoc(session.player.location),
            1
    )

    private lateinit var job: Job
    private var shouldUpdate = false

    init {
        start()
    }

    /**
     * Begins updating location based on the player's view angle.
     */
    fun start() {
        if (shouldUpdate) {
            return
        }

        shouldUpdate = true

        job = GlobalScope.launch(BukkitDispatcher(plugin)) {
            while (shouldUpdate) {
                val dir = VectorUtils.rotFromLoc(session.player.location)
                if (pad.direction != dir) {
                    pad.direction = dir
                    SoundUtils.tick(
                            session.player,
                            MathUtils.inverseLerp(0f, 90f, session.player.location.pitch) + 0.5f
                    )
                }
                delay(250)
            }
        }
    }

    /**
     * End the editing session and return the modified jump pad.
     */
    fun end(): JumpPad {
        shouldUpdate = false
        job.cancel()
        return pad
    }
}
