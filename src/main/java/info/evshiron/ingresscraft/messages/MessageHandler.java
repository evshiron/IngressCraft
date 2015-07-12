package info.evshiron.ingresscraft.messages;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import info.evshiron.ingresscraft.IngressCraft;

/**
 * Created by evshiron on 7/17/15.
 */
public class MessageHandler {

    public static final SimpleNetworkWrapper Wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(IngressCraft.MODID);

    public static void Setup() {

        int index = 0;

        Wrapper.registerMessage(MessageSyncScanner.class, MessageSyncScanner.class, index++, Side.SERVER);
        Wrapper.registerMessage(MessageSyncPortal.class, MessageSyncPortal.class, index++, Side.SERVER);

    }

}
