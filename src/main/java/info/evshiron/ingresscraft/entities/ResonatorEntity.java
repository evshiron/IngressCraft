package info.evshiron.ingresscraft.entities;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import info.evshiron.ingresscraft.Constants;
import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.client.gui.ScannerGUI;
import info.evshiron.ingresscraft.items.ScannerItem;
import info.evshiron.ingresscraft.items.XMPBursterItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import javax.sound.sampled.Port;
import java.util.List;

/**
 * Created by evshiron on 5/31/15.
 */
public class ResonatorEntity extends IngressEntityBase implements IEntityAdditionalSpawnData {

    public static String NAME = "resonator";

    public int mFaction = Constants.Faction.NEUTRAL;

    public String mOwner;

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
    public void writeEntityToNBT(NBTTagCompound nbt) {

        nbt.setInteger("faction", mFaction);
        nbt.setString("owner", mOwner);

        super.writeEntityToNBT(nbt);

    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {

        super.readEntityFromNBT(nbt);

        mFaction = nbt.getInteger("faction");
        mOwner = nbt.getString("owner");

    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {

        ByteBufUtils.writeUTF8String(buffer, String.valueOf(mFaction));
        ByteBufUtils.writeUTF8String(buffer, mOwner);

    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {

        mFaction = Integer.parseInt(ByteBufUtils.readUTF8String(additionalData));
        mOwner = ByteBufUtils.readUTF8String(additionalData);

    }

    @Override
    protected void applyEntityAttributes() {

        super.applyEntityAttributes();

        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(1000.0);

    }

    @Override
    public void onLivingUpdate() {

        //System.out.println("Resonator current Health:" + this.getHealth());

        if (this.getHealth() <= 0) {

            //this.setHealth(100);
            this.isDead = true;

            if(attackingPlayer != null && attackingPlayer.getCurrentArmor(3).getItem() instanceof ScannerItem) {

                NBTTagCompound nbt = attackingPlayer.getCurrentArmor(3).getTagCompound();

                String broadcast = String.format("%s has destroyed a resonator.", nbt.getString("codename"));

                Minecraft.getMinecraft().getIntegratedServer().getConfigurationManager().sendChatMsg(new ChatComponentText(broadcast));

            }

            return;

        }

        List<PortalEntity> entities = worldObj.getEntitiesWithinAABB(PortalEntity.class, boundingBox.expand(4, 4, 4));

        for(int i = 0; i < entities.size(); i++) {

            PortalEntity entity = entities.get(i);

            if(entity.mFaction == Constants.Faction.NEUTRAL) {

                entity.SetFaction(mFaction);
                entity.SetOwner(mOwner);

            }

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

                Entity entity = p_70097_1_.getEntity();

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

                        if (player.getCurrentArmor(3) != null && player.getCurrentArmor(3).getTagCompound() != null && player.getCurrentArmor(3).getTagCompound().getInteger("faction") != mFaction) {

                            if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof XMPBursterItem) {

                                this.damageEntity(p_70097_1_, p_70097_2_);

                            }

                        }

                    }
                    else {

                        System.out.println(entity);

                    }

                    this.hurtTime = this.maxHurtTime = 10;

                }

                this.attackedAtYaw = 0.0F;

                if (entity != null) {

                    if (entity instanceof EntityPlayer) {

                        EntityPlayer player = (EntityPlayer) entity;

                        if(player.getCurrentArmor(3) != null && player.getCurrentArmor(3).getTagCompound() != null && player.getCurrentArmor(3).getTagCompound().getInteger("faction") != mFaction) {

                            if(player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof XMPBursterItem) {

                                this.recentlyHit = 100;
                                this.attackingPlayer = (EntityPlayer) entity;

                            }

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
