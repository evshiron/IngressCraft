package info.evshiron.ingresscraft.messages;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import info.evshiron.ingresscraft.IngressData;
import info.evshiron.ingresscraft.entities.EntityPortal;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by evshiron on 6/12/15.
 */
public class MessageCreatePortal implements IMessage, IMessageHandler<MessageCreatePortal, IMessage> {

    public String Name;
    public double X;
    public double Y;
    public double Z;

    public MessageCreatePortal() {}

    public MessageCreatePortal(EntityPlayer player, String name, double x, double y, double z) {

        Name = name;
        X = x;
        Y = y;
        Z = z;

    }

    @Override
    public void fromBytes(ByteBuf buf) {

        Name = ByteBufUtils.readUTF8String(buf);
        X = buf.readDouble();
        Y = buf.readDouble();
        Z = buf.readDouble();

    }

    @Override
    public void toBytes(ByteBuf buf) {

        ByteBufUtils.writeUTF8String(buf, Name);
        buf.writeDouble(X);
        buf.writeDouble(Y);
        buf.writeDouble(Z);

    }

    @Override
    public IMessage onMessage(MessageCreatePortal message, MessageContext ctx) {

        if(ctx.side.isServer()) {

            EntityPortal portal = new EntityPortal(ctx.getServerHandler().playerEntity.worldObj);

            portal.SetName(message.Name);
            portal.setPosition(message.X, message.Y, message.Z);

            ctx.getServerHandler().playerEntity.worldObj.spawnEntityInWorld(portal);

            IngressData.GetInstance(ctx.getServerHandler().playerEntity.worldObj).RegisterPortal(portal.UUID, portal.Name, portal.posX, portal.posY, portal.posZ);

        }

        return null;

    }

}
