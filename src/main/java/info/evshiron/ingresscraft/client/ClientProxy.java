package info.evshiron.ingresscraft.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import info.evshiron.ingresscraft.CommonProxy;
import info.evshiron.ingresscraft.entities.EntityPortal;
import info.evshiron.ingresscraft.client.renderers.PortalRenderer;
import info.evshiron.ingresscraft.entities.EntityResonator;
import info.evshiron.ingresscraft.client.renderers.ResonatorRenderer;

/**
 * Created by evshiron on 5/27/15
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void RegisterRenderers() {

        RenderingRegistry.registerEntityRenderingHandler(EntityPortal.class, new PortalRenderer());
        RenderingRegistry.registerEntityRenderingHandler(EntityResonator.class, new ResonatorRenderer());

    }

}
