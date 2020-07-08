package com.dumbdogdiner.yapp.commands

import com.dumbdogdiner.yapp.Base
import com.dumbdogdiner.yapp.courses.Course
import com.dumbdogdiner.yapp.editor.EditingSession
import com.dumbdogdiner.yapp.utils.Language
import com.dumbdogdiner.yapp.utils.SoundUtils
import com.dumbdogdiner.yapp.utils.Utils
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

class ParkourCommand : TabExecutor, Base {

    override fun onCommand(sender: CommandSender, command: Command, label: String, _args: Array<out String>): Boolean {
        if (_args.isEmpty()) {
            sender.sendMessage(Language.unknownSubCommand)
            if (sender is Player) {
                SoundUtils.error(sender)
            }
            return true
        }

        // fine vlad i'll do case insensitivity
        val args = _args.map { it.toLowerCase() }.toTypedArray()

        // Permissionless commands.
        when (args[0]) {
            "exit" -> { exit(sender); return true }
            "back" -> { back(sender); return true }
        }

        if (args[0] == "exit") {
            exit(sender)
            return true
        }

        if (!sender.hasPermission("parkour.command")) {
            sender.sendMessage(Language.noPermission)
            return true
        }

        when (args[0]) {
            "list" -> list(sender)
            "create" -> create(sender, args)
            "delete" -> delete(sender, args)
            else -> sender.sendMessage(Language.unknownSubCommand)
        }

        return true
    }

    /**
     * Allow players to teleport back to the previous checkpoint.
     */
    private fun back(sender: CommandSender) {
        if (sender !is Player) {
            sender.sendMessage(Language.noConsole)
            return
        }

        val session = sessionManager.getSession(sender)
        if (session != null) {
            session.revertToPreviousCheckpoint()
            return
        }

        // If no session punch the fox
        sender.sendMessage(Language.noSession)
        SoundUtils.error(sender)
    }

    /**
     * Allows the player to exit the current parkour session.
     */
    private fun exit(sender: CommandSender) {
        if (sender !is Player) {
            sender.sendMessage(Language.noConsole)
            return
        }

        val session = sessionManager.getSession(sender)
        if (session != null) {
            return sessionManager.endSession(session, returnToStart = false, didFinish = false)
        }

        val editingSession = editingSessionManager.getEditingSession(sender)
        if (editingSession != null) {
            return editingSessionManager.endEditingSession(editingSession, dropProgress = false)
        }

        // If no session punch the fox
        sender.sendMessage(Language.noSession)
        SoundUtils.error(sender)
    }

    /**
     * List the available courses.
     */
    private fun list(sender: CommandSender) {
        sender.sendMessage(Language.listCount.replace("%COUNT%", plugin.courseManager.getCourses().size.toString(), true))
        courseManager.getCourses()
            .map { it -> Utils.colorize(String.format(" &b- &e%s", it.name)) }
            .forEach { sender.sendMessage(it) }

        if (sender !is Player) {
            return
        }

        return SoundUtils.boop(sender)
    }

    /**
     * Create a new editing session and start making a course.
     */
    private fun create(sender: CommandSender, args: Array<out String>) {
        if (sender !is Player) {
            sender.sendMessage(Language.noConsole)
            return
        }

        // Require course name
        if (args.size < 2) {
            sender.sendMessage(Language.invalidCommandUsage.replace("%USAGE%", "/parkour create <name>", true))
            SoundUtils.error(sender)
            return
        }

        // Create a new course - don't add to course list yet!
        val course = Course()
        course.name = args[1]

        if (courseManager.getCourses().find { it.name == args[1] } != null) {
            sender.sendMessage(Language.courseExists.replace("%COURSE%", args[1], ignoreCase = true))
            return SoundUtils.error(sender)
        }

        editingSessionManager.createEditingSession(sender, course, EditingSession.Type.CREATE)
    }

    /**
     * Edit a course.
     */
    fun edit(sender: CommandSender, args: Array<out String>) {
        if (sender !is Player) {
            sender.sendMessage(Language.noConsole)
            return
        }

        if (args.size < 2) {
            sender.sendMessage(Language.invalidCommandUsage.replace("%USAGE%", "/parkour edit <name>", true))
            SoundUtils.error(sender)
            return
        }

        val course = courseManager.getCourses().find { it.name == args[1] }

        // If no course exists
        if (course == null) {
            sender.sendMessage(Language.courseNotFound.replace("%COURSE%", args[1], ignoreCase = true))
            return SoundUtils.error(sender)
        }

        editingSessionManager.createEditingSession(sender, course, EditingSession.Type.MODIFY)
    }

    /**
     * Delete a course.
     */
    private fun delete(sender: CommandSender, args: Array<out String>) {
        if (sender !is Player) {
            sender.sendMessage(Language.noConsole)
            return
        }

        if (args.size < 2) {
            sender.sendMessage(Language.invalidCommandUsage.replace("%USAGE%", "/parkour delete <name>", true))
            SoundUtils.error(sender)
            return
        }

        val course = courseManager.getCourses().find { it.name == args[1] }

        // If no course exists
        if (course == null) {
            sender.sendMessage(Language.courseNotFound.replace("%COURSE%", args[1], ignoreCase = true))
            return SoundUtils.error(sender)
        }

        courseManager.removeCourse(course)
        sender.sendMessage(Language.courseDeleted.replace("%COURSE%", course.name, ignoreCase = true))
        SoundUtils.boop(sender)
    }

    /**
     * Tab completer.
     */
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        if (!sender.hasPermission("parkour.command")) {
            return mutableListOf("exit", "back")
        }

        return when {
            args.size < 2 -> mutableListOf("exit", "back", "list", "create", "edit", "delete")
            args.size == 2 && args[0] == "list" -> courseManager.getCourses().map { it.name } as MutableList<String>
            args.size == 2 && args[0] == "edit" -> courseManager.getCourses().map { it.name } as MutableList<String>
            args.size == 2 && args[0] == "delete" -> courseManager.getCourses().map { it.name } as MutableList<String>
            else -> mutableListOf()
        }
    }
}
