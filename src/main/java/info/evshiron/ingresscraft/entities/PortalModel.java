package info.evshiron.ingresscraft.entities;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * Created by evshiron on 5/28/15.
 */
public class PortalModel extends ModelBase {

    ModelRenderer mPortal;
    ModelRenderer[] mResonators = new ModelRenderer[8];

    PortalModel() {

        mPortal = new ModelRenderer(this, 0, 0);
        mPortal.addBox(0, 0, 0, 8, 8, 8);

        for(int i = 0; i < mResonators.length; i++) {

            mResonators[i] = new ModelRenderer(this, 0, 16);
            mResonators[i].addBox(0, 0, 0, 2, 8, 2);
            mResonators[i].offsetX = (float) Math.cos(2 * Math.PI / mResonators.length * i) * 2;
            mResonators[i].offsetZ = (float) Math.sin(2 * Math.PI / mResonators.length * i) * 2;

        }

    }

    @Override
    public void render(Entity entity, float time, float swingSuppress, float p_78088_4_, float headAngleY, float headAngleX, float p_78088_7_) {

        mPortal.render(p_78088_7_);

        for(int i = 0; i < mResonators.length; i++) {

            mResonators[i].render(p_78088_7_);

        }

    }

}
