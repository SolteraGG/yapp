package com.dumbdogdiner.yapp.utils

import com.dumbdogdiner.yapp.Base

/**
 * Object for managing the creation, migration, and default registrin
 */
object Configuration : Base {
    fun loadDefaultConfig() {
        config.options()
            .header("""
                ${plugin.description.name} v${plugin.description.version}

                Main Configuration File
                
                Developer(s): ${plugin.description.authors}\n
            """.trimIndent())

        config.addDefault("disableSound", false)
        config.addDefault("enableControls", false)
        config.addDefault("disablePadParticles", false)

        // BEGIN LINGUISTICS
        config.addDefault(path + "prefix", "&d&lParkour &r&8» &r")
        config.addDefault(path + "noPermission", "&cYou do not have permission to run this command.")
        config.addDefault(path + "noConsole", "&cThis command must be run as a player.")
        config.addDefault(path + "unknownSubCommand", "&cUnknown or invalid subcommand.")
        config.addDefault(path + "invalidCommandUsage", "&cInvalid usage: %USAGE%")

        // Session linguistics
        config.addDefault(path + "newBestTime", "&bCongratulations! You scored a new best time of &a&l%TIME%s&r&b!")
        config.addDefault(path + "startCourse", "&bCourse &a'%COURSE%' &bstarted!")
        config.addDefault(path + "finishCourse", "&bYou finished the course &a'%COURSE%' &b- awesome!")
        config.addDefault(path + "nextCheckpoint", "&e&lCheckpoint &r&bpassed!")
        config.addDefault(path + "restartCourse", "&bTimer restarted!")
        config.addDefault(path + "newRecord", "&d&lRECORD&b - &c%TIME%s &bon &a'%COURSE%' &bby &c%PLAYER%")
        config.addDefault(path + "exitSession", "&bExited the current session.")

        // Command-specific linguistics.
        config.addDefault(path + "listCount", "&bThere are currently &a%COUNT% &bcourses.")
        config.addDefault(path + "courseDeleted", "&bCourse &a'%COURSE%' &bdeleted.")
        config.addDefault(path + "noSession", "&cYou are not currently in a session.")

        // Editing linguistics
        config.addDefault(path + "createEditingSession", "&bRight-click on pressure plates with the &6&lCourse Editor &r&bto add them to the course!")
        config.addDefault(path + "courseNotFound", "&cThat course could not be found.")
        config.addDefault(path + "courseExists", "&cThe course &a'%COURSE%' &calready exists.")
        config.addDefault(path + "checkpointNotFound", "&cCould not find that checkpoint.")
        config.addDefault(path + "checkpointExists", "&cA checkpoint for this course already exists at this pressure plate!")
        config.addDefault(path + "checkpointCreated", "&bCheckpoint added!")
        config.addDefault(path + "checkpointRemoved", "&bCheckpoint removed.")
        config.addDefault(path + "badWorld", "Trans-dimensional parkour is &c&lILLEGAL&r. All checkpoints must be in the same world.")
        config.addDefault(path + "badLength", "&c&lInsufficient length! &r&cMake sure a course has at least 2 checkpoints before exiting.")
        config.addDefault(path + "badBlock", "&cThis is not a pressure plate!")
        config.addDefault(path + "courseSaved", "&bSaved course!")

        // Misc Linguistics
        config.addDefault(path + "blockIsCheckpoint", "&c&lHey! &rYou can't break that block.")

        // END LINGUISTICS

        config.options().copyDefaults(true)
        plugin.saveConfig()
        plugin.reloadConfig()
    }

    private const val path = "language."
}
