package info.evshiron.ingresscraft;

import cpw.mods.fml.client.registry.RenderingRegistry;
import info.evshiron.ingresscraft.entities.PortalEntity;
import info.evshiron.ingresscraft.entities.ResonatorEntity;
import info.evshiron.ingresscraft.entities.ResonatorRenderer;

/**
 * Created by evshiron on 5/27/15.
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void RegisterRenderers() {

        RenderingRegistry.registerEntityRenderingHandler(ResonatorEntity.class, new ResonatorRenderer());
        //RenderingRegistry.registerEntityRenderingHandler(PortalEntity.class, new RenderEntity());

    }

}
