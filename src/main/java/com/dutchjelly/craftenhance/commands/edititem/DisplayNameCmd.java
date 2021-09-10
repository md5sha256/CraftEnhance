package com.dutchjelly.craftenhance.commands.edititem;

import com.dutchjelly.craftenhance.commandhandling.CommandRoute;
import com.dutchjelly.craftenhance.commandhandling.CustomCmdHandler;
import com.dutchjelly.craftenhance.commandhandling.ICommand;
import com.dutchjelly.craftenhance.itemcreation.ItemCreator;
import com.dutchjelly.craftenhance.itemcreation.ParseResult;
import com.dutchjelly.craftenhance.messaging.Messenger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandRoute(cmdPath = "edititem.name", perms = "perms.item-editor")
public class DisplayNameCmd implements ICommand {

	private CustomCmdHandler handler;
	
	public DisplayNameCmd(CustomCmdHandler handler){
		this.handler = handler;
	}
	
	@Override
	public String getDescription() {
		return "With this command the displayname of the held item can be set. Here's an example of the usage: &7/edititem name example name";
	}

	@Override
	public void handlePlayerCommand(Player p, String[] args) {
		ItemCreator creator = new ItemCreator(p.getInventory().getItemInMainHand(), args);
		ParseResult result = creator.setDisplayName();
		p.getInventory().setItemInMainHand(creator.getItem());
		Messenger.Message(result.getMessage(), p);
	}

	@Override
	public void handleConsoleCommand(CommandSender sender, String[] args) {
		Messenger.MessageFromConfig("messages.commands.only-for-players", sender);
	}
}
