package info.evshiron.ingresscraft.entities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.evshiron.ingresscraft.Constants;
import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.client.gui.PortalGUI;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import javax.swing.text.html.parser.Entity;
import java.util.List;

/**
 * Created by evshiron on 5/26/15.
 */
public class PortalEntity extends IngressEntityBase {

    public World world;

    public static final String NAME = "portal";

    int mFaction = Constants.Faction.NEUTRAL;

    String mOwner;

    int mFaction = Constants.Faction.NEUTRAL;

    public void setmFaction(int mFaction) {
        this.mFaction = mFaction;
    }

    public PortalEntity(World world) {
        super(world);

    }

    /**
     * Health's done
     * Movement is done
     */
    @Override
    public void onLivingUpdate() {
        //System.err.println("Portal current Health:" + this.getHealth());
        if (this.getHealth() <= 0) {
            //this.setHealth(100);
            this.isDead = true;
        if(world.isRemote){
            net.minecraft.entity.Entity single = world.findNearestEntityWithinAABB(ResonatorEntity.class, this.boundingBox.expand(5, 5, 5), this);
            if (single != null) {
                System.out.println("found");
                if(((ResonatorEntity)single).mFaction== Constants.Faction.ENLIGHTENED){
                    this.setmFaction(Constants.Faction.ENLIGHTENED);
                }else if(((ResonatorEntity)single).mFaction== Constants.Faction.RESISTANCE){
                    this.setmFaction(Constants.Faction.RESISTANCE);
                }
            }else{
                this.setmFaction(Constants.Faction.NEUTRAL);
            }
        }
    }


    /**
     * this.setAttackTarget()
     * used to response to XM Burst
     */


    @Override
    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
        if (ForgeHooks.onLivingAttack(this, p_70097_1_, p_70097_2_)) return false;
        if (this.isEntityInvulnerable()) {
            return false;
        } else if (this.worldObj.isRemote) {
            return false;
        } else {
            this.entityAge = 0;

            if (this.getHealth() <= 0.0F) {
                return false;
            } else if (p_70097_1_.isFireDamage() && this.isPotionActive(Potion.fireResistance)) {
                return false;
            } else {
                if ((p_70097_1_ == DamageSource.anvil || p_70097_1_ == DamageSource.fallingBlock) && this.getEquipmentInSlot(4) != null) {
                    this.getEquipmentInSlot(4).damageItem((int) (p_70097_2_ * 4.0F + this.rand.nextFloat() * p_70097_2_ * 2.0F), this);
                    p_70097_2_ *= 0.75F;
                }
                this.limbSwingAmount = 1.5F;
                boolean flag = true;
                net.minecraft.entity.Entity entity = p_70097_1_.getEntity();
                if ((float) this.hurtResistantTime > (float) this.maxHurtResistantTime / 2.0F) {
                    if (p_70097_2_ <= this.lastDamage) {
                        return false;
                    }
                    this.damageEntity(p_70097_1_, p_70097_2_ - this.lastDamage);
                    this.lastDamage = p_70097_2_;
                    flag = false;
                } else {

                    this.lastDamage = p_70097_2_;
                    this.prevHealth = this.getHealth();
                    this.hurtResistantTime = this.maxHurtResistantTime;
                    if (entity instanceof EntityPlayer) {
                        EntityPlayer player = (EntityPlayer) entity;
                        /**
                         * TODO:replace the IngressCraft.scanner to anything you want
                         */
                        if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem().equals(IngressCraft.scanner)) {
                            System.out.println("boom");
                            this.damageEntity(p_70097_1_, p_70097_2_);
                            player.inventory.consumeInventoryItem(player.getCurrentEquippedItem().getItem());
                        }
                    } else {
                        System.out.println(entity);
                    }
                    this.hurtTime = this.maxHurtTime = 10;
                }
                this.attackedAtYaw = 0.0F;
                if (entity != null) {
                    if (entity instanceof EntityPlayer) {
                        EntityPlayer player = (EntityPlayer) entity;
                        /**
                         * TODO:replace the IngressCraft.scanner to anything you want
                         */
                        if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem().equals(IngressCraft.scanner)) {
                            this.recentlyHit = 100;
                            this.attackingPlayer = (EntityPlayer) entity;
                        }
                    } else {
                        return false;
                    }
                }
                String s;

                if (this.getHealth() <= 0.0F) {
                    s = this.getDeathSound();

                    if (flag && s != null) {
                        this.playSound(s, this.getSoundVolume(), this.getSoundPitch());
                    }

                    this.onDeath(p_70097_1_);
                } else {
                    s = this.getHurtSound();

                    if (flag && s != null) {
                        this.playSound(s, this.getSoundVolume(), this.getSoundPitch());
                    }
                }

                return true;
            }
        }
    }

}
