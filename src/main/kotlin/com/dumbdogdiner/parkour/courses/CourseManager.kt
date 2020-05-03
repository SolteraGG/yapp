package com.dumbdogdiner.parkour.courses

import com.dumbdogdiner.parkour.ParkourPlugin
import com.dumbdogdiner.parkour.utils.Utils
import org.bukkit.Location

class CourseManager(private val plugin: ParkourPlugin) {
    private val courses = mutableListOf<Course>()
    val storage = CourseStorage(this)

    init {
        storage.fetchCourses().forEach { this.addCourse(it) }
        Utils.log("Loaded ${courses.size} courses from configuration.")
    }

    /**
     * Add a course.
     */
    fun addCourse(course: Course) {
        courses.add(course)
    }

    /**
     * Find a course who's first checkpoint is at the given location.
     */
    fun findCourseFromStart(location: Location): Course? {
        return courses.find { it.getCheckpoints()[0] == location }
    }

    fun getCourses(): List<Course> {
        return courses
    }

    fun saveCourses() {
        storage.saveCourses(courses)
    }
}