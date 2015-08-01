package info.evshiron.ingresscraft.items;

import info.evshiron.ingresscraft.Constants;
import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.entities.EntityPortal;
import info.evshiron.ingresscraft.entities.EntityResonator;
import info.evshiron.ingresscraft.messages.MessageHandler;
import info.evshiron.ingresscraft.messages.MessageSyncScanner;
import info.evshiron.ingresscraft.utils.IngressHelper;
import info.evshiron.ingresscraft.utils.IngressNotifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by evshiron on 5/25/15.
 */
public class ItemResonator extends Item {

    public static final String NAME = "resonator";

    public int Level = 0;

    public ItemResonator(int level) {

        super();

        Level = level;

        setUnlocalizedName(NAME);
        //setCreativeTab(IngressCraft.CreativeTab);
        setTextureName(IngressCraft.MODID + ":l" + Level + NAME);

        setMaxDamage((int) IngressHelper.GetResonatorMaxXM(Level));

    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List lines, boolean p_77624_4_) {

        lines.add(String.format("Level: %d", Level));

    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int targetSide, float targetX, float targetY, float targetZ) {

        ItemStack scanner = player.getCurrentArmor(3);

        if(scanner == null || !(scanner.getItem() instanceof ItemScanner)) {

            if(world.isRemote) IngressNotifier.NotifyCantDeployWithoutScanner(player);

            return false;

        }

        NBTTagCompound nbt = scanner.getTagCompound();

        if(nbt.getInteger("level") < Level) {

            if(world.isRemote) IngressNotifier.NotifyCantDeployWithoutAccess(player);

            return false;

        }

        EntityResonator entity = new EntityResonator(world);

        entity.SetLevel(Level);
        entity.SetOwner(nbt.getString("codename"));
        entity.SetFaction(nbt.getInteger("faction"));

        entity.setPosition(x + targetX, y + targetY, z + targetZ);

        List<EntityPortal> portals = IngressHelper.GetEntitiesAround(world, EntityPortal.class, entity, IngressCraft.CONFIG_PORTAL_RANGE);

        if(portals.size() == 0) {

            if(world.isRemote) IngressNotifier.NotifyCantDeployFarFromPortal(player);

            return false;

        }
        else {

            for(int i = 0; i < portals.size(); i++) {

                EntityPortal portal = portals.get(i);

                if(nbt.getInteger("faction") != portal.Faction && portal.Faction != Constants.Faction.NEUTRAL) {

                    if(world.isRemote) IngressNotifier.NotifyCantDeployWithinOpponentPortal(player);

                    return false;

                }
                else if(IngressHelper.GetEntitiesAround(world, EntityResonator.class, portal, IngressCraft.CONFIG_PORTAL_RANGE).size() >= 8) {

                    if(world.isRemote) IngressNotifier.NotifyCantDeployOnThisPortal(player);

                    return false;

                }

                if(!world.isRemote) world.spawnEntityInWorld(entity);

                if(!world.isRemote) IngressNotifier.BroadcastDeploying(scanner);

                nbt.setInteger("ap", nbt.getInteger("ap") + 125);

                if(portal.Faction == Constants.Faction.NEUTRAL) {

                    portal.SetFaction(nbt.getInteger("faction"));
                    portal.SetOwner(nbt.getString("codename"));

                    if(!world.isRemote) IngressNotifier.BroadcastCapturing(scanner);

                    nbt.setInteger("ap", nbt.getInteger("ap") + 625);

                    if(!world.isRemote) MessageHandler.Wrapper.sendTo(new MessageSyncScanner(player, scanner), (EntityPlayerMP) player);

                    break;

                }

                if(!world.isRemote) MessageHandler.Wrapper.sendTo(new MessageSyncScanner(player, scanner), (EntityPlayerMP) player);

            }

            if(!player.capabilities.isCreativeMode) {

                player.inventory.consumeInventoryItem(itemStack.getItem());

            }

            return true;

        }

    }

}
