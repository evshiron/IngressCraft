package info.evshiron.ingresscraft.utils;

import info.evshiron.ingresscraft.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

/**
 * Created by evshiron on 6/10/15.
 */
public class IngressNotifier {

    public static void NotifyCantDeployWithinOpponentPortal(EntityPlayer player) {

        String broadcast = String.format("Resonator can't be deployed within opponent's Portal.");
        player.addChatMessage(new ChatComponentText(broadcast));

    }

    public static void NotifyCantDeployOnThisPortal(EntityPlayer player) {

        String broadcast = String.format("Resonator can't be deployed on this Portal.");
        player.addChatMessage(new ChatComponentText(broadcast));

    }

    public static void BroadcastDeployingResonator(NBTTagCompound nbt) {

        ChatComponentText message = new ChatComponentText("");
        message.appendSibling(
                new ChatComponentText(nbt.getString("codename"))
                        .setChatStyle(
                                new ChatStyle()
                                        .setColor(nbt.getInteger("faction") == Constants.Faction.RESISTANCE ? EnumChatFormatting.BLUE : EnumChatFormatting.GREEN)
                        )
        );
        message.appendSibling(new ChatComponentText(" has deployed a Resonator."));
        Minecraft.getMinecraft().getIntegratedServer().getConfigurationManager().sendChatMsg(message);

    }

    public static void BroadcastCapturing(int faction, String codename) {

        ChatComponentText message = new ChatComponentText("");
        message.appendSibling(
                new ChatComponentText(codename)
                        .setChatStyle(
                                new ChatStyle()
                                        .setColor(faction == Constants.Faction.RESISTANCE ? EnumChatFormatting.BLUE : EnumChatFormatting.GREEN)
                        )
        );
        message.appendSibling(new ChatComponentText(" has captured a Portal."));
        Minecraft.getMinecraft().getIntegratedServer().getConfigurationManager().sendChatMsg(message);

    }

    public static void BroadcastDestroying(NBTTagCompound nbt) {

        ChatComponentText message = new ChatComponentText("");
        message.appendSibling(
                new ChatComponentText(nbt.getString("codename"))
                        .setChatStyle(
                                new ChatStyle()
                                        .setColor(nbt.getInteger("faction") == Constants.Faction.RESISTANCE ? EnumChatFormatting.BLUE : EnumChatFormatting.GREEN)
                        )
        );
        message.appendSibling(new ChatComponentText(" has destroyed a Resonator."));
        Minecraft.getMinecraft().getIntegratedServer().getConfigurationManager().sendChatMsg(message);

    }

    public static void BroadcastNeutralizing(NBTTagCompound nbt) {

        ChatComponentText message = new ChatComponentText("");
        message.appendSibling(
                new ChatComponentText(nbt.getString("codename"))
                        .setChatStyle(
                                new ChatStyle()
                                        .setColor(nbt.getInteger("faction") == Constants.Faction.RESISTANCE ? EnumChatFormatting.BLUE : EnumChatFormatting.GREEN)
                        )
        );
        message.appendSibling(new ChatComponentText(" has neutralized a Portal."));
        Minecraft.getMinecraft().getIntegratedServer().getConfigurationManager().sendChatMsg(message);

    }

}
