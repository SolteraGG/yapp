package com.dumbdogdiner.parkour.session

import com.dumbdogdiner.parkour.Base
import com.dumbdogdiner.parkour.courses.Course
import java.io.File
import java.io.IOException
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player

class SessionStorage : Base {
    private val file: File = File(plugin.dataFolder, "records.yml")
    private val config: FileConfiguration

    init {

        if (!file.exists()) {
            file.parentFile.mkdirs()
            plugin.saveResource("records.yml", false)
        }

        config = YamlConfiguration()
        try {
            config.load(file)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InvalidConfigurationException) {
            e.printStackTrace()
        }
    }

    /**
     * Fetch a player's session.
     */
    fun fetchPlayerSession(player: Player, course: Course): StoredSession? {
        val session = StoredSession()
        val section = config.getConfigurationSection("$course.${player.uniqueId}") ?: return null

        session.time = section.getDouble("time")
        session.player = player
        session.course = plugin.courseManager.getCourses()[section.getInt("course")]

        return session
    }

    /**
     * Store a player's session.
     */
    fun storePlayerSession(session: StoredSession) {
        val section = config.getConfigurationSection("${session.course.id}.${session.player.uniqueId}")!!
        section.set("time", session.time)
    }

    /**
     * Fetch a player's best time.
     */
    fun getPlayerBest(player: Player, course: Course): Double {
        val session = fetchPlayerSession(player, course) ?: return -1.0
        return session.time
    }
}
