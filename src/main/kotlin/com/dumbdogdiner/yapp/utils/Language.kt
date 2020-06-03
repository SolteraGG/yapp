package com.dumbdogdiner.yapp.utils

import com.dumbdogdiner.yapp.YappParkourPlugin

object Language {
    private const val path = "language"
    private val plugin = YappParkourPlugin.instance
    private val config = plugin.config

    private fun get(path: String) = Utils.colorize(config.getString(path)!!)
    private fun getList(path: String) = config.getStringList(path).map { Utils.colorize(it) }

    val prefix
        get() = get("$path.prefix")

    val noPermission
        get() = "$prefix${get("$path.noPermission")}"
    val noConsole
        get() = "$prefix${get("$path.noConsole")}"
    val unknownSubCommand
        get() = "$prefix${get("$path.unknownSubCommand")}"
    val invalidCommandUsage
        get() = "$prefix${get("$path.invalidCommandUsage")}"

    // Session linguistics
    val newBestTime
        get() = "$prefix${get("$path.newBestTime")}"
    val startCourse
        get() = "$prefix${get("$path.startCourse")}"
    val finishCourse
        get() = "$prefix${get("$path.finishCourse")}"
    val nextCheckpoint
        get() = "$prefix${get("$path.nextCheckpoint")}"
    val restartCourse
        get() = "$prefix${get("$path.restartCourse")}"
    val newRecord
        get() = "$prefix${get("$path.newRecord")}"
    val exitSession
        get() = "$prefix${get("$path.exitSession")}"

    // Command-specific
    val listCount
        get() = "$prefix${get("$path.listCount")}"
    val courseDeleted
        get() = "$prefix${get("$path.courseDeleted")}"
    val noSession
        get() = "$prefix${get("$path.noSession")}"

    // Editing linguistics
    val createEditingSession
        get() = "$prefix${get("$path.createEditingSession")}"
    val courseNotFound
        get() = "$prefix${get("$path.courseNotFound")}"
    val courseExists
        get() = "$prefix${get("$path.courseExists")}"
    val checkpointNotFound
        get() = "$prefix${get("$path.checkpointNotFound")}"
    val checkpointExists
        get() = "$prefix${get("$path.checkpointExists")}"
    val badBlock
        get() = "$prefix${get("$path.badBlock")}"
    val badLength
        get() = "$prefix${get("$path.badLength")}"
    val badWorld
        get() = "$prefix${get("$path.badWorld")}"
    val checkpointCreated
        get() = "$prefix${get("$path.checkpointCreated")}"
    val checkpointRemoved
        get() = "$prefix${get("$path.checkpointRemoved")}"
    val courseSaved
        get() = "$prefix${get("$path.courseSaved")}"

}
