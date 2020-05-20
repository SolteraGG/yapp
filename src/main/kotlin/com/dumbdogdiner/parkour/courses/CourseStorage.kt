package com.dumbdogdiner.parkour.courses

import com.dumbdogdiner.parkour.ParkourPlugin
import java.io.File
import java.io.IOException
import org.bukkit.Location
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration

class CourseStorage(private val manager: CourseManager) {
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
            val course = Course(manager)

            course.id = key.toInt()
            config.getConfigurationSection(key)?.getString("name")?.let { course.name = it }
            config.getConfigurationSection(key)?.getString("description")?.let { course.description = it }

            // TODO: Potential bug material.
            val checkpoints: MutableList<Location> = fetchCourseCheckpoints(key) ?: continue
            checkpoints.forEach { course.addCheckpoint(it) }
        }

        return courses
    }

    /**
     * Fetch a course's checkpoints from storage.
     */
    fun fetchCourseCheckpoints(id: String): MutableList<Location>? {
        @Suppress("UNCHECKED_CAST")
        return config.getConfigurationSection(id)?.getList("checkpoints") as MutableList<Location>
    }

    /**
     * Save the provided courses to disk.
     */
    fun saveCourses(courses: MutableList<Course>) {
        courses.forEach { saveCourse(it) }
    }

    /**
     * Save a course to disk.
     */
    fun saveCourse(course: Course) {
        var section = config.getConfigurationSection(course.id.toString())

        if (section == null) {
            section = config.createSection(course.id.toString())
        }

        section.set("name", course.name)
        section.set("description", course.description)
        section.set("checkpoints", course.getCheckpoints())
    }

    /**
     * Delete a course from the config.
     */
    fun removeCourse(course: Course) {
        config.set(course.id.toString(), null)
    }
}
