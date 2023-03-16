package io.redspace.ironsspellbooks.setup;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.block.pedestal.PedestalRenderer;
import io.redspace.ironsspellbooks.block.scroll_forge.ScrollForgeRenderer;
import io.redspace.ironsspellbooks.entity.armor.*;
import io.redspace.ironsspellbooks.entity.armor.pumpkin.PumpkinArmorModel;
import io.redspace.ironsspellbooks.entity.armor.pumpkin.PumpkinArmorRenderer;
import io.redspace.ironsspellbooks.entity.armor.simple_wizard.WizardArmorRenderer;
import io.redspace.ironsspellbooks.entity.blood_slash.BloodSlashRenderer;
import io.redspace.ironsspellbooks.entity.cone_of_cold.ConeOfColdRenderer;
import io.redspace.ironsspellbooks.entity.creeper_head.CreeperHeadRenderer;
import io.redspace.ironsspellbooks.entity.electrocute.ElectrocuteRenderer;
import io.redspace.ironsspellbooks.entity.firebolt.FireboltRenderer;
import io.redspace.ironsspellbooks.entity.icicle.IcicleRenderer;
import io.redspace.ironsspellbooks.entity.lightning_lance.LightningLanceRenderer;
import io.redspace.ironsspellbooks.entity.magic_arrow.MagicArrowRenderer;
import io.redspace.ironsspellbooks.entity.magic_missile.MagicMissileRenderer;
import io.redspace.ironsspellbooks.entity.mobs.dead_king_boss.DeadKingRenderer;
import io.redspace.ironsspellbooks.entity.mobs.debug_wizard.DebugWizardRenderer;
import io.redspace.ironsspellbooks.entity.mobs.frozen_humanoid.FrozenHumanoidRenderer;
import io.redspace.ironsspellbooks.entity.mobs.horse.SpectralSteedRenderer;
import io.redspace.ironsspellbooks.entity.mobs.necromancer.NecromancerRenderer;
import io.redspace.ironsspellbooks.entity.mobs.wizards.archevoker.ArchevokerRenderer;
import io.redspace.ironsspellbooks.entity.mobs.wizards.pyromancer.PyromancerRenderer;
import io.redspace.ironsspellbooks.entity.shield.ShieldModel;
import io.redspace.ironsspellbooks.entity.shield.ShieldRenderer;
import io.redspace.ironsspellbooks.entity.shield.ShieldTrimModel;
import io.redspace.ironsspellbooks.entity.wisp.WispRenderer;
import io.redspace.ironsspellbooks.item.WaywardCompass;
import io.redspace.ironsspellbooks.item.armor.*;
import io.redspace.ironsspellbooks.particle.*;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import io.redspace.ironsspellbooks.render.*;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.item.CompassItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@Mod.EventBusSubscriber(modid = IronsSpellbooks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        //LayerDefinition basicHumanLayer = LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F), 64, 64);

        //See LayerDefinitions.createRoots
        LayerDefinition energyOverlayLayer = LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(1.25F), 0.0F), 64, 32);
        LayerDefinition outerLayer = LayerDefinition.create(HumanoidModel.createMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0.0F), 64, 32);
        LayerDefinition innerLayer = LayerDefinition.create(HumanoidModel.createMesh(LayerDefinitions.INNER_ARMOR_DEFORMATION, 0.0F), 64, 32);

        //event.registerLayerDefinition(PyromancerRenderer.PYROMANCER_MODEL_LAYER, PyromancerModel::createBodyLayer);
        //event.registerLayerDefinition(PyromancerRenderer.PYROMANCER_INNER_ARMOR, () -> innerLayer);
        //event.registerLayerDefinition(PyromancerRenderer.PYROMANCER_OUTER_ARMOR, () -> outerLayer);

