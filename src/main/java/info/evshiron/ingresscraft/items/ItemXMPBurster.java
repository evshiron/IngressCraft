package info.evshiron.ingresscraft.items;

import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.entities.EntityPortal;
import info.evshiron.ingresscraft.entities.EntityResonator;
import info.evshiron.ingresscraft.utils.IngressHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by evshiron on 5/25/15
 */
public class ItemXMPBurster extends Item {

    public static final String NAME = "xmpBurster";

    public int Level = 0;

    public ItemXMPBurster(int level){

        super();

        Level = level;

        setUnlocalizedName(NAME);
        //setCreativeTab(IngressCraft.CreativeTab);
        setTextureName(IngressCraft.MODID + ":l" + Level + NAME);

    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List lines, boolean p_77624_4_) {

        lines.add(String.format("Level: %d", Level));

    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {

        ItemStack scanner = player.getCurrentArmor(3);

        if(scanner == null || !(scanner.getItem() instanceof ItemScanner)) {

            String broadcast = String.format("XMP Burster can't be fired without Scanner.");
            player.addChatMessage(new ChatComponentText(broadcast));

            return itemStack;

        }

        NBTTagCompound nbt = scanner.getTagCompound();

        if(nbt.getInteger("level") < Level) {

            String broadcast = String.format("You don't have the access to fire this XMP Burster.");
            player.addChatMessage(new ChatComponentText(broadcast));

            return itemStack;

        }

        double range = IngressHelper.GetXMPBursterRange(Level);
        double damage = IngressHelper.GetXMPBursterDamage(Level);

        List<EntityResonator> resonators = IngressHelper.GetEntitiesAround(world, EntityResonator.class, player, range);

        for(int i = 0; i < resonators.size(); i++) {

            EntityResonator resonator = resonators.get(i);

            resonator.attackEntityFrom(new EntityDamageSource(IngressCraft.MODID + ":xmpBurster", player), (float) damage);

        }

        List<EntityPortal> portals = IngressHelper.GetEntitiesAround(world, EntityPortal.class, player, range);

        for(int i = 0; i < portals.size(); i++) {

            EntityPortal portal = portals.get(i);

            portal.attackEntityFrom(new EntityDamageSource(IngressCraft.MODID + ":xmpBurster", player), (float) damage);

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
