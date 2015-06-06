package info.evshiron.ingresscraft;

import cpw.mods.fml.client.registry.RenderingRegistry;
import info.evshiron.ingresscraft.entities.PortalEntity;
import info.evshiron.ingresscraft.entities.PortalRenderer;
import info.evshiron.ingresscraft.entities.ResonatorEntity;
import info.evshiron.ingresscraft.entities.ResonatorRenderer;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by evshiron on 5/27/15
 */
public class ClientProxy extends CommonProxy {

    private PortalRenderer portal;
    private  ResonatorRenderer resonator;
    private EntityPlayer player;

    @Override
    public void RegisterRenderers() {
        portal = new PortalRenderer();
        resonator = new ResonatorRenderer();
        RenderingRegistry.registerEntityRenderingHandler(PortalEntity.class,portal);
        RenderingRegistry.registerEntityRenderingHandler(ResonatorEntity.class,resonator);

    }

    @Override
    public void setPlayer(EntityPlayer player) {
        this.player = player;
        portal.setPlayer(player);
    }

    @Override
    public EntityPlayer getPlayer() {
        return player;
    }
}
