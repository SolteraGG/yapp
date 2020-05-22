package com.dumbdogdiner.parkour.courses

import com.dumbdogdiner.parkour.Base
import com.dumbdogdiner.parkour.ParkourPlugin
import com.dumbdogdiner.parkour.utils.Utils
import org.bukkit.Bukkit

import java.io.File
import java.io.IOException

import org.bukkit.Location
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.util.*

/**
 * Wrapper class for storing course data.
 */
class CourseStorage : Base {
    private val file: File
    private val config: FileConfiguration

    init {
        val plugin = ParkourPlugin.instance

        file = File(plugin.dataFolder, "courses.yml")

        if (!file.exists()) {
            file.parentFile.mkdirs()
            plugin.saveResource("courses.yml", false)
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
     * Fetch all stored courses.
     */
    fun fetchCourses(): MutableList<Course> {
        val courses = mutableListOf<Course>()

        for (key in config.getKeys(false)) {
            val course = Course()

            course.name = key
            config.getConfigurationSection(key)?.getString("description")?.let { course.description = it }

            val checkpoints: List<Location> = fetchCourseCheckpoints(key) ?: continue

            checkpoints.forEach { course.addCheckpointAtLocation(it) }
            courses.add(course)
        }

        return courses
    }

    /**
     * Fetch a course's checkpoints from storage.
     */
    fun fetchCourseCheckpoints(id: String): List<Location>? {
        val section = config.getConfigurationSection(id) ?: return null
        val res = mutableListOf<Location>()

        for (checkpoint in section.getStringList("checkpoints")) {
            val loc = Utils.deserializeLocation(checkpoint) ?: return null
            res.add(loc)
        }

        return res
    }

    /**
     * Save the provided courses to disk.
     */
    fun saveCourses(courses: MutableList<Course>) {
        courses.forEach { saveCourse(it, true) }
        config.save(file)
        Utils.log("Saved ${courses.size} courses to disk.")
    }

    /**
     * Save a course to disk.
     */
    fun saveCourse(course: Course, skipSave: Boolean = false) {
        var section = config.getConfigurationSection(course.name)

        if (section == null) {
            section = config.createSection(course.name)
        }

        section.set("description", course.description)
        section.set("checkpoints", course.getCheckpoints().map { Utils.serializeLocation(it.getEndCheckpoint()) })

        if (skipSave) {
            return
        }
        config.save(file)
        Utils.log("Saved course '${course.name}' to disk.")
    }

    /**
     * Delete a course from the config.
     */
    fun removeCourse(course: Course) {
        config.set(course.name, null)
        Utils.log("Deleted course '${course.name}' from disk.")
    }

}
