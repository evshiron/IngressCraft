package info.evshiron.ingresscraft;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

/**
 * Created by evshiron on 7/17/15.
 */
public class IngressData extends WorldSavedData {

    public static IngressData GetInstance(World world) {

        IngressData instance = (IngressData) world.loadItemData(IngressData.class, IngressCraft.MODID);

        if(instance == null) {

            instance = new IngressData();
            world.setItemData(IngressCraft.MODID, instance);

        }

        return instance;

    }

    NBTTagCompound mAgents = new NBTTagCompound();
    NBTTagCompound mPortals = new NBTTagCompound();
    NBTTagCompound mResonators = new NBTTagCompound();
    NBTTagCompound mLinks = new NBTTagCompound();
    NBTTagCompound mFields = new NBTTagCompound();

    public IngressData() {
        super(IngressCraft.MODID);
    }

    public IngressData(String key) {
        super(key);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {

        mAgents = nbt.getCompoundTag("agents");
        mPortals = nbt.getCompoundTag("portals");

    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {

        nbt.setTag("agents", mAgents);
        nbt.setTag("portals", mPortals);

    }

    public void RegisterAgent(String codename, int faction) {

        NBTTagCompound agent = new NBTTagCompound();

        agent.setString("codename", codename);
        agent.setInteger("faction", faction);

        mAgents.setTag(codename, agent);

    }

    public void RegisterPortal(String uuid, String name, double x, double y, double z) {

        NBTTagCompound portal = new NBTTagCompound();

        portal.setString("uuid", uuid);
        portal.setString("name", name);
        portal.setDouble("x", x);
        portal.setDouble("y", y);
        portal.setDouble("z", z);

        mPortals.setTag(uuid, portal);

        markDirty();

    }

    public NBTTagCompound GetPortal(String uuid) {

        return mPortals.getCompoundTag(uuid);

    }

    public boolean GetPortalLinkability(String fromUuid, String toUuid) {

        boolean linkability = true;

        if(fromUuid.contentEquals(toUuid)) linkability = false;

        return linkability;

    }

}
