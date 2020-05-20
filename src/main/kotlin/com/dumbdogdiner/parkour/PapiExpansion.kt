package com.dumbdogdiner.parkour

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player

class PapiExpansion : PlaceholderExpansion(), Base {

    override fun persist(): Boolean {
        return true
    }

    override fun canRegister(): Boolean {
        return true
    }

    override fun getAuthor(): String {
        return plugin.description.authors.toString()
    }

    override fun getIdentifier(): String {
        return "parkour"
    }

    override fun getVersion(): String {
        return plugin.description.version
    }

    override fun onPlaceholderRequest(player: Player, identifier: String): String? {
        // Get the list of courses.
        val courses = plugin.courseManager.getCourses()

        if (identifier.startsWith("course_")) {
            // Strip out the 'course' prefix of the identifier.
            val subIdentifier = identifier.substringAfter(identifier.substringBefore("_") + "_")

            val courseId = (subIdentifier.substringBefore("_")).toIntOrNull()

            val option = subIdentifier.substringAfter(String.format("%s_", courseId))

            // ---- DEBUG STATEMENTS START -----
            logger.info("SUBID: " + subIdentifier)
            logger.info("COURSEID: " + courseId)
            logger.info("OPTION FORMAT: " + String.format("%s_", courseId))
            logger.info("OPTION: " + option)
            // ----- DEBUG STATEMENTS END -----

            if (courseId == null) {
                logger.warning("[PlaceholderAPI Request] Invalid syntax! (" + identifier + ")")
                return "Invalid syntax"
            }

            // Make sure the course exists before requesting it.
            if (courses.size != (courseId + 1)) {
                logger.warning("[PlaceholderAPI Request] course #" + courseId + " does not exist!")
                return "Course does not exist!"
            }

            val course = courses[courseId]

            // Total Checkpoints
            if (option == "total_checkpoints") {
                return course.getCheckpoints().size.toString()
            }

            // Top 10 Times
            if (option.startsWith("times_top_")) {
                val topNo = (option.split("times_top_")[1]).toIntOrNull()

                // Make sure that the top number is a valid number
                if (topNo == null) {
                    logger.warning("[PlaceholderAPI Request] Top 10: Invalid syntax! ($identifier)")
                    return "Invalid syntax!"

                    // It is a valid number, now make sure it is between one and 10.
                } else if (topNo == 0 || topNo > 10) {
                    logger.warning("[PlaceholderAPI Request] Top 10: Only positions 1-10 are supported!")
                    return "Invalid position!"
                }

                // STUB : Not implemented.
                return "Not Implemented! - Position $topNo"
            }
        }

        return when (identifier) {
            // %parkour_test%
            "test" -> "Hello"

            // %parkour_total_courses%
            "total_courses" -> courses.size.toString()
            else -> null
        }
    }
}
