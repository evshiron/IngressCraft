package info.evshiron.ingresscraft.items;

import info.evshiron.ingresscraft.Constants;
import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.IngressData;
import info.evshiron.ingresscraft.client.effects.EffectLink;
import info.evshiron.ingresscraft.client.gui.GUICreatePortal;
import info.evshiron.ingresscraft.client.gui.GUICreateScanner;
import info.evshiron.ingresscraft.entities.EntityPortal;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by evshiron on 6/12/15.
 */
public class ItemPortal extends Item {

    public static final String NAME = "portal";

    public ItemPortal() {

        super();

        setUnlocalizedName(NAME);
        setCreativeTab(IngressCraft.CreativeTab);
        setTextureName(IngressCraft.MODID + ":" + NAME);

    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int targetSide, float targetX, float targetY, float targetZ) {

        //Vector3f start = new Vector3f((float) 0, (float) 4, (float) 0);
        //Vector3f end = new Vector3f((float) player.posX, (float) player.posY, (float) player.posZ);
        //Minecraft.getMinecraft().effectRenderer.addEffect(new EffectLink(world, start, end, Constants.TeamColor.RESISTANCE));

        player.openGui(IngressCraft.Instance, GUICreatePortal.ID, world, (int) (x + targetX), (int) (y + targetY), (int) (z + targetZ));

        return false;

    }

}
