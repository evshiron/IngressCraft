package info.evshiron.ingresscraft.entities;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import info.evshiron.ingresscraft.Constants;
import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.items.ItemScanner;
import info.evshiron.ingresscraft.items.ItemXMPBurster;
import info.evshiron.ingresscraft.messages.MessageHandler;
import info.evshiron.ingresscraft.messages.MessageSyncScanner;
import info.evshiron.ingresscraft.utils.IngressHelper;
import info.evshiron.ingresscraft.utils.IngressNotifier;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by evshiron on 5/31/15.
 */
public class EntityResonator extends EntityIngressBase implements IEntityAdditionalSpawnData {

    public static String NAME = "resonator";

    public int Level = 0;
    public int Faction = Constants.Faction.NEUTRAL;
    public String Owner = "NIA";

    public EntityPlayer AttackingAgent = null;

    public EntityResonator(World world) {

        super(world);

        setSize(0.2f, 0.2f);

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

        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(6000);

    }

    @Override
    public void onLivingUpdate() {

        if(getHealth() <= 0) {

            onDeath(new EntityDamageSource(IngressCraft.MODID + ":xmpBurster", AttackingAgent));

        }

        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(IngressHelper.GetResonatorMaxXM(Level));

        if(getHealth() > getMaxHealth()) {

            prevHealth = getMaxHealth();
            setHealth(prevHealth);

        }

    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {

        if(source.getDamageType().contentEquals(IngressCraft.MODID + ":xmpBurster")) {

            EntityPlayer player = (EntityPlayer) source.getEntity();

            ItemStack scanner = player.getCurrentArmor(3);

            if(scanner.getItem() instanceof ItemScanner && scanner.getTagCompound().getInteger("faction") != Faction) {

                AttackingAgent = player;

                ItemStack xmpBurster;

                if((xmpBurster = player.getCurrentEquippedItem()).getItem() instanceof ItemXMPBurster) {

                    double xmpBursterRange = IngressHelper.GetXMPBursterRange(((ItemXMPBurster) xmpBurster.getItem()).Level);

                    float newDamage = (float) IngressHelper.GetCalculatedDamage(xmpBursterRange, player.getDistanceToEntity(this), damage);

                    damageEntity(source, newDamage);

                    List<EntityPortal> portals = IngressHelper.GetEntitiesAround(worldObj, EntityPortal.class, this, IngressCraft.CONFIG_PORTAL_RANGE);

                    for(int i = 0; i < portals.size(); i++) {

                        EntityPortal portal = portals.get(i);

                        portal.attackEntityFrom(new EntityDamageSource(IngressCraft.MODID + ":xmpBurster", AttackingAgent), 0);

                    }

                }

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

        EntityPlayer player;
        ItemStack scanner;

        if(source.getEntity() instanceof EntityPlayer && (scanner = (player = (EntityPlayer) source.getEntity()).getCurrentArmor(3)).getItem() instanceof ItemScanner) {

            NBTTagCompound nbt = scanner.getTagCompound();

            if(!worldObj.isRemote) IngressNotifier.BroadcastDestroying(scanner);

            nbt.setInteger("ap", nbt.getInteger("ap") + 75);

            if(!worldObj.isRemote) MessageHandler.Wrapper.sendTo(new MessageSyncScanner(scanner), (EntityPlayerMP) player);

        }

        isDead = true;

    }


}
