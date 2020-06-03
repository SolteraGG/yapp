package com.dumbdogdiner.yapp.utils

import com.dumbdogdiner.yapp.Base

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import org.bukkit.entity.Player

import com.okkero.skedule.BukkitDispatcher

/**
 * Utility class for dealing with elapsed time.
 */
object TimerUtils {
    class Timer(private var player: Player) : Base {
        private var start: Long = -1L
        private var running = false
        private lateinit var job: Job

        /**
         * Start this timer.
         */
        fun start() {
            if (running) {
                stop()
            }

            start = System.currentTimeMillis()
            running = true

            job = GlobalScope.launch(BukkitDispatcher(plugin)) {
                while(running) {
                    updateDisplay()
                    delay(100)
                }
            }
        }

        /**
         * Stop the timer, returning the duration it was running for.
         */
        fun stop(): Long {
            running = false
            job.cancel()

            player.sendActionBar(Utils.colorize("&a&lCourse complete!"))

            return System.currentTimeMillis() - start
        }

        /**
         * Reset the timer, setting the start date to the current time.
         */
        fun reset() {
            start = System.currentTimeMillis()
        }

        /**
         * Update the timer display.
         */
        private fun updateDisplay() {
            player.sendActionBar(
                Utils.colorize(
                    "&b${Utils.round((System.currentTimeMillis() - start).toDouble() / 1000, 1)}s"
                )
            )
        }
    }

    /**
     * Create a display bar timer for the specified player.
     * This method automatically starts the timer.
     */
    fun createTimer(player: Player): Timer {
        val timer = Timer(player)
        timer.start()
        return timer
    }
}