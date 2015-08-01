package info.evshiron.ingresscraft.messages;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import info.evshiron.ingresscraft.IngressData;
import info.evshiron.ingresscraft.items.ItemPortalKey;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by evshiron on 6/12/15.
 */
public class MessageGetPortalInfo implements IMessage, IMessageHandler<MessageGetPortalInfo, IMessage> {

    public String UUID;
    public String Name;
    public double X;
    public double Y;
    public double Z;

    public MessageGetPortalInfo() {}

    // Used by client request.
    public MessageGetPortalInfo(EntityPlayer player, String uuid) {

        this(player, uuid, "", 0, 0, 0);

    }

    // Used by server response.
    public MessageGetPortalInfo(EntityPlayer player, String uuid, String name, double x, double y, double z) {

        UUID = uuid;
        Name = name;
        X = x;
        Y = y;
        Z = z;

    }

    @Override
    public void fromBytes(ByteBuf buf) {

        UUID = ByteBufUtils.readUTF8String(buf);
        Name = ByteBufUtils.readUTF8String(buf);
        X = buf.readDouble();
        Y = buf.readDouble();
        Z = buf.readDouble();

    }

    @Override
    public void toBytes(ByteBuf buf) {

        ByteBufUtils.writeUTF8String(buf, UUID);
        ByteBufUtils.writeUTF8String(buf, Name);
        buf.writeDouble(X);
        buf.writeDouble(Y);
        buf.writeDouble(Z);

    }

    @Override
    public IMessage onMessage(MessageGetPortalInfo message, MessageContext ctx) {

        if(ctx.side.isServer()) {

            NBTTagCompound portal = IngressData.GetInstance(ctx.getServerHandler().playerEntity.worldObj).GetPortal(message.UUID);

            return new MessageGetPortalInfo(ctx.getServerHandler().playerEntity, portal.getString("uuid"), portal.getString("name"), portal.getDouble("x"), portal.getDouble("y"), portal.getDouble("z"));

        }
        else {

            ItemPortalKey.ResponsePortalInfo(message);

            return null;

        }

    }

}
