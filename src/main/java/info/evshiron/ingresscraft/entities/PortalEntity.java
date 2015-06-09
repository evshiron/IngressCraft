package info.evshiron.ingresscraft.entities;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import info.evshiron.ingresscraft.Constants;
import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.items.ScannerItem;
import info.evshiron.ingresscraft.items.XMPBursterItem;
import info.evshiron.ingresscraft.utils.IngressHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by evshiron on 5/26/15.
 */
public class PortalEntity extends IngressEntityBase implements IEntityAdditionalSpawnData {

    public static final String NAME = "portal";

    public int Faction = Constants.Faction.NEUTRAL;
    public String Owner = "NIA";
    public EntityPlayer AttackingAgent = null;

    public PortalEntity(World world) {

        super(world);

    }

    public void SetFaction(int faction) {

        Faction = faction;

    }

    public void SetOwner(String owner) {

        Owner = owner;

    }

    public int GetLevel() {

        int levels = 0;

        List<ResonatorEntity> resonators = IngressHelper.GetEntitiesAround(worldObj, ResonatorEntity.class, this, IngressCraft.CONFIG_PORTAL_RANGE);

        for(int i = 0; i < resonators.size(); i++) {

            ResonatorEntity resonator = resonators.get(i);

            levels += resonator.Level;

        }

        return resonators.size() == 0 ? 0 : levels / resonators.size();

    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {

        nbt.setInteger("faction", Faction);
        nbt.setString("owner", Owner);

        super.writeEntityToNBT(nbt);

    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {

        super.readEntityFromNBT(nbt);

        Faction = nbt.getInteger("faction");
        Owner = nbt.getString("owner");

    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {

        ByteBufUtils.writeUTF8String(buffer, String.valueOf(Faction));
        ByteBufUtils.writeUTF8String(buffer, Owner);

    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {

        Faction = Integer.parseInt(ByteBufUtils.readUTF8String(additionalData));
        Owner = ByteBufUtils.readUTF8String(additionalData);

    }

    @Override
    public boolean shouldRenderInPass(int pass) {

        // 0 = Opaque, 1 = Translucent.
        return pass == 1;

    }

    @Override
    public void onLivingUpdate() {

        List resonators = IngressHelper.GetEntitiesAround(worldObj, ResonatorEntity.class, this, IngressCraft.CONFIG_PORTAL_RANGE);

        // When called, the last resonator has not been destoryed.
        if (Faction != Constants.Faction.NEUTRAL && resonators.size() == 0) {

            // FIXME: But client's faction is not changed. Message needed.
            onDeath(new EntityDamageSource(IngressCraft.MODID + ":xmpBurster", AttackingAgent));

        }

    }

    public boolean attackEntityFrom(DamageSource source, float damage) {

        if(worldObj.isRemote) {

            return false;

        }

        if(source.getEntity() instanceof EntityPlayer) {

            EntityPlayer player = (EntityPlayer) source.getEntity();

            ItemStack scanner = player.getCurrentArmor(3);

            if(!(scanner.getItem() instanceof ScannerItem)) {

                return false;

            }

            if(source.getDamageType().contentEquals(IngressCraft.MODID + ":xmpBurster")) {

                AttackingAgent = player;

                return true;

            }
            else {

                NBTTagCompound nbt = scanner.getTagCompound();

                int times = rand.nextInt(4);
                for(int i = 0; i < times; i++) {

                    int type = rand.nextInt(2);

                    int amount;
                    ItemStack itemStack;
                    NBTTagCompound nbt1;

                    switch(type) {
                        case 0: // Resonator.

                            amount = rand.nextInt(3 + GetLevel() / 2 + (nbt.getInteger("faction") == Faction ? 2 : 0));

                            itemStack = new ItemStack(IngressCraft.ResonatorItem, amount, 0);
                            nbt1 = new NBTTagCompound();
                            // Math.max let level >= 1, Math.min etc. let level in [x-2, x+2].
                            nbt1.setInteger("level", Math.max(1, Math.min(nbt.getInteger("level"), GetLevel()) - 2 + rand.nextInt(5)));
                            itemStack.setTagCompound(nbt1);

                            entityDropItem(itemStack, 0);
                            break;

                        case 1: // XMPBurster.

                            amount = rand.nextInt(3 + GetLevel() / 2 + (nbt.getInteger("faction") != Faction ? 2 : 0));

                            itemStack = new ItemStack(IngressCraft.XMPBursterItem, amount, 0);
                            nbt1 = new NBTTagCompound();
                            nbt1.setInteger("level", Math.max(1, Math.min(nbt.getInteger("level"), GetLevel()) - 2 + rand.nextInt(5)));
                            itemStack.setTagCompound(nbt1);

                            entityDropItem(itemStack, 0);
                            break;

                    }

                }

                return true;

            }

        }

        return false;

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

                ChatComponentText message = new ChatComponentText("");
                message.appendSibling(
                    new ChatComponentText(nbt.getString("codename"))
                    .setChatStyle(
                        new ChatStyle()
                        .setColor(nbt.getInteger("faction") == Constants.Faction.RESISTANCE ? EnumChatFormatting.BLUE : EnumChatFormatting.GREEN)
                    )
                );
                message.appendSibling(new ChatComponentText(" has neutralized a Portal."));
                Minecraft.getMinecraft().getIntegratedServer().getConfigurationManager().sendChatMsg(message);

                // Show effects.

                SetFaction(Constants.Faction.NEUTRAL);
                SetOwner("NIA");

                AttackingAgent = null;

            }

        }
        else if(source.getEntity() == null) {

            SetFaction(Constants.Faction.NEUTRAL);
            SetOwner("NIA");

        }

    }

}
