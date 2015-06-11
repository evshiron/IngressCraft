package info.evshiron.ingresscraft.messages;

import com.sun.corba.se.impl.orbutil.concurrent.Sync;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import info.evshiron.ingresscraft.entities.PortalEntity;
import io.netty.buffer.ByteBuf;

/**
 * Created by evshiron on 6/12/15.
 */
public class SyncPortalMessage implements IMessage {

    public static class Handler implements IMessageHandler<SyncPortalMessage, IMessage> {

        @Override
        public IMessage onMessage(SyncPortalMessage message, MessageContext ctx) {

            if(ctx.side.isServer()) {

                PortalEntity portal = (PortalEntity) ctx.getServerHandler().playerEntity.worldObj.getEntityByID(message.Id);

                portal.SetName(message.Name);
                portal.SetFaction(message.Faction);
                portal.SetOwner(message.Owner);

            }

            return null;

        }

    }

    public int Id;
    public String Name;
    public int Faction;
    public String Owner;

    public SyncPortalMessage() {}

    public SyncPortalMessage(PortalEntity portal) {

        Id = portal.getEntityId();
        Name = portal.Name;
        Faction = portal.Faction;
        Owner = portal.Owner;

    }

    @Override
    public void fromBytes(ByteBuf buf) {

        Id = Integer.parseInt(ByteBufUtils.readUTF8String(buf));
        Name = ByteBufUtils.readUTF8String(buf);
        Faction = Integer.parseInt(ByteBufUtils.readUTF8String(buf));
        Owner = ByteBufUtils.readUTF8String(buf);

    }

    @Override
    public void toBytes(ByteBuf buf) {

        ByteBufUtils.writeUTF8String(buf, String.valueOf(Id));
        ByteBufUtils.writeUTF8String(buf, Name);
        ByteBufUtils.writeUTF8String(buf, String.valueOf(Faction));
        ByteBufUtils.writeUTF8String(buf, Owner);

    }

}
