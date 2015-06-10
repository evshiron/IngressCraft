package info.evshiron.ingresscraft.items;

import info.evshiron.ingresscraft.Constants;
import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.entities.PortalEntity;
import info.evshiron.ingresscraft.entities.ResonatorEntity;
import info.evshiron.ingresscraft.utils.IngressHelper;
import info.evshiron.ingresscraft.utils.IngressNotifier;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import javax.sound.sampled.Port;
import java.util.List;

/**
 * Created by evshiron on 5/25/15.
 */
public class ResonatorItem extends Item {

    public static final String NAME = "resonator";

    public ResonatorItem() {

        super();

        setUnlocalizedName(NAME);
        setCreativeTab(IngressCraft.CreativeTab);
        setTextureName(IngressCraft.MODID + ":" + NAME);

    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List lines, boolean p_77624_4_) {

        if(!itemStack.hasTagCompound()) {

            return;

        }

        NBTTagCompound nbt = itemStack.getTagCompound();

        lines.add(String.format("L%d", nbt.getInteger("level")));

    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_) {

        if(!itemStack.hasTagCompound()) {

            NBTTagCompound nbt = new NBTTagCompound();

            nbt.setInteger("level", 1);

            itemStack.setTagCompound(nbt);

        }

    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int targetSide, float targetX, float targetY, float targetZ) {

        if(world.isRemote) {

            return false;

        }

        ItemStack scanner = player.getCurrentArmor(3);

        if(scanner == null || !(scanner.getItem() instanceof ScannerItem)) {

            String broadcast = String.format("Resonator can't be deployed without Scanner.");
            player.addChatMessage(new ChatComponentText(broadcast));

            return false;

        }

        NBTTagCompound nbt = scanner.getTagCompound();

        if(nbt.getInteger("level") < itemStack.getTagCompound().getInteger("level")) {

            String broadcast = String.format("You don't have the access to deploy this Resonator.");
            player.addChatMessage(new ChatComponentText(broadcast));

            return false;

        }

        ResonatorEntity entity = new ResonatorEntity(world);

        entity.SetLevel(itemStack.getTagCompound().getInteger("level"));
        entity.SetOwner(nbt.getString("codename"));
        entity.SetFaction(nbt.getInteger("faction"));

        entity.setPosition(x + targetX, y + targetY, z + targetZ);

        List<PortalEntity> portals = IngressHelper.GetEntitiesAround(world, PortalEntity.class, entity, IngressCraft.CONFIG_PORTAL_RANGE);

        if(portals.size() == 0) {

            String broadcast = String.format("Resonator can't be deployed far from Portal.");
            player.addChatMessage(new ChatComponentText(broadcast));

            return false;

        }
        else {

            for(int i = 0; i < portals.size(); i++) {

                PortalEntity portal = portals.get(i);

                if(nbt.getInteger("faction") != portal.Faction && portal.Faction != Constants.Faction.NEUTRAL) {

                    IngressNotifier.NotifyCantDeployWithinOpponentPortal(player);

                    return false;

                }
                else if(IngressHelper.GetEntitiesAround(world, ResonatorEntity.class, portal, IngressCraft.CONFIG_PORTAL_RANGE).size() >= 8) {

                    IngressNotifier.NotifyCantDeployOnThisPortal(player);

                    return false;

                }

            }

            IngressNotifier.BroadcastDeployingResonator(nbt);

            nbt.setInteger("ap", nbt.getInteger("ap") + 125);
            IngressCraft.SyncScannerChannel.sendTo(new IngressCraft.SyncScannerMessage(nbt), (EntityPlayerMP) player);

            if(!player.capabilities.isCreativeMode) {

                player.inventory.consumeInventoryItem(itemStack.getItem());

            }

            world.spawnEntityInWorld(entity);

            return true;

        }

    }

}
