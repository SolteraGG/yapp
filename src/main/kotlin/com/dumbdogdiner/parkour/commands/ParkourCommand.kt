package com.dumbdogdiner.parkour.commands

import com.dumbdogdiner.parkour.ParkourPlugin
import com.dumbdogdiner.parkour.courses.Course
import com.dumbdogdiner.parkour.editor.EditingSession
import com.dumbdogdiner.parkour.utils.Language
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
            "create" -> create(sender)
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
            args[1] == "edit" -> MutableList(plugin.courseManager.getCourses().size) { i -> (i + 1).toString() }
            args[1] == "list" -> MutableList(plugin.courseManager.getCourses().size) { i -> (i + 1).toString() }
            args[1] == "delete" -> MutableList(plugin.courseManager.getCourses().size) { i -> (i + 1).toString() }
            else -> mutableListOf()
    }
    }

    private fun list(sender: CommandSender) {
        sender.sendMessage(Language.listCount.replace("%COUNT%", plugin.courseManager.getCourses().size.toString(), true))
        // var messages = plugin.courseManager.getCourses().map { it -> String.format("")}
    }

    private fun create(sender: CommandSender) {
        if (sender !is Player) {
            sender.sendMessage(Language.noConsole)
            return
        }

        // Create a new course - don't add to course list yet!
        val course = Course()

        plugin.editingSessionManager.createEditingSession(sender, course, EditingSession.Type.CREATE)
    }

    private fun delete(sender: CommandSender, args: Array<out String>) {
        if (sender !is Player) {
            sender.sendMessage(Language.noConsole)
            return
        }

        if (args.size < 2 || args[1].toIntOrNull() == null) {
            return sender.sendMessage(Language.invalidCommandUsage.replace("%USAGE%", "/parkour delete <id>", true))
        }

        // TODO: Delete course
        val course = plugin.courseManager.getCourses()[args[1].toInt()] as Course?
            ?: return sender.sendMessage(Language.courseNotFound)

        plugin.courseManager.removeCourse(course)
    }
}
