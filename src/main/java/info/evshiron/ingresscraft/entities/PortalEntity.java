package info.evshiron.ingresscraft.entities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.evshiron.ingresscraft.IngressCraft;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import javax.swing.text.html.parser.Entity;

/**
 * Created by evshiron on 5/26/15.
 */
public class PortalEntity extends IngressEntityBase {

    public static final String NAME = "portal";

    public PortalEntity(World world) {
        super(world);

    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender(float p_70070_1_) {
        return 15728880;
    }

    public float getBrightness(float p_70013_1_) {
        return 1.0F;
    }

    /**
     * Health's done
     * Movement is done
     */
    @Override
    public void onLivingUpdate() {
        System.err.println("Portal current Health:" + this.getHealth());
        if (this.getHealth() <= 0) {
            this.setHealth(100);
            this.isDead = false;
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
                        EntityPlayer player = (EntityPlayer)entity;
                        /**
                         * TODO:replace the IngressCraft.scanner to anything you want
                         */
                        if(player.getCurrentEquippedItem().getItem().equals(IngressCraft.scanner)){
                            this.damageEntity(p_70097_1_, p_70097_2_);
                        }

                    }
                    this.hurtTime = this.maxHurtTime = 10;
                }
                this.attackedAtYaw = 0.0F;
                if (entity != null) {
                    if (entity instanceof EntityPlayer) {
                        EntityPlayer player = (EntityPlayer)entity;
                        /**
                         * TODO:replace the IngressCraft.scanner to anything you want
                         */
                        if(player.getCurrentEquippedItem().getItem().equals(IngressCraft.scanner)){
                            this.recentlyHit = 100;
                            this.attackingPlayer = (EntityPlayer) entity;
                        }
                    } else {
                        return false;
                    }
                }

                /*if (flag) {
                    this.worldObj.setEntityState(this, (byte) 2);

                    if (p_70097_1_ != DamageSource.drown) {
                        this.setBeenAttacked();
                    }

                    if (entity != null) {
                        double d1 = entity.posX - this.posX;
                        double d0;

                        for (d0 = entity.posZ - this.posZ; d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D) {
                            d1 = (Math.random() - Math.random()) * 0.01D;
                        }

                        this.attackedAtYaw = (float) (Math.atan2(d0, d1) * 180.0D / Math.PI) - this.rotationYaw;
                        this.knockBack(entity, p_70097_2_, d1, d0);
                    } else {
                        this.attackedAtYaw = (float) ((int) (Math.random() * 2.0D) * 180);
                    }
                }*/

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
