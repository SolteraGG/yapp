package com.dumbdogdiner.parkour.courses

import com.dumbdogdiner.parkour.Base
import com.okkero.skedule.schedule

import org.bukkit.Location

class CourseManager : Base {
    private val courses = mutableListOf<Course>()
    private val storage = CourseStorage()

    init {
        // Schedule course loading one tick after worlds are loaded.
        plugin.schedule {
            logger.info("Worlds are loaded - reading course configuration...")
            storage.fetchCourses().forEach { addCourse(it, true) }
            logger.info("Loaded ${courses.size} courses from configuration.")
        }
    }

    /**
     * Add a course.
     */
    fun addCourse(course: Course, preventLog: Boolean = false) {
        if (course.id == -1) {
            course.id = this.courses.size
        }

        courses.add(course)
        storage.saveCourse(course)

        if (preventLog) {
            return
        }
    }

    /**
     * Update a course.
     */
    fun updateCourse(course: Course) {
        if (courses.size < course.id) {
            return addCourse(course)
        }

        courses[course.id] = course
        storage.saveCourse(course)
    }

    /**
     * Remove a course.
     */
    fun removeCourse(course: Course) {
        courses.remove(course)
        storage.removeCourse(course)
    }

    /**
     * Find a course who's first checkpoint is at the given location.
     */
    fun findCourseFromStart(location: Location): Course? {
        return courses.find { it.getCheckpoints()[0] == location }
    }

    /**
     * Fetch all loaded courses.
     */
    fun getCourses(): List<Course> {
        return courses
    }

    /**
     * Save all courses currently in memory.
     */
    fun saveCourses() {
        storage.saveCourses(courses)
    }
}
