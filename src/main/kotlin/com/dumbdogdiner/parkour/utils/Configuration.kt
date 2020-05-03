package com.dumbdogdiner.parkour.utils

import com.dumbdogdiner.parkour.ParkourPlugin
import com.dumbdogdiner.parkour.courses.Course

object Configuration {
    private val plugin = ParkourPlugin.instance

    fun loadDefaultConfig() {
        val config = plugin.config;

        config.options()
            .header("""
                ${plugin.description.name} v${plugin.description.version}
                
                Parkour Main Configuration File
                
                Developer(s): ${plugin.description.authors}\n
            """.trimIndent());

        config.addDefault("courses", mutableListOf<CourseData>());
        config.addDefault(path + "prefix", "&5&lPawkour> &r");
        config.addDefault(path + "noPermission", "&cYou do not have permission to run this command.");
        config.addDefault(path + "noConsole", "&cThis command must be run as a player.")
        config.addDefault(path + "unknownSubCommand", "&cUnknown or invalid subcommand.")
        config.addDefault(path + "invalidCommandUsage", "&cInvalid usage: %USAGE%")

        config.addDefault(path + "listCount", "There are currently &b%COUNT% &r courses.")

        config.addDefault(path + "createEditingSession", "Right-click on pressure plates with the Course Editor to add them to the course!")
        config.addDefault(path + "checkpointNotFound", "&cCould not find that checkpoint.")
        config.addDefault(path + "checkpointExists", "&cA checkpoint for this course already exists at this pressure plate!")
        config.addDefault(path + "checkpointCreated", "Checkpoint added!")

        config.options().copyDefaults(true);
        plugin.saveConfig();
        plugin.reloadConfig();
    }



    internal interface CourseData {
        val checkpoints: MutableList<String>;
    }

    fun getCourses(): List<Course> {
        val raw: MutableList<CourseData> = ParkourPlugin.instance.config.getList("courses") as MutableList<CourseData>
        return raw.map { data -> parseCourse(data) }
    }

    private fun parseCourse(data: CourseData): Course {
        val course = Course();
        data.checkpoints.forEach { coords -> course.addCheckpoint(Utils.makeLocation(coords)) }
        return course;
    }

    private const val path = "language.";

}