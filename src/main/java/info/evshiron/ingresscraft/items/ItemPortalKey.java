package info.evshiron.ingresscraft.items;

import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.messages.MessageHandler;
import info.evshiron.ingresscraft.messages.MessageGetPortalInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by evshiron on 6/12/15.
 */
public class ItemPortalKey extends Item {

    public static final String NAME = "portalKey";

    static boolean mIsRequesting = false;

    public static String UUID = "";
    public static String Name = "";
    public static String Owner = "";
    public static int Faction = 0;
    public static int Level = 0;
    public static double X = 0;
    public static double Y = 0;
    public static double Z = 0;

    public static void RequestPortalInfo(EntityPlayer player, String uuid) {

        mIsRequesting = true;

        UUID = uuid;
        Name = "";
        Owner = "";
        Faction = 0;
        Level = 0;
        X = 0;
        Y = 0;
        Z = 0;

        MessageHandler.Wrapper.sendToServer(new MessageGetPortalInfo(player, uuid));

    }

    public static void ResponsePortalInfo(MessageGetPortalInfo message) {

        UUID = message.UUID;
        Name = message.Name;
        X = message.X;
        Y = message.Y;
        Z = message.Z;

        mIsRequesting = false;

    }

    public ItemPortalKey() {

        super();

        setUnlocalizedName(NAME);
        setCreativeTab(IngressCraft.CreativeTab);
        setTextureName(IngressCraft.MODID + ":" + NAME);

    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List lines, boolean p_77624_4_) {

        if(itemStack.hasTagCompound()) {

            NBTTagCompound nbt = itemStack.getTagCompound();
            String uuid = nbt.getString("uuid");

            if(!mIsRequesting && !uuid.contentEquals(UUID)) RequestPortalInfo(player, uuid);

        }

        lines.add(String.format("UUID: %s", UUID));
        lines.add(String.format("Name: %s", Name));
        lines.add(String.format("Owner: %s", Owner));
        lines.add(String.format("Level: %d", Level));
        lines.add(String.format("Position: %f, %f, %f", X, Y, Z));
        lines.add(String.format("Distance: %f", Math.sqrt(Math.pow(X - player.posX, 2) + Math.pow(Y - player.posY, 2) + Math.pow(Z - player.posZ, 2))));

    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_) {

        if(!itemStack.hasTagCompound()) {

            NBTTagCompound nbt = new NBTTagCompound();

            nbt.setString("uuid", java.util.UUID.randomUUID().toString());
            nbt.setString("name", "-UNKNOWN-");
            nbt.setInteger("level", 0);
            nbt.setDouble("x", 0);
            nbt.setDouble("y", 0);
            nbt.setDouble("z", 0);

            itemStack.setTagCompound(nbt);

        }

    }

}
