package com.dumbdogdiner.parkour.courses

import com.dumbdogdiner.parkour.ParkourPlugin
import com.dumbdogdiner.parkour.utils.Configuration
import com.dumbdogdiner.parkour.utils.Utils
import org.bukkit.Location

class CourseManager(private val plugin: ParkourPlugin) {
    private var courses = HashMap<String, Course>()

    init {
        Configuration.getCourses().forEach { course -> addCourse(course) }
        Utils.log("Loaded ${courses.size} courses from configuration.")
    }

    /**
     * Add a course.
     */
    fun addCourse(course: Course) {
        courses[Utils.makeShortCoords(course.getOrderedCheckpoints()[0])]
    }

    fun getCourse(k: String): Course? {
        return courses[k]
    }

    fun getCourse(loc: Location): Course? {
        return courses[Utils.makeShortCoords(loc)]
    }

    fun getCourses(): List<Course> {
        return courses.values.toList()
    }
}