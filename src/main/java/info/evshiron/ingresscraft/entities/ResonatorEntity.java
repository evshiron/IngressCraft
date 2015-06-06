package info.evshiron.ingresscraft.entities;

import info.evshiron.ingresscraft.Constants;
import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.client.gui.PortalGUI;
import info.evshiron.ingresscraft.client.gui.ScannerGUI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

/**
 * Created by evshiron on 5/31/15.
 */
public class ResonatorEntity extends IngressEntityBase {

    public static String NAME = "resonator";

    int mFaction = Constants.Faction.NEUTRAL;

    String mOwner;

    public ResonatorEntity(World world) {

        super(world);

    }

    public void SetFaction(int faction) {

        mFaction = faction;

    }

    public void SetOwner(String owner) {

        mOwner = owner;

    }

    @Override
    protected boolean interact(EntityPlayer player) {

        player.openGui(IngressCraft.Instance, ScannerGUI.ID, worldObj, 0, 0, 0);

        return true;

    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(1000.0);
    }

    @Override
    public void onLivingUpdate() {
        //System.err.println("Resonator current Health:" + this.getHealth());
        if (this.getHealth() <= 0) {
            //this.setHealth(100);
            this.isDead = true;
        }
    }

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
                        if(player.getCurrentEquippedItem()!=null&&player.getCurrentEquippedItem().getItem().equals(IngressCraft.xmp)){
                            System.out.println("boom");
                            this.damageEntity(p_70097_1_, p_70097_2_);
                            //player.inventory.consumeInventoryItem(player.getCurrentEquippedItem().getItem());
                        }
                    }else{
                        System.out.println(entity);
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
                        if(player.getCurrentEquippedItem()!=null&&player.getCurrentEquippedItem().getItem().equals(IngressCraft.xmp)){
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
