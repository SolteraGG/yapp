package com.dumbdogdiner.parkour.courses

import com.dumbdogdiner.parkour.Base
import com.okkero.skedule.schedule
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import org.bukkit.Location

class CourseManager : Base {
    private val courses = HashMap<String, Course>()
    private val storage = CourseStorage()

    init {
        // Schedule course loading one tick after worlds are loaded.
        plugin.schedule {
            waitFor(1)
            logger.info("Worlds are loaded - reading course configuration...")
            storage.fetchCourses().forEach { addCourse(it, true) }
            logger.info("Loaded ${courses.size} courses from configuration.")
        }
    }

    /**
     * Add a course.
     */
    fun addCourse(course: Course, preventSave: Boolean = false) {
        courses[course.id.toString()] = course
        if (preventSave) {
            return
        }
        storage.saveCourse(course)
    }

    /**
     * Remove a course.
     */
    fun removeCourse(course: Course) {
        courses.remove(course.id.toString())
        storage.removeCourse(course)
    }

    /**
     * Find a course who's first checkpoint is at the given location.
     */
    fun findCourseFromStart(location: Location): Course? {
        return courses.values.find { it.getCheckpoints()[0] == location }
    }

    /**
     * Fetch all loaded courses.
     */
    fun getCourses(): List<Course> {
        return courses.values.toList()
    }

    /**
     * Save all courses currently in memory.
     */
    fun saveCourses() {
        storage.saveCourses(courses.values.toMutableList())
    }
}
