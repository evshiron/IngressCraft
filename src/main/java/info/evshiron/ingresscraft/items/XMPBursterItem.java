package info.evshiron.ingresscraft.items;

import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.entities.PortalEntity;
import info.evshiron.ingresscraft.entities.ResonatorEntity;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by evshiron on 5/25/15
 */
public class XMPBursterItem extends Item {

    public static final String NAME = "xmpBurster";

    public XMPBursterItem(){

        super();

        setUnlocalizedName(NAME);
        setCreativeTab(CreativeTabs.tabInventory);
        setTextureName(IngressCraft.MODID + ":" + NAME);

    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {

        ItemStack scanner = player.getCurrentArmor(3);

        if(scanner == null || !(scanner.getItem() instanceof ScannerItem)) {

            String broadcast = String.format("XMP Burster can't be fired without Scanner.");
            player.addChatMessage(new ChatComponentText(broadcast));

            return itemStack;

        }

        // Should be fetched as below when leveling available.
        float xmpBursterRange = 10;
        float xmpBursterDamage = 500;
        //float xmpBursterDamage = mDamage;

        List resonators = world.getEntitiesWithinAABB(ResonatorEntity.class, player.boundingBox.expand(xmpBursterRange, xmpBursterRange, xmpBursterRange));

        for(int i = 0; i < resonators.size(); i++) {

            ResonatorEntity resonator = (ResonatorEntity) resonators.get(i);

            resonator.attackEntityFrom(new EntityDamageSource(IngressCraft.MODID + ":xmpBurster", player), xmpBursterDamage);

        }

        List portals = world.getEntitiesWithinAABB(PortalEntity.class, player.boundingBox.expand(xmpBursterRange, xmpBursterRange, xmpBursterRange));

        for(int i = 0; i < portals.size(); i++) {

            PortalEntity portal = (PortalEntity) portals.get(i);

            portal.attackEntityFrom(new EntityDamageSource(IngressCraft.MODID + ":xmpBurster", player), xmpBursterDamage);

        }

        world.spawnParticle("largeexplode", player.posX, player.posY, player.posZ, 1.0D, 0.0D, 0.0D);
        world.spawnParticle("hugeexplosion", player.posX, player.posY, player.posZ, 1.0D, 0.0D, 0.0D);

        //OExplosion explosion = new OExplosion(world, player, player.posX, player.posY, player.posZ, 10.0F);
        //explosion.doExplosionA();
        //explosion.doExplosionB(true);

        player.inventory.consumeInventoryItem(player.getCurrentEquippedItem().getItem());

        return itemStack;

    }

}
