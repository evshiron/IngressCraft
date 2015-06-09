package info.evshiron.ingresscraft.items;

import info.evshiron.ingresscraft.Constants;
import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.entities.PortalEntity;
import info.evshiron.ingresscraft.entities.ResonatorEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

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
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int targetSide, float targetX, float targetY, float targetZ) {

        if(world.isRemote) {

            return false;

        }

        ResonatorEntity entity = new ResonatorEntity(world);

        ItemStack scanner = player.getCurrentArmor(3);

        if(scanner == null || !(scanner.getItem() instanceof ScannerItem)) {

            String broadcast = String.format("Resonator can't be deployed without Scanner.");
            player.addChatMessage(new ChatComponentText(broadcast));

            return false;

        }

        NBTTagCompound nbt = scanner.getTagCompound();

        entity.SetFaction(nbt.getInteger("faction"));
        entity.SetOwner(nbt.getString("codename"));

        entity.setPosition(x + targetX, y + targetY, z + targetZ);

        List portals = world.getEntitiesWithinAABB(PortalEntity.class, entity.boundingBox.expand(4, 4, 4));

        if(portals.size() == 0) {

            String broadcast = String.format("Resonator can't be deployed far from Portal.");
            player.addChatMessage(new ChatComponentText(broadcast));

            return false;

        }
        else {

            for(int i = 0; i < portals.size(); i++) {

                PortalEntity portal = (PortalEntity) portals.get(i);

                if(player.getCurrentArmor(3).getTagCompound().getInteger("faction") != portal.mFaction && portal.mFaction != Constants.Faction.NEUTRAL) {

                    String broadcast = String.format("Resonator can't be deployed within opponent's Portal.");
                    player.addChatMessage(new ChatComponentText(broadcast));

                    return false;

                }

            }

            if(world.getEntitiesWithinAABB(ResonatorEntity.class, entity.boundingBox.expand(4, 4, 4)).size() >= 8) {

                String broadcast = String.format("Resonator can't be deployed on this Portal.");
                player.addChatMessage(new ChatComponentText(broadcast));

                return false;

            }

            NBTTagCompound nbt1 = player.getCurrentArmor(3).getTagCompound();

            ChatComponentText message = new ChatComponentText("");
            message.appendSibling(
                new ChatComponentText(nbt1.getString("codename"))
                .setChatStyle(
                    new ChatStyle()
                    .setColor(nbt1.getInteger("faction") == Constants.Faction.RESISTANCE ? EnumChatFormatting.BLUE : EnumChatFormatting.GREEN)
                )
            );
            message.appendSibling(new ChatComponentText(" has deployed a resonator."));
            Minecraft.getMinecraft().getIntegratedServer().getConfigurationManager().sendChatMsg(message);

            if(!player.capabilities.isCreativeMode) {

                player.inventory.consumeInventoryItem(itemStack.getItem());
                player.addExperience(2);

            }

            world.spawnEntityInWorld(entity);

            return true;

        }

    }

}
