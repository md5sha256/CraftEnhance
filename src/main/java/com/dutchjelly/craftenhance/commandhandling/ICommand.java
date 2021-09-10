package com.dutchjelly.craftenhance.commandhandling;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface ICommand {
	
	String getDescription();
	
	void handlePlayerCommand(Player p, String[] args);
	
	void handleConsoleCommand(CommandSender sender, String[] args);
	
}
