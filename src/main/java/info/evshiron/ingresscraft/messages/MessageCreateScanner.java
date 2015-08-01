package info.evshiron.ingresscraft.messages;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import ibxm.Player;
import info.evshiron.ingresscraft.Constants;
import info.evshiron.ingresscraft.IngressData;
import info.evshiron.ingresscraft.items.ItemScanner;
import info.evshiron.ingresscraft.utils.IngressNotifier;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.world.NoteBlockEvent;

/**
 * Created by evshiron on 6/12/15.
 */
public class MessageCreateScanner implements IMessage, IMessageHandler<MessageCreateScanner, IMessage> {

    public String Codename;
    public int Faction;

    public MessageCreateScanner() {}

    public MessageCreateScanner(EntityPlayer player, String codename, int faction) {

        Codename = codename;
        Faction = faction;

    }

    @Override
    public void fromBytes(ByteBuf buf) {

        Codename = ByteBufUtils.readUTF8String(buf);
        Faction = buf.readInt();

    }

    @Override
    public void toBytes(ByteBuf buf) {

        ByteBufUtils.writeUTF8String(buf, Codename);
        buf.writeInt(Faction);

    }

    @Override
    public IMessage onMessage(MessageCreateScanner message, MessageContext ctx) {

        if(ctx.side.isServer()) {

            ItemStack itemStack = ctx.getServerHandler().playerEntity.getCurrentArmor(3);

            if(itemStack != null && itemStack.getItem() instanceof ItemScanner) {

                NBTTagCompound nbt = itemStack.getTagCompound();

                nbt.setString("codename", message.Codename);
                nbt.setInteger("faction", message.Faction);
                nbt.setInteger("level", 1);
                nbt.setInteger("ap", 0);
                nbt.setInteger("xm", 1000);

                itemStack.setTagCompound(nbt);

                IngressData.GetInstance(ctx.getServerHandler().playerEntity.worldObj).RegisterAgent(nbt.getString("codename"), nbt.getInteger("faction"));

                IngressNotifier.BroadcastJoining(itemStack);

                return new MessageSyncScanner(ctx.getServerHandler().playerEntity, itemStack);

            }

        }

        return null;

    }

}

