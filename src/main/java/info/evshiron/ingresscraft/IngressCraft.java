package info.evshiron.ingresscraft;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import info.evshiron.ingresscraft.blocks.XMBlock;
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

@Mod(modid = IngressCraft.MODID, version = IngressCraft.VERSION)
public class IngressCraft
{

    public static class SyncScannerMessage implements IMessage {

        public String Codename;
        public int Faction;
        public int Level;
        public int AP;
        public int XM;

        public SyncScannerMessage() {}

        public SyncScannerMessage(NBTTagCompound nbt) {

            Codename = nbt.getString("codename");
            Faction = nbt.getInteger("faction");
            Level = nbt.getInteger("level");
            AP = nbt.getInteger("ap");
            XM = nbt.getInteger("xm");

        }

        @Override
        public void fromBytes(ByteBuf buf) {

            Codename = ByteBufUtils.readUTF8String(buf);
            Faction = Integer.parseInt(ByteBufUtils.readUTF8String(buf));
            Level = Integer.parseInt(ByteBufUtils.readUTF8String(buf));
            AP = Integer.parseInt(ByteBufUtils.readUTF8String(buf));
            XM = Integer.parseInt(ByteBufUtils.readUTF8String(buf));

        }

        @Override
        public void toBytes(ByteBuf buf) {

            ByteBufUtils.writeUTF8String(buf, Codename);
            ByteBufUtils.writeUTF8String(buf, String.valueOf(Faction));
            ByteBufUtils.writeUTF8String(buf, String.valueOf(Level));
            ByteBufUtils.writeUTF8String(buf, String.valueOf(AP));
            ByteBufUtils.writeUTF8String(buf, String.valueOf(XM));

        }

    }

    public static class SyncScannerHandler implements IMessageHandler<SyncScannerMessage, IMessage> {

        @Override
        public IMessage onMessage(SyncScannerMessage message, MessageContext ctx) {

            if(ctx.side.isServer()) {

                ItemStack itemStack = ctx.getServerHandler().playerEntity.getCurrentArmor(3);

                if(itemStack != null && itemStack.getItem() instanceof ScannerItem) {

                    NBTTagCompound nbt;

                    if(!itemStack.hasTagCompound()) {

                        nbt = new NBTTagCompound();

                    }
                    else {

                        nbt = itemStack.getTagCompound();

                    }

                    nbt.setString("codename", message.Codename);
                    nbt.setInteger("faction", message.Faction);
                    nbt.setInteger("level", message.Level);
                    nbt.setInteger("ap", message.AP);
                    nbt.setInteger("xm", message.XM);

                    itemStack.setTagCompound(nbt);

                }

            }
            else {

                ItemStack itemStack = Minecraft.getMinecraft().thePlayer.getCurrentArmor(3);

                if(itemStack != null && itemStack.getItem() instanceof ScannerItem) {

                    NBTTagCompound nbt;

                    if(!itemStack.hasTagCompound()) {

                        nbt = new NBTTagCompound();

                    }
                    else {

                        nbt = itemStack.getTagCompound();

                    }

                    nbt.setString("codename", message.Codename);
                    nbt.setInteger("faction", message.Faction);
                    nbt.setInteger("level", message.Level);
                    nbt.setInteger("ap", message.AP);
                    nbt.setInteger("xm", message.XM);

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

    public static double CONFIG_PORTAL_RANGE;

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

    public static SimpleNetworkWrapper SyncScannerChannel = NetworkRegistry.INSTANCE.newSimpleChannel("SyncScanner");

    public Configuration Config;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        Config = new Configuration(event.getSuggestedConfigurationFile());

        CONFIG_PORTAL_RANGE = Config.get("general", "PortalRange", 4.0).getDouble();

        Config.save();

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

        SyncScannerChannel.registerMessage(SyncScannerHandler.class, SyncScannerMessage.class, 0, Side.SERVER);

    }

}
