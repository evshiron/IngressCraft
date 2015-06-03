package info.evshiron.ingresscraft.blocks;

import info.evshiron.ingresscraft.IngressCraft;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

/**
 * Created by evshiron on 5/27/15.
 */
public class XMBlock extends Block {

    public static final String NAME = "xm_block";

    public XMBlock() {

        super(Material.ground);

        setBlockName(NAME);
        setCreativeTab(CreativeTabs.tabBlock);
        setBlockTextureName(IngressCraft.MODID + ":" + NAME);
        setLightLevel(0.5f);

    }

}