//        event.registerLayerDefinition(NecromancerRenderer.NECROMANCER_MODEL_LAYER, NecromancerModel::createBodyLayer);
//        event.registerLayerDefinition(NecromancerRenderer.NECROMANCER_INNER_ARMOR, () -> innerLayer);
//        event.registerLayerDefinition(NecromancerRenderer.NECROMANCER_OUTER_ARMOR, () -> outerLayer);

        event.registerLayerDefinition(ShieldModel.LAYER_LOCATION, ShieldModel::createBodyLayer);
        event.registerLayerDefinition(ShieldTrimModel.LAYER_LOCATION, ShieldTrimModel::createBodyLayer);
        event.registerLayerDefinition(AngelWingsModel.ANGEL_WINGS_LAYER, AngelWingsModel::createLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.AddLayers event) {
        GeoArmorRenderer.registerArmorRenderer(WizardArmorItem.class, WizardArmorRenderer::new);
        GeoArmorRenderer.registerArmorRenderer(WanderingMagicianArmorItem.class, () -> new GenericCustomArmorRenderer(new WanderingMagicianModel()));
        GeoArmorRenderer.registerArmorRenderer(PyromancerArmorItem.class, () -> new GenericCustomArmorRenderer(new PyromancerArmorModel()));
        GeoArmorRenderer.registerArmorRenderer(ElectromancerArmorItem.class, () -> new GenericCustomArmorRenderer(new ElectromancerArmorModel()));
        GeoArmorRenderer.registerArmorRenderer(ArchevokerArmorItem.class, () -> new GenericCustomArmorRenderer(new ArchevokerArmorModel()));
        GeoArmorRenderer.registerArmorRenderer(CultistArmorItem.class, () -> new GenericCustomArmorRenderer(new CultistArmorModel()));
        GeoArmorRenderer.registerArmorRenderer(CryomancerArmorItem.class, () -> new GenericCustomArmorRenderer(new CryomancerArmorModel()));
        GeoArmorRenderer.registerArmorRenderer(ShadowwalkerArmorItem.class, () -> new GenericCustomArmorRenderer(new ShadowwalkerArmorModel()));
        GeoArmorRenderer.registerArmorRenderer(PriestArmorItem.class, () -> new GenericCustomArmorRenderer(new PriestArmorModel()));
        GeoArmorRenderer.registerArmorRenderer(TarnishedCrownArmorItem.class, () -> new GenericCustomArmorRenderer(new TarnishedCrownModel()));
        GeoArmorRenderer.registerArmorRenderer(PumpkinArmorItem.class, () -> new PumpkinArmorRenderer(new PumpkinArmorModel()));

        IronsSpellbooks.LOGGER.debug("registerRenderers: EntityRenderersEvent.AddLayers event: {}", event.toString());

        addLayerToPlayerSkin(event, "default");
        addLayerToPlayerSkin(event, "slim");
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void addLayerToPlayerSkin(EntityRenderersEvent.AddLayers event, String skinName) {
        EntityRenderer<? extends Player> render = event.getSkin(skinName);
        if (render instanceof LivingEntityRenderer livingRenderer) {
            livingRenderer.addLayer(new AngelWingsLayer<>(livingRenderer));
            livingRenderer.addLayer(new EvasionLayer(livingRenderer));
            livingRenderer.addLayer(new ChargeSpellLayer(livingRenderer));
            livingRenderer.addLayer(new GlowingEyesLayer(livingRenderer));
        }
        //EntityRenderer<? extends AbstractSpellCastingMob> renderer = event.getRenderer(EntityRegistry.PYROMANCER.get());
    }

    @SubscribeEvent
    public static void rendererRegister(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.MAGIC_MISSILE_PROJECTILE.get(), MagicMissileRenderer::new);
        event.registerEntityRenderer(EntityRegistry.CONE_OF_COLD_PROJECTILE.get(), ConeOfColdRenderer::new);
        event.registerEntityRenderer(EntityRegistry.BLOOD_SLASH_PROJECTILE.get(), BloodSlashRenderer::new);
        event.registerEntityRenderer(EntityRegistry.ELECTROCUTE_PROJECTILE.get(), ElectrocuteRenderer::new);
        event.registerEntityRenderer(EntityRegistry.FIREBOLT_PROJECTILE.get(), FireboltRenderer::new);
        event.registerEntityRenderer(EntityRegistry.ICICLE_PROJECTILE.get(), IcicleRenderer::new);
        event.registerEntityRenderer(EntityRegistry.FIRE_BREATH_PROJECTILE.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityRegistry.DRAGON_BREATH_PROJECTILE.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityRegistry.DEBUG_WIZARD.get(), (renderManager) -> new DebugWizardRenderer(renderManager));
        event.registerEntityRenderer(EntityRegistry.PYROMANCER.get(), PyromancerRenderer::new);
        event.registerEntityRenderer(EntityRegistry.NECROMANCER.get(), NecromancerRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SPECTRAL_STEED.get(), SpectralSteedRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SHIELD_ENTITY.get(), ShieldRenderer::new);
        event.registerEntityRenderer(EntityRegistry.WALL_OF_FIRE_ENTITY.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntityRegistry.WISP.get(), WispRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SUMMONED_VEX.get(), VexRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SUMMONED_ZOMBIE.get(), ZombieRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SUMMONED_SKELETON.get(), SkeletonRenderer::new);
        event.registerEntityRenderer(EntityRegistry.LIGHTNING_LANCE_PROJECTILE.get(), LightningLanceRenderer::new);
        event.registerEntityRenderer(EntityRegistry.WITHER_SKULL_PROJECTILE.get(), WitherSkullRenderer::new);
        event.registerEntityRenderer(EntityRegistry.MAGIC_ARROW_PROJECTILE.get(), MagicArrowRenderer::new);
        event.registerEntityRenderer(EntityRegistry.CREEPER_HEAD_PROJECTILE.get(), CreeperHeadRenderer::new);
        event.registerEntityRenderer(EntityRegistry.FROZEN_HUMANOID.get(), FrozenHumanoidRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SMALL_FIREBALL_PROJECTILE.get(), (p_174082_) -> new ThrownItemRenderer<>(p_174082_, 0.75F, true));
        event.registerEntityRenderer(EntityRegistry.SUMMONED_POLAR_BEAR.get(), PolarBearRenderer::new);
        event.registerEntityRenderer(EntityRegistry.DEAD_KING.get(), DeadKingRenderer::new);
        event.registerEntityRenderer(EntityRegistry.DEAD_KING_CORPSE.get(), DeadKingRenderer::new);
        event.registerEntityRenderer(EntityRegistry.ARCHEVOKER.get(), ArchevokerRenderer::new);

        event.registerBlockEntityRenderer(BlockRegistry.SCROLL_FORGE_TILE.get(), ScrollForgeRenderer::new);
        event.registerBlockEntityRenderer(BlockRegistry.PEDESTAL_TILE.get(), PedestalRenderer::new);
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.register(ParticleRegistry.WISP_PARTICLE.get(), WispParticle.Provider::new);
        event.register(ParticleRegistry.BLOOD_PARTICLE.get(), BloodParticle.Provider::new);
        event.register(ParticleRegistry.BLOOD_GROUND_PARTICLE.get(), BloodGroundParticle.Provider::new);
        event.register(ParticleRegistry.SNOWFLAKE_PARTICLE.get(), SnowflakeParticle.Provider::new);
        event.register(ParticleRegistry.ELECTRICITY_PARTICLE.get(), ElectricityParticle.Provider::new);
        event.register(ParticleRegistry.UNSTABLE_ENDER_PARTICLE.get(), UnstableEnderParticle.Provider::new);
        event.register(ParticleRegistry.DRAGON_FIRE_PARTICLE.get(), DragonFireParticle.Provider::new);
        event.register(ParticleRegistry.FIRE_PARTICLE.get(), FireParticle.Provider::new);
        event.register(ParticleRegistry.EMBER_PARTICLE.get(), EmberParticle.Provider::new);
        event.register(ParticleRegistry.SIPHON_PARTICLE.get(), SiphonParticle.Provider::new);
    }

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent e) {
        //Item Properties
        e.enqueueWork(() -> ItemProperties.register(ItemRegistry.WAYWARD_COMPASS.get(), new ResourceLocation("angle"), new CompassItemPropertyFunction((level, itemStack, entity) -> WaywardCompass.getCatacombsLocation(entity, itemStack.getOrCreateTag()))));
    }

}
