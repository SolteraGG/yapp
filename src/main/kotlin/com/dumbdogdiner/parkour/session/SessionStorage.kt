package com.dumbdogdiner.parkour.session

import com.dumbdogdiner.parkour.Base
import com.dumbdogdiner.parkour.courses.Course
import com.dumbdogdiner.parkour.utils.Utils

import java.io.File
import java.io.IOException
import java.util.*

import org.bukkit.Bukkit
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player

/**
 * A utility class for storing player sessions and fetching records/personal bests.
 */
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
     * Fetch the record session for a given course.
     */
    fun fetchRecordSession(course: Course): StoredSession? {
        val session = StoredSession()
        val section = config.getConfigurationSection("${course.name}:top") ?: return null

        session.time = section.getDouble("time")
        session.player = Bukkit.getOfflinePlayer(UUID.fromString(section.getString("player")))
        session.course = course

        return session
    }

    /**
     * Fetch a player's session.
     */
    fun fetchPlayerSession(player: Player, course: Course): StoredSession? {
        val session = StoredSession()
        val section = config.getConfigurationSection("${course.name}:${player.uniqueId}") ?: return null

        session.time = section.getDouble("time")
        session.player = player
        session.course = course

        return session
    }

    /**
     * Store a session as a record session.
     */
    fun storeRecordSession(session: StoredSession) {
        var section = config.getConfigurationSection("${session.course.name}:top")

        if (section == null) {
            section = config.createSection("${session.course.name}:top")
        }

        section.set("time", session.time)
        section.set("player", session.player.uniqueId.toString())

        config.save(file)
        Utils.log("Saved record session for course '${session.course.name}' to disk.")
    }

    /**
     * Store a player's session.
     */
    fun storePlayerSession(session: StoredSession) {
        var section = config.getConfigurationSection("${session.course.name}:${session.player.uniqueId}")

        if (section == null) {
            section = config.createSection("${session.course.name}:${session.player.uniqueId}")
        }

        section.set("time", session.time)

        config.save(file)
        Utils.log("Saved personal best for player '${session.player.uniqueId}' on course '${session.course.name}'.")
    }

    /**
     * Fetch the record for a given course.
     */
    fun fetchRecord(course: Course): Double {
        val session = fetchRecordSession(course) ?: return Double.MAX_VALUE
        return session.time
    }

    /**
     * Fetch the sessions in ascending times.
     */
    fun fetchOrderedSessions(course: Course): List<StoredSession>? {

        val orderedKeys = config.getKeys(false)
            .filter { it.startsWith(course.name) }
            .sortedBy { config.getDouble("$it.time", Double.MAX_VALUE) }

        return orderedKeys.map {
            val session = StoredSession()
            val section = config.getConfigurationSection(it) ?: return null

            session.time = section.getDouble("time")
            session.player = Bukkit.getOfflinePlayer(UUID.fromString(section.getString("player")))
            session.course = course

            session
        }
    }

    /**
     * Fetch the nth time on a given course.
     */
    fun fetchNthTime(course: Course, position: Int): Double? {
        val sessions: List<StoredSession>? = fetchOrderedSessions(course) ?: return null
        val session = sessions?.getOrNull(position) ?: return null
        return session.time
    }

    /**
     * Fetch a player's best time.
     */
    fun getPlayerBest(player: Player, course: Course): Double {
        val session = fetchPlayerSession(player, course) ?: return Double.MAX_VALUE
        return session.time
    }
}
