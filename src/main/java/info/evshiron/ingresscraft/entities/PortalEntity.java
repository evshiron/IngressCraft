package info.evshiron.ingresscraft.entities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import info.evshiron.ingresscraft.IngressCraft;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;

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
    public int getBrightnessForRender(float p_70070_1_)
    {
        return 15728880;
    }

    public float getBrightness(float p_70013_1_)
    {
        return 1.0F;
    }

}
