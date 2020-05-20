package com.dumbdogdiner.parkour.commands

import com.dumbdogdiner.parkour.ParkourPlugin
import com.dumbdogdiner.parkour.courses.Course
import com.dumbdogdiner.parkour.editor.EditingSession
import com.dumbdogdiner.parkour.utils.Language
import com.dumbdogdiner.parkour.utils.SoundUtils

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

class ParkourCommand : TabExecutor {
    private val plugin = ParkourPlugin.instance

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission("parkour.command")) {
            sender.sendMessage(Language.noPermission)
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage(Language.unknownSubCommand)
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

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        if (!sender.hasPermission("parkour.command")) {
            return mutableListOf()
        }

        return when {
            args.size < 2 -> mutableListOf("list", "edit", "create", "delete")
            args[0] == "edit" -> plugin.courseManager.getCourses().map { it.name } as MutableList<String>
            args[0] == "list" -> plugin.courseManager.getCourses().map { it.name } as MutableList<String>
            args[0] == "delete" -> plugin.courseManager.getCourses().map { it.name } as MutableList<String>
            else -> mutableListOf()
        }
    }

    /**
     * List the available courses.
     */
    private fun list(sender: CommandSender) {
        sender.sendMessage(Language.listCount.replace("%COUNT%", plugin.courseManager.getCourses().size.toString(), true))
        plugin.courseManager.getCourses()
            .map { it -> String.format(" &e- &r%s", it.name)}
            .forEach { sender.sendMessage(it)}
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

        plugin.editingSessionManager.createEditingSession(sender, course, EditingSession.Type.CREATE)
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

        // TODO: Delete course
        val course = plugin.courseManager.getCourses().find { it.name == args[1] }
            ?: return sender.sendMessage(Language.courseNotFound)

        plugin.courseManager.removeCourse(course)
    }
}
