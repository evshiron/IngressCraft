package info.evshiron.ingresscraft;

import com.sun.corba.se.impl.orbutil.concurrent.Sync;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import info.evshiron.ingresscraft.blocks.XMBlock;
import info.evshiron.ingresscraft.client.gui.PortalGUI;
import info.evshiron.ingresscraft.entities.PortalEntity;
import info.evshiron.ingresscraft.entities.ResonatorEntity;
import info.evshiron.ingresscraft.items.ResonatorItem;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import info.evshiron.ingresscraft.items.ScannerItem;
import info.evshiron.ingresscraft.items.XMPBursterItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import scala.tools.nsc.backend.icode.Members;
import sun.nio.ch.Net;

import javax.swing.text.html.parser.Entity;
import java.io.File;

@Mod(modid = IngressCraft.MODID, version = IngressCraft.VERSION)
public class IngressCraft
{

    public static class LoginScannerMessage implements IMessage {

        public String Codename;

        public int Faction;

        public LoginScannerMessage() {}

        public LoginScannerMessage(String codename, int faction) {

            Codename = codename;
            Faction = faction;
        }

        @Override
        public void fromBytes(ByteBuf buf) {

            Codename = ByteBufUtils.readUTF8String(buf);
            Faction = Integer.parseInt(ByteBufUtils.readUTF8String(buf));

        }

        @Override
        public void toBytes(ByteBuf buf) {

            ByteBufUtils.writeUTF8String(buf, Codename);
            ByteBufUtils.writeUTF8String(buf, String.valueOf(Faction));

        }

    }

    public static class LoginScannerHandler implements IMessageHandler<LoginScannerMessage, IMessage> {

        @Override
        public IMessage onMessage(LoginScannerMessage message, MessageContext ctx) {

            if(ctx.side.isServer()) {

                ItemStack itemStack = ctx.getServerHandler().playerEntity.getCurrentArmor(3);

                if(itemStack != null) {

                    NBTTagCompound nbt = new NBTTagCompound();

                    nbt.setString("codename", message.Codename);
                    nbt.setInteger("faction", message.Faction);

                    itemStack.setTagCompound(nbt);

                }

            }

            return null;

        }

    }

    public static final String MODID = "ingresscraft";
    public static final String VERSION = "0.0.1";

    @Mod.Instance(MODID)
    public static IngressCraft Instance;

    public static final CreativeTabs CreativeTab = new CreativeTabs("ingress") {

        @Override
        public Item getTabIconItem() {

            return ResonatorItem;

        }

    };

    public static final ScannerItem ScannerItem = new ScannerItem();
    public static final ResonatorItem ResonatorItem = new ResonatorItem();
    public static final XMPBursterItem XMPBursterItem = new XMPBursterItem();

    @SidedProxy(clientSide = "info.evshiron.ingresscraft.ClientProxy", serverSide = "info.evshiron.ingresscraft.CommonProxy")
    public static CommonProxy Proxy;

    public static SimpleNetworkWrapper LoginScannerChannel = NetworkRegistry.INSTANCE.newSimpleChannel("LoginScanner");

    public Configuration Config;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        Config = new Configuration(event.getSuggestedConfigurationFile());

    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        GameRegistry.registerItem(ScannerItem, ScannerItem.NAME);
        GameRegistry.registerItem(ResonatorItem, ResonatorItem.NAME);
        GameRegistry.registerItem(XMPBursterItem, XMPBursterItem.NAME);

        GameRegistry.registerBlock(new XMBlock(), XMBlock.NAME);

        int portalEntityId = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(PortalEntity.class, PortalEntity.NAME, portalEntityId);
        EntityRegistry.registerModEntity(PortalEntity.class, PortalEntity.NAME, portalEntityId, Instance, 64, 1, true);
        EntityList.entityEggs.put(portalEntityId, new EntityList.EntityEggInfo(portalEntityId, 0, 0));

        int resonatorEntityId = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(ResonatorEntity.class, ResonatorEntity.NAME, resonatorEntityId);
        EntityRegistry.registerModEntity(ResonatorEntity.class, ResonatorEntity.NAME, resonatorEntityId, Instance, 64, 1, true);

        Proxy.RegisterRenderers();

        NetworkRegistry.INSTANCE.registerGuiHandler(Instance, Proxy);

        LoginScannerChannel.registerMessage(LoginScannerHandler.class, LoginScannerMessage.class, 0, Side.SERVER);

    }

}
