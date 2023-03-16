package io.redspace.ironsspellbooks.block.inscription_table;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.extensions.IForgeBlock;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

//https://youtu.be/CUHEKcaIpOk?t=451
public class InscriptionTableBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 18, 16);

    public InscriptionTableBlock() {
        super(BlockBehaviour.Properties.of(Material.WOOD).strength(2.5F).sound(SoundType.WOOD).noOcclusion());
    }

    @Override
    @SuppressWarnings("deprecation")
    public float getDestroyProgress(BlockState pState, Player player, BlockGetter pLevel, BlockPos pPos) {
        IronsSpellbooks.LOGGER.debug("getDestroyProgress: {} {}", pPos, pState);
        if (player.getItemInHand(player.getUsedItemHand()).getItem() instanceof AxeItem) {
            return 1;
        }
        return super.getDestroyProgress(pState, player, pLevel, pPos);
    }

    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        IronsSpellbooks.LOGGER.debug("InscriptionTablePlaceholderBlock.playerWillDestroy: {} {}", pPos, pState);

        if (!pLevel.isClientSide) {
            var placeholderDirection = getPlaceholderDirection(pState.getValue(FACING));
            var neighborPos = pPos.relative(placeholderDirection);
            var blockstate = pLevel.getBlockState(neighborPos);
            if (blockstate.is(BlockRegistry.INSCRIPTION_TABLE_PLACEHOLDER_BLOCK.get())) {
                pLevel.setBlock(neighborPos, Blocks.AIR.defaultBlockState(), 35);
                //TODO: research if we need to raise a levelEvent here
                //pLevel.levelEvent(pPlayer, 2001, blockpos, Block.getId(blockstate));
            }
        }

        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        IronsSpellbooks.LOGGER.debug("updateShape: {} {} {} {} {}", pState, pFacing, pFacingState, pCurrentPos, pFacingPos);
        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction placingDirection = context.getHorizontalDirection().getOpposite();
        BlockPos blockpos = context.getClickedPos();
        BlockPos blockpos1 = blockpos.relative(getPlaceholderDirection(placingDirection));
        Level level = context.getLevel();

        if (level.getBlockState(blockpos1).canBeReplaced(context) && level.getWorldBorder().isWithinBounds(blockpos1)) {
            return this.defaultBlockState().setValue(FACING, placingDirection);
        }

        return null;
    }

    private Direction getPlaceholderDirection(Direction facing) {
        return switch (facing) {
            case NORTH -> Direction.EAST;
            case SOUTH -> Direction.WEST;
            case WEST -> Direction.NORTH;
            default -> Direction.SOUTH;
        };
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @javax.annotation.Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        if (!pLevel.isClientSide) {
            var placeholderDirection = getPlaceholderDirection(pState.getValue(FACING));
            BlockPos blockpos = pPos.relative(placeholderDirection);
            var state = BlockRegistry.INSCRIPTION_TABLE_PLACEHOLDER_BLOCK.get().defaultBlockState().setValue(FACING, placeholderDirection.getOpposite());
            pLevel.setBlock(blockpos, state, 3);
            // pLevel.setBlock(blockpos, pState.setValue(PART, InscriptionTablePart.PLACEHOLDER), 3);
            //pLevel.blockUpdated(blockpos, BlockRegistry.INSCRIPTION_TABLE_PLACEHOLDER_BLOCK.get());
            IronsSpellbooks.LOGGER.debug("setPlacedBy {} {}", pPos, blockpos);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        //TODO: this might need to deal with the double block
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        //TODO: this might need to deal with the double block
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    /* BLOCK ENTITY */

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof InscriptionTableTile) {
                ((InscriptionTableTile) blockEntity).drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level pLevel, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pos);
            if (entity instanceof InscriptionTableTile) {
                NetworkHooks.openScreen(((ServerPlayer) player), (InscriptionTableTile) entity, pos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new InscriptionTableTile(pos, state);
    }
}