package info.evshiron.ingresscraft;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import info.evshiron.ingresscraft.blocks.BlockXM;
import info.evshiron.ingresscraft.commands.CommandIngressCraft;
import info.evshiron.ingresscraft.entities.EntityPortal;
import info.evshiron.ingresscraft.entities.EntityResonator;
import info.evshiron.ingresscraft.items.*;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import info.evshiron.ingresscraft.messages.MessageHandler;
import info.evshiron.ingresscraft.messages.MessageSyncScanner;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = IngressCraft.MODID, version = IngressCraft.VERSION)
public class IngressCraft
{

    public static final String MODID = "ingresscraft";
    public static final String VERSION = "0.0.1";

    @Mod.Instance(MODID)
    public static IngressCraft Instance;

    public static double CONFIG_RANGE_FACTOR;
    public static double CONFIG_PORTAL_RANGE;

    public static final CreativeTabs CreativeTab = new CreativeTabs("ingress") {

        @Override
        public Item getTabIconItem() {

            return L8ResonatorItem;

        }

    };

    public static final ItemScanner ScannerItem = new ItemScanner();

    public static final ItemPortal PortalItem = new ItemPortal();
    public static final ItemPortalKey PortalKeyItem = new ItemPortalKey();

    public static final ItemResonator L1ResonatorItem = new ItemResonator(1);
    public static final ItemResonator L2ResonatorItem = new ItemResonator(2);
    public static final ItemResonator L3ResonatorItem = new ItemResonator(3);
    public static final ItemResonator L4ResonatorItem = new ItemResonator(4);
    public static final ItemResonator L5ResonatorItem = new ItemResonator(5);
    public static final ItemResonator L6ResonatorItem = new ItemResonator(6);
    public static final ItemResonator L7ResonatorItem = new ItemResonator(7);
    public static final ItemResonator L8ResonatorItem = new ItemResonator(8);
    public static final ItemXMPBurster L1XMPBursterItem = new ItemXMPBurster(1);
    public static final ItemXMPBurster L2XMPBursterItem = new ItemXMPBurster(2);
    public static final ItemXMPBurster L3XMPBursterItem = new ItemXMPBurster(3);
    public static final ItemXMPBurster L4XMPBursterItem = new ItemXMPBurster(4);
    public static final ItemXMPBurster L5XMPBursterItem = new ItemXMPBurster(5);
    public static final ItemXMPBurster L6XMPBursterItem = new ItemXMPBurster(6);
    public static final ItemXMPBurster L7XMPBursterItem = new ItemXMPBurster(7);
    public static final ItemXMPBurster L8XMPBursterItem = new ItemXMPBurster(8);

    @SidedProxy(clientSide = "info.evshiron.ingresscraft.client.ClientProxy", serverSide = "info.evshiron.ingresscraft.CommonProxy")
    public static CommonProxy Proxy;

    public Configuration Config;

    public static ItemResonator GetResonatorItem(int level) {

        switch(level) {
            case 1:
                return L1ResonatorItem;
            case 2:
                return L2ResonatorItem;
            case 3:
                return L3ResonatorItem;
            case 4:
                return L4ResonatorItem;
            case 5:
                return L5ResonatorItem;
            case 6:
                return L6ResonatorItem;
            case 7:
                return L7ResonatorItem;
            case 8:
                return L8ResonatorItem;
            default:
                return null;
        }

    }

    public static ItemXMPBurster GetXMPBursterItem(int level) {

        switch(level) {
            case 1:
                return L1XMPBursterItem;
            case 2:
                return L2XMPBursterItem;
            case 3:
                return L3XMPBursterItem;
            case 4:
                return L4XMPBursterItem;
            case 5:
                return L5XMPBursterItem;
            case 6:
                return L6XMPBursterItem;
            case 7:
                return L7XMPBursterItem;
            case 8:
                return L8XMPBursterItem;
            default:
                return null;
        }

    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        Config = new Configuration(event.getSuggestedConfigurationFile());

        CONFIG_RANGE_FACTOR = Config.get("general", "RangeFactor", 0.1).getDouble();
        CONFIG_PORTAL_RANGE = 40.0 * CONFIG_RANGE_FACTOR;

        Config.save();

    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {

        GameRegistry.registerItem(ScannerItem.setCreativeTab(CreativeTab), ScannerItem.NAME);
        GameRegistry.registerItem(PortalItem.setCreativeTab(CreativeTab), PortalItem.NAME);
        GameRegistry.registerItem(PortalKeyItem.setCreativeTab(CreativeTab), PortalKeyItem.NAME);
        GameRegistry.registerItem(L1ResonatorItem.setCreativeTab(CreativeTab), "l1" + ItemResonator.NAME);
        GameRegistry.registerItem(L2ResonatorItem.setCreativeTab(CreativeTab), "l2" + ItemResonator.NAME);
        GameRegistry.registerItem(L3ResonatorItem.setCreativeTab(CreativeTab), "l3" + ItemResonator.NAME);
        GameRegistry.registerItem(L4ResonatorItem.setCreativeTab(CreativeTab), "l4" + ItemResonator.NAME);
        GameRegistry.registerItem(L5ResonatorItem.setCreativeTab(CreativeTab), "l5" + ItemResonator.NAME);
        GameRegistry.registerItem(L6ResonatorItem.setCreativeTab(CreativeTab), "l6" + ItemResonator.NAME);
        GameRegistry.registerItem(L7ResonatorItem.setCreativeTab(CreativeTab), "l7" + ItemResonator.NAME);
        GameRegistry.registerItem(L8ResonatorItem.setCreativeTab(CreativeTab), "l8" + ItemResonator.NAME);
        GameRegistry.registerItem(L1XMPBursterItem.setCreativeTab(CreativeTab), "l1" + ItemXMPBurster.NAME);
        GameRegistry.registerItem(L2XMPBursterItem.setCreativeTab(CreativeTab), "l2" + ItemXMPBurster.NAME);
        GameRegistry.registerItem(L3XMPBursterItem.setCreativeTab(CreativeTab), "l3" + ItemXMPBurster.NAME);
        GameRegistry.registerItem(L4XMPBursterItem.setCreativeTab(CreativeTab), "l4" + ItemXMPBurster.NAME);
        GameRegistry.registerItem(L5XMPBursterItem.setCreativeTab(CreativeTab), "l5" + ItemXMPBurster.NAME);
        GameRegistry.registerItem(L6XMPBursterItem.setCreativeTab(CreativeTab), "l6" + ItemXMPBurster.NAME);
        GameRegistry.registerItem(L7XMPBursterItem.setCreativeTab(CreativeTab), "l7" + ItemXMPBurster.NAME);
        GameRegistry.registerItem(L8XMPBursterItem.setCreativeTab(CreativeTab), "l8" + ItemXMPBurster.NAME);

        GameRegistry.registerBlock(new BlockXM(), BlockXM.NAME);

        int portalEntityId = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(EntityPortal.class, EntityPortal.NAME, portalEntityId);
        EntityRegistry.registerModEntity(EntityPortal.class, EntityPortal.NAME, portalEntityId, Instance, 64, 1, true);

        int resonatorEntityId = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(EntityResonator.class, EntityResonator.NAME, resonatorEntityId);
        EntityRegistry.registerModEntity(EntityResonator.class, EntityResonator.NAME, resonatorEntityId, Instance, 64, 1, true);

        Proxy.RegisterRenderers();

        NetworkRegistry.INSTANCE.registerGuiHandler(Instance, Proxy);

        MessageHandler.Setup();

    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event) {

        event.registerServerCommand(new CommandIngressCraft());

    }

}
