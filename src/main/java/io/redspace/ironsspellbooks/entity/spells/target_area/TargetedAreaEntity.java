package io.redspace.ironsspellbooks.entity.spells.target_area;

import com.mojang.math.Vector3f;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidType;

public class TargetedAreaEntity extends Entity {
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(TargetedAreaEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(TargetedAreaEntity.class, EntityDataSerializers.INT);

    public TargetedAreaEntity(EntityType<TargetedAreaEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        setRadius(3f);
        this.noPhysics = true;
        this.setNoGravity(true);
    }

    @Override
    public void tick() {
        if (tickCount > 600)
            discard();
    }

    public TargetedAreaEntity(Level level, float radius, int color) {
        this(EntityRegistry.TARGET_AREA_ENTITY.get(), level);
        this.setRadius(radius);
        this.setColor(color);
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable(this.getRadius() * 2.0F, 0.8F);
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    protected void defineSynchedData() {
        this.getEntityData().define(DATA_RADIUS, 2F);
        this.getEntityData().define(DATA_COLOR, 0xFFFFFF);
    }

    public void setRadius(float pRadius) {
        if (!this.level.isClientSide) {
            this.getEntityData().set(DATA_RADIUS, Mth.clamp(pRadius, 0.0F, 32.0F));
        }
    }

    public float getRadius() {
        return this.getEntityData().get(DATA_RADIUS);
    }

    public void setColor(int color) {
        if (!this.level.isClientSide) {
            this.getEntityData().set(DATA_COLOR, color);
        }
    }

    public Vector3f getColor() {
        int color = this.getEntityData().get(DATA_COLOR);
        //Clever color mapping, taken from potionutils get color
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        return new Vector3f(red / 255.0f, green / 255.0f, blue / 255.0f);

    }

    public int getColorRaw() {
        return this.getEntityData().get(DATA_COLOR);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_RADIUS.equals(pKey)) {
            this.refreshDimensions();
            if (getRadius() < .1f)
                this.discard();
        }
        super.onSyncedDataUpdated(pKey);
    }

    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putFloat("Radius", this.getRadius());
        pCompound.putInt("Color", this.getColorRaw());
    }

    protected void readAdditionalSaveData(CompoundTag pCompound) {
        this.setRadius(pCompound.getFloat("Radius"));
        this.setColor(pCompound.getInt("Color"));

    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}