package com.dumbdogdiner.yapp

import com.dumbdogdiner.yapp.courses.CourseManager
import com.dumbdogdiner.yapp.editor.EditingSessionManager
import com.dumbdogdiner.yapp.session.SessionManager

import java.util.logging.Logger

interface Base {
    val plugin: YappParkourPlugin
        get() = YappParkourPlugin.instance

    val logger: Logger
        get() = plugin.logger

    val courseManager: CourseManager
        get() = plugin.courseManager

    val sessionManager: SessionManager
        get() = plugin.sessionManager

    val editingSessionManager: EditingSessionManager
        get() = plugin.editingSessionManager
}
