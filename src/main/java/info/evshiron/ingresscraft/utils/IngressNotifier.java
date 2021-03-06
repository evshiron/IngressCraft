package info.evshiron.ingresscraft.utils;

import info.evshiron.ingresscraft.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

/**
 * Created by evshiron on 6/10/15.
 */
public class IngressNotifier {

    public static void NotifyLevelUp(EntityPlayer player, int level) {

        String broadcast = String.format("NEW ACCESS LEVEL %d", level);
        player.addChatMessage(new ChatComponentText(broadcast));

    }

    public static void NotifyCantDeployWithoutScanner(EntityPlayer player) {

        String broadcast = String.format("Resonator can't be deployed without Scanner.");
        player.addChatMessage(new ChatComponentText(broadcast));

    }

    public static void NotifyCantDeployWithoutAccess(EntityPlayer player) {

        String broadcast = String.format("Resonator can't be deployed without access.");
        player.addChatMessage(new ChatComponentText(broadcast));

    }

    public static void NotifyCantDeployFarFromPortal(EntityPlayer player) {

        String broadcast = String.format("Resonator can't be deployed far from Portal.");
        player.addChatMessage(new ChatComponentText(broadcast));

    }

    public static void NotifyCantDeployWithinOpponentPortal(EntityPlayer player) {

        String broadcast = String.format("Resonator can't be deployed within opponent's Portal.");
        player.addChatMessage(new ChatComponentText(broadcast));

    }

    public static void NotifyCantDeployOnThisPortal(EntityPlayer player) {

        String broadcast = String.format("Resonator can't be deployed on this Portal.");
        player.addChatMessage(new ChatComponentText(broadcast));

    }

    public static void BroadcastJoining(ItemStack scanner) {

        NBTTagCompound nbt = scanner.getTagCompound();

        ChatComponentText message = new ChatComponentText("");
        message.appendSibling(
                new ChatComponentText(nbt.getString("codename"))
                        .setChatStyle(
                                new ChatStyle()
                                        .setColor(nbt.getInteger("faction") == Constants.Faction.RESISTANCE ? EnumChatFormatting.BLUE : EnumChatFormatting.GREEN)
                        )
        );
        message.appendSibling(new ChatComponentText(" has joined the "));
        message.appendSibling(
                new ChatComponentText(nbt.getInteger("faction") == Constants.Faction.RESISTANCE ? "Resistance" : "Enlightened")
                        .setChatStyle(
                                new ChatStyle()
                                        .setColor(nbt.getInteger("faction") == Constants.Faction.RESISTANCE ? EnumChatFormatting.BLUE : EnumChatFormatting.GREEN)
                        )
        );
        message.appendSibling(new ChatComponentText("."));
        MinecraftServer.getServer().getConfigurationManager().sendChatMsg(message);

    }

    public static void BroadcastDeploying(ItemStack scanner) {

        NBTTagCompound nbt = scanner.getTagCompound();

        ChatComponentText message = new ChatComponentText("");
        message.appendSibling(
                new ChatComponentText(nbt.getString("codename"))
                        .setChatStyle(
                                new ChatStyle()
                                        .setColor(nbt.getInteger("faction") == Constants.Faction.RESISTANCE ? EnumChatFormatting.BLUE : EnumChatFormatting.GREEN)
                        )
        );
        message.appendSibling(new ChatComponentText(" has deployed a Resonator."));
        MinecraftServer.getServer().getConfigurationManager().sendChatMsg(message);

    }

    public static void BroadcastCapturing(ItemStack scanner) {

        NBTTagCompound nbt = scanner.getTagCompound();

        ChatComponentText message = new ChatComponentText("");
        message.appendSibling(
                new ChatComponentText(nbt.getString("codename"))
                        .setChatStyle(
                                new ChatStyle()
                                        .setColor(nbt.getInteger("faction") == Constants.Faction.RESISTANCE ? EnumChatFormatting.BLUE : EnumChatFormatting.GREEN)
                        )
        );
        message.appendSibling(new ChatComponentText(" has captured a Portal."));
        MinecraftServer.getServer().getConfigurationManager().sendChatMsg(message);

    }

    public static void BroadcastDestroying(ItemStack scanner) {

        NBTTagCompound nbt = scanner.getTagCompound();

        ChatComponentText message = new ChatComponentText("");
        message.appendSibling(
                new ChatComponentText(nbt.getString("codename"))
                        .setChatStyle(
                                new ChatStyle()
                                        .setColor(nbt.getInteger("faction") == Constants.Faction.RESISTANCE ? EnumChatFormatting.BLUE : EnumChatFormatting.GREEN)
                        )
        );
        message.appendSibling(new ChatComponentText(" has destroyed a Resonator."));
        MinecraftServer.getServer().getConfigurationManager().sendChatMsg(message);

    }

    public static void BroadcastNeutralizing(ItemStack scanner) {

        NBTTagCompound nbt = scanner.getTagCompound();

        ChatComponentText message = new ChatComponentText("");
        message.appendSibling(
                new ChatComponentText(nbt.getString("codename"))
                        .setChatStyle(
                                new ChatStyle()
                                        .setColor(nbt.getInteger("faction") == Constants.Faction.RESISTANCE ? EnumChatFormatting.BLUE : EnumChatFormatting.GREEN)
                        )
        );
        message.appendSibling(new ChatComponentText(" has neutralized a Portal."));
        MinecraftServer.getServer().getConfigurationManager().sendChatMsg(message);

    }

}
