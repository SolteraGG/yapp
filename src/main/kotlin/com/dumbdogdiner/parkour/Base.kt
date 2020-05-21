package com.dumbdogdiner.parkour

import com.dumbdogdiner.parkour.courses.CourseManager
import com.dumbdogdiner.parkour.editor.EditingSessionManager
import com.dumbdogdiner.parkour.session.SessionManager

import java.util.logging.Logger

interface Base {
    val plugin: ParkourPlugin
        get() = ParkourPlugin.instance

    val logger: Logger
        get() = plugin.logger

    val courseManager: CourseManager
        get() = plugin.courseManager

    val sessionManager: SessionManager
        get() = plugin.sessionManager

    val editingSessionManager: EditingSessionManager
        get() = plugin.editingSessionManager
}
