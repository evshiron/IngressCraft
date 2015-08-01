package info.evshiron.ingresscraft.messages;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import info.evshiron.ingresscraft.IngressData;
import info.evshiron.ingresscraft.client.gui.GUIPortal;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by evshiron on 6/12/15.
 */
public class MessageGetPortalLinkability implements IMessage, IMessageHandler<MessageGetPortalLinkability, IMessage> {

    public String FromUUID;
    public String ToUUID;
    public String Name;
    public boolean Linkability;

    public MessageGetPortalLinkability() {}

    // Used by client request.
    public MessageGetPortalLinkability(EntityPlayer player, String fromUUID, String toUUID) {

        this(player, fromUUID, toUUID, "", false);

    }

    // Used by server response.
    public MessageGetPortalLinkability(EntityPlayer player, String fromUUID, String toUUID, String name, boolean linkability) {

        FromUUID = fromUUID;
        ToUUID = toUUID;
        Name = name;
        Linkability = linkability;

    }

    @Override
    public void fromBytes(ByteBuf buf) {

        FromUUID = ByteBufUtils.readUTF8String(buf);
        ToUUID = ByteBufUtils.readUTF8String(buf);
        Name = ByteBufUtils.readUTF8String(buf);
        Linkability = buf.readBoolean();

    }

    @Override
    public void toBytes(ByteBuf buf) {

        ByteBufUtils.writeUTF8String(buf, FromUUID);
        ByteBufUtils.writeUTF8String(buf, ToUUID);
        ByteBufUtils.writeUTF8String(buf, Name);
        buf.writeBoolean(Linkability);

    }

    @Override
    public IMessage onMessage(MessageGetPortalLinkability message, MessageContext ctx) {

        if(ctx.side.isServer()) {

            IngressData ingressData = IngressData.GetInstance(ctx.getServerHandler().playerEntity.worldObj);

            String name = ingressData.GetPortal(message.ToUUID).getString("name");
            boolean linkability = ingressData.GetPortalLinkability(message.FromUUID, message.ToUUID);

            return new MessageGetPortalLinkability(ctx.getServerHandler().playerEntity, message.FromUUID, message.ToUUID, name, linkability);

        }
        else {

            GUIPortal.ResponsePortalLinkability(message);

            return null;

        }

    }

}
