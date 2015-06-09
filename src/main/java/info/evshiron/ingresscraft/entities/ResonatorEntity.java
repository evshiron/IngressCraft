package info.evshiron.ingresscraft.entities;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import info.evshiron.ingresscraft.Constants;
import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.client.gui.ScannerGUI;
import info.evshiron.ingresscraft.items.ScannerItem;
import info.evshiron.ingresscraft.items.XMPBursterItem;
import info.evshiron.ingresscraft.utils.IngressHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import javax.sound.sampled.Port;
import java.util.List;

/**
 * Created by evshiron on 5/31/15.
 */
public class ResonatorEntity extends IngressEntityBase implements IEntityAdditionalSpawnData {

    public static String NAME = "resonator";

    public int Level = 0;
    public int Faction = Constants.Faction.NEUTRAL;
    public String Owner = "NIA";

    public ResonatorEntity(World world) {

        super(world);

    }

    public void SetLevel(int level) { Level = level; }

    public void SetFaction(int faction) {
        Faction = faction;
    }

    public void SetOwner(String owner) {
        Owner = owner;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {

        nbt.setInteger("level", Level);
        nbt.setInteger("faction", Faction);
        nbt.setString("owner", Owner);

        super.writeEntityToNBT(nbt);

    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {

        super.readEntityFromNBT(nbt);

        Level = nbt.getInteger("level");
        Faction = nbt.getInteger("faction");
        Owner = nbt.getString("owner");

    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {

        ByteBufUtils.writeUTF8String(buffer, String.valueOf(Level));
        ByteBufUtils.writeUTF8String(buffer, String.valueOf(Faction));
        ByteBufUtils.writeUTF8String(buffer, Owner);

    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {

        Level = Integer.parseInt(ByteBufUtils.readUTF8String(additionalData));
        Faction = Integer.parseInt(ByteBufUtils.readUTF8String(additionalData));
        Owner = ByteBufUtils.readUTF8String(additionalData);

    }

    @Override
    protected void applyEntityAttributes() {

        super.applyEntityAttributes();

        // FIXME: Leveling.
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(1000);

    }

    @Override
    public void onLivingUpdate() {

        List<PortalEntity> portals = IngressHelper.GetEntitiesAround(worldObj, PortalEntity.class, this, IngressCraft.CONFIG_PORTAL_RANGE);

        for(int i = 0; i < portals.size(); i++) {

            PortalEntity portal = portals.get(i);

            if(portal.mFaction == Constants.Faction.NEUTRAL) {

                portal.SetFaction(Faction);
                portal.SetOwner(Owner);

                //break;

            }

        }

        if(getHealth() <= 0) {

            onDeath(new EntityDamageSource(IngressCraft.MODID + ":xmpBurster", attackingPlayer));

        }

    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {

        if(worldObj.isRemote) {

            return false;

        }

        entityAge = 0;

        if(source.getDamageType().contentEquals(IngressCraft.MODID + ":xmpBurster")) {

            EntityPlayer player = (EntityPlayer) source.getEntity();

            ItemStack scanner = player.getCurrentArmor(3);

            if(scanner.getItem() instanceof ScannerItem && scanner.getTagCompound().getInteger("faction") != Faction) {

                attackingPlayer = player;

                float xmpBursterRange = 10;
                // Should be fetched as below when leveling available.
                /*
                if(player.getCurrentEquippedItem().getItem() instanceof XMPBursterItem) {
                    float xmpBursterRange = XMPBursterItem.GetMaxRangeFromLevel(player.getCurrentEquippedItem().getTagCompound().getInteger("range"));
                }
                */

                float newDamage = (float) (xmpBursterRange - IngressHelper.GetDistanceBetween(player, this)) / xmpBursterRange * damage;

                damageEntity(source, newDamage);

            }

        }

        return true;

    }

    @Override
    protected void damageEntity(DamageSource source, float damage) {

        prevHealth = getHealth();

        setHealth(prevHealth - damage);

        if(getHealth() <= 0) {

            onDeath(source);

        }

    }

    @Override
    public void onDeath(DamageSource source) {

        ItemStack scanner;

        if(source.getEntity() instanceof EntityPlayer && (scanner = ((EntityPlayer) source.getEntity()).getCurrentArmor(3)).getItem() instanceof ScannerItem) {

            NBTTagCompound nbt = scanner.getTagCompound();

            ChatComponentText message = new ChatComponentText("");
            message.appendSibling(
                new ChatComponentText(nbt.getString("codename"))
                .setChatStyle(
                    new ChatStyle()
                    .setColor(nbt.getInteger("faction") == Constants.Faction.RESISTANCE ? EnumChatFormatting.BLUE : EnumChatFormatting.GREEN)
                )
            );
            message.appendSibling(new ChatComponentText(" has destroyed a Resonator."));
            Minecraft.getMinecraft().getIntegratedServer().getConfigurationManager().sendChatMsg(message);

            nbt.setInteger("ap", nbt.getInteger("ap") + 75);

        }

        isDead = true;

    }


}
