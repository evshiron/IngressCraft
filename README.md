# IngressCraft

A whim of making Ingress in MineCraft 1.7.10 with Forge.

## Directives

A Portal is an Entity that accepts left click (Hack), right click with hand (Inspect, and further linking). Deployed Resonators will stay around and their distances to Portal are determined by the operating Agent. That is to say, a Resonator should have a Portal as parent.

A Resonator is an Item that can be deployed by left click on a Portal, or be recycled by right hold.

An XMPBurster can be fired by left click, and damage Resonators around, or be recycled by right hold.

A Mod can be deployed by left click on a Portal, or be recycled by right hold.

A Power Cube?

## TODO

* Extract Wavefront .obj from Ingress.ipa.
* Make Portal and Resonator Entity rendered as custom Wavefront model.
* Prevent Links from going through solid blocks.
* Make the position of a Resonator reasonable.

## Decompiled Code Comments

```

// Box related. Just for reference, this rendering method has huge limitation.

// upperCorner? = lowerCorner? + offset?;

ModelRenderer.addBox(
	float lowerCornerX, float lowerCornerY, float lowerCornerZ,
	float offsetX, float offsetY, float offsetZ
);

ModelBox(
	ModelRenderer renderer, int textureOffsetX, int textureOffsetY,
	float lowerCornerX, float lowerCornerY, float lowerCornerZ,
	float offsetX, float offsetY, float offsetZ,	float unknownOffset
);

PositionTextureVertex(
	float x, float y, float z,
	float u, float v
);

```

## References

* [http://bedrockminer.jimdo.com/modding-tutorials/basic-modding-1-7/](http://bedrockminer.jimdo.com/modding-tutorials/basic-modding-1-7/)
* [[1.7.2] Forge | Add new Block, Item, Entity, AI, Creative tab, Language localization, Block textures, Side textures - Mapping and Modding Tutorials - Mapping and Modding - Minecraft Forum - Minecraft Forum](http://www.minecraftforum.net/forums/mapping-and-modding/mapping-and-modding-tutorials/1571558-1-7-2-forge-add-new-block-item-entity-ai-creative)
* [GLSL Shaders with LWJGL - LWJGL](http://wiki.lwjgl.org/wiki/GLSL_Shaders_with_LWJGL)