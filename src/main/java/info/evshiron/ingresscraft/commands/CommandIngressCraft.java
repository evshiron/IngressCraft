package info.evshiron.ingresscraft.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;

/**
 * Created by evshiron on 8/20/15.
 */
public class CommandIngressCraft extends CommandBase {

    @Override
    public String getCommandName() {
        return "ingress";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "/ingress <action>[ arguments]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        if(args.length == 0) {

            sender.addChatMessage(new ChatComponentTranslation("Invalid arguments, try /ingress help.", new Object[0]));
            return;

        }

    }

}
