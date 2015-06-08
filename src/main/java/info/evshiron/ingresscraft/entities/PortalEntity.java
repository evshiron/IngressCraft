package info.evshiron.ingresscraft.entities;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.evshiron.ingresscraft.Constants;
import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.client.gui.PortalGUI;
import info.evshiron.ingresscraft.items.ResonatorItem;
import info.evshiron.ingresscraft.items.ScannerItem;
import info.evshiron.ingresscraft.items.XMPBursterItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import javax.swing.text.html.parser.Entity;
import java.util.List;

/**
 * Created by evshiron on 5/26/15.
 */
public class PortalEntity extends IngressEntityBase implements IEntityAdditionalSpawnData {

    public static final String NAME = "portal";

    public int mFaction = Constants.Faction.NEUTRAL;

    public String mOwner = "NIA";

    public PortalEntity(World world) {

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
    public void onLivingUpdate() {

        List resonators = worldObj.getEntitiesWithinAABB(ResonatorEntity.class, boundingBox.expand(4, 4, 4));

        // When called, the last resonator has not been destoryed.
        if (resonators.size() == 0) {

            // FIXME: But client's faction is not changed. Message needed.
            onDeath(new EntityDamageSource(IngressCraft.MODID + ":xmpBurster", attackingPlayer));

        }

    }

    public boolean attackEntityFrom(DamageSource source, float damage) {

        if(worldObj.isRemote) {

            return false;

        }

        entityAge = 0;

        if(source.getEntity() instanceof EntityPlayer) {

            if(((EntityPlayer) source.getEntity()).getCurrentEquippedItem() == null) {

                EntityPlayer player = (EntityPlayer) source.getEntity();

                dropFewItems(true, 1);

                return true;

            }
            else {

                EntityPlayer player = (EntityPlayer) source.getEntity();

                attackingPlayer = player;

                return true;

            }

        }

        return false;

    }

    @Override
    protected void dropFewItems(boolean isHitRecently, int lootingLevel) {

        int i = rand.nextInt(2);
        int j = rand.nextInt(3);

        switch(i) {

            case 0:

                if (lootingLevel > 0) {

                    j += rand.nextInt(lootingLevel + 1);

                }

                for (int k = 0; k < j; k++) {

                    dropItem(IngressCraft.ResonatorItem, 1);

                }

                break;

            case 1:

                if (lootingLevel > 0) {

                    j += rand.nextInt(lootingLevel + 1);

                }

                for (int k = 0; k < j; k++) {

                    this.dropItem(IngressCraft.XMPBursterItem, 1);

                }

                break;

        }

    }

    @Override
    protected void damageEntity(DamageSource source, float damage) {

        prevHealth = getHealth();

        setHealth(getHealth() - damage);

    }

    @Override
    public void onDeath(DamageSource source) {

        if(source.getEntity() instanceof EntityPlayer) {

            // FIXME: Never called.

            ItemStack scanner = ((EntityPlayer) source.getEntity()).getCurrentArmor(3);

            if(scanner.getItem() instanceof ScannerItem) {

                NBTTagCompound nbt = scanner.getTagCompound();

                String broadcast = String.format("%s has neutralized a portal.", nbt.getString("codename"));
                Minecraft.getMinecraft().getIntegratedServer().getConfigurationManager().sendChatMsg(new ChatComponentText(broadcast));

                // Show effects.

                SetFaction(Constants.Faction.NEUTRAL);
                SetOwner("NIA");

            }

        }
        else if(source.getEntity() == null) {

            SetFaction(Constants.Faction.NEUTRAL);
            SetOwner("NIA");

        }

    }

}
