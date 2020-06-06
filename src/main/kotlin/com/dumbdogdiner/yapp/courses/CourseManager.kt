package com.dumbdogdiner.yapp.courses

import com.dumbdogdiner.yapp.Base
import com.dumbdogdiner.yapp.utils.Utils
import com.okkero.skedule.schedule
import org.bukkit.Location

class CourseManager : Base {
    private val courses = HashMap<String, Course>()
    private val storage = CourseStorage()

    init {
        // Schedule course loading one tick after worlds are loaded.
        plugin.schedule {
            waitFor(1)
            storage.fetchCourses().forEach { addCourse(it, true) }
            Utils.log("Loaded ${courses.size} courses from configuration.")
        }
    }

    /**
     * Add a course.
     */
    fun addCourse(course: Course, preventSave: Boolean = false) {
        courses[course.name] = course
        if (preventSave) {
            return
        }
        storage.saveCourse(course)
    }

    /**
     * Remove a course.
     */
    fun removeCourse(course: Course) {
        courses.remove(course.name)
        storage.removeCourse(course)
    }

    /**
     * Find a course who's first checkpoint is at the given location.
     */
    fun findCourseFromStart(location: Location): Course? {
        return courses.values.find { it.getCheckpoints().first().getEndCheckpoint() == location }
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
