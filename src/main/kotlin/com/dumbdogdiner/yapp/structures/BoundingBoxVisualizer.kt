package com.dumbdogdiner.yapp.structures

import com.dumbdogdiner.yapp.Base
import com.dumbdogdiner.yapp.utils.BoundUtils
import com.dumbdogdiner.yapp.utils.ParticleUtils
import com.okkero.skedule.BukkitDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.World
import org.bukkit.util.BoundingBox

/**
 * Visualizes bounding boxes by repeatedly spawning their particles.
 */
class BoundingBoxVisualizer(private val world: World, bounds: BoundingBox) : Base {
    private val edges = BoundUtils.getBoundingBoxEdges(world, bounds)
    private var show = false
    private lateinit var job: Job

    /**
     * Show this visualizer's bounding box.
     */
    fun show() {
        if (show) {
            return
        }

        show = true

        job = GlobalScope.launch(BukkitDispatcher(plugin)) {
            while (show) {
                ParticleUtils.drawBoundingBox(world, edges)
                delay(500)
            }
        }
    }

    /**
     * Hide this visualizer's bounding box.
     */
    fun hide() {
        job.cancel()
        show = false
    }
}
