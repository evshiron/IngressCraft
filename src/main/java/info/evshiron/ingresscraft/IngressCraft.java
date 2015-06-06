package info.evshiron.ingresscraft;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import info.evshiron.ingresscraft.blocks.XMBlock;
import info.evshiron.ingresscraft.entities.PortalEntity;
import info.evshiron.ingresscraft.entities.ResonatorEntity;
import info.evshiron.ingresscraft.items.ResonatorItem;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import info.evshiron.ingresscraft.items.ScannerItem;
import info.evshiron.ingresscraft.items.XMPBursterItem;
import net.minecraft.entity.EntityList;

@Mod(modid = IngressCraft.MODID, version = IngressCraft.VERSION)
public class IngressCraft
{
    public static final String MODID = "ingresscraft";
    public static final String VERSION = "0.0.1";

    @Mod.Instance(MODID)
    public static IngressCraft Instance;

    public static ScannerItem scanner;
    public static XMPBursterItem xmp;


    @SidedProxy(clientSide = "info.evshiron.ingresscraft.ClientProxy", serverSide = "info.evshiron.ingresscraft.CommonProxy")
    public static CommonProxy Proxy;

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        scanner = new ScannerItem();
        xmp = new XMPBursterItem();
        GameRegistry.registerItem(scanner, ScannerItem.NAME);
        GameRegistry.registerItem(new ResonatorItem(), ResonatorItem.NAME);
        GameRegistry.registerItem(xmp, XMPBursterItem.NAME);

        GameRegistry.registerBlock(new XMBlock(), XMBlock.NAME);

        int portalEntityId = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(PortalEntity.class, PortalEntity.NAME, portalEntityId);
        EntityRegistry.registerModEntity(PortalEntity.class, PortalEntity.NAME, portalEntityId, Instance, 64, 1, true);
        EntityList.entityEggs.put(portalEntityId, new EntityList.EntityEggInfo(portalEntityId, 0, 0));

        int resonatorEntityId = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(ResonatorEntity.class, ResonatorEntity.NAME, resonatorEntityId);
        EntityRegistry.registerModEntity(ResonatorEntity.class, ResonatorEntity.NAME, resonatorEntityId, Instance, 64, 1, true);
        EntityList.entityEggs.put(resonatorEntityId, new EntityList.EntityEggInfo(resonatorEntityId, 0, 0));

        Proxy.RegisterRenderers();

        NetworkRegistry.INSTANCE.registerGuiHandler(Instance, Proxy);

    }

}
