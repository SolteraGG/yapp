package com.dumbdogdiner.yapp

import com.dumbdogdiner.yapp.utils.Utils
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player

/**
 * PlaceholderAPI Expansion class
 */
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
        if (identifier.startsWith("course_")) {
            val args = identifier.split("_")
            val name = args[1]

            // Check course exists
            val course = courseManager.getCourses().find { it.name == name }
            if (course == null) {
                Utils.log("[PlaceholderAPI] Unknown course '$name'.")
                return "UNKNOWN"
            }

            if (args.size < 3) {
                Utils.log("[PlaceholderAPI] Unknown placeholder command '$identifier'.")
                return "INVALID"
            }

            return when (val cmd = args.subList(2, args.size).joinToString(separator = "_")) {
                "checkpoint_count" -> {
                    course.getCheckpoints().size.toString()
                }
                "personal_best" -> {
                    val attempt = sessionManager.storage.getPlayerBest(player, course)
                    if (attempt == -1.0) {
                        return "Not Attempted"
                    }
                    return attempt.toString()
                }
                "record_3" -> {
                    val attempt = sessionManager.storage.fetchNthTime(course, 2) ?: return "Not Attempted"
                    return attempt.toString()
                }
                "record_2" -> {
                    val attempt = sessionManager.storage.fetchNthTime(course, 1) ?: return "Not Attempted"
                    return attempt.toString()
                }
                "record" -> {
                    "${sessionManager.storage.fetchRecord(course)}"
                }
                else -> {
                    Utils.log("[PlaceholderAPI] Unknown placeholder command '$cmd'.")
                    "INVALID"
                }
            }
        }

        Utils.log("[PlaceholderAPI] Unknown placeholder command '$identifier'.")
        return "INVALID"
    }
}
