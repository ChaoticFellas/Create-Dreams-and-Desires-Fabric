package uwu.lopyluna.create_dd.block.BlockProperties.copycat;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.decoration.copycat.CopycatSpecialCases;
import com.simibubi.create.foundation.model.BakedModelHelper;
import com.simibubi.create.foundation.model.BakedQuadHelper;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import java.util.Random;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.data.IModelData;

import java.util.ArrayList;
import java.util.List;

public class BlockcopycatSlabModel extends com.simibubi.create.content.decoration.copycat.CopycatModel {

    protected static final AABB CUBE_AABB = new AABB(BlockPos.ZERO);

    public BlockcopycatSlabModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    protected List<BakedQuad> getCroppedQuads(BlockState state, Direction side, Random rand, BlockState material,
                                              IModelData wrappedData) {
        Direction facing = state.getOptionalValue(BlockcopycatSlab.FACING)
                .orElse(Direction.UP);
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance()
                .getBlockRenderer();

        BlockState specialCopycatModelState = null;
        if (CopycatSpecialCases.isBarsMaterial(material))
            specialCopycatModelState = AllBlocks.COPYCAT_BARS.getDefaultState();
        if (CopycatSpecialCases.isTrapdoorMaterial(material))
            return blockRenderer.getBlockModel(material)
                    .getQuads(state, side, rand, wrappedData);

        if (specialCopycatModelState != null) {
            BakedModel blockModel = blockRenderer
                    .getBlockModel(specialCopycatModelState.setValue(DirectionalBlock.FACING, facing));
            if (blockModel instanceof CopycatModel cm)
                return cm.getCroppedQuads(state, side, rand, material, wrappedData);
        }

        BakedModel model = getModelOf(material);
        List<BakedQuad> templateQuads = model.getQuads(material, side, rand, wrappedData);
        int size = templateQuads.size();

        List<BakedQuad> quads = new ArrayList<>();

        Vec3 normal = Vec3.atLowerCornerOf(facing.getNormal());
        Vec3 normalScaled8 = normal.scale(12 / 16f);

        // 2 Pieces
        for (boolean front : Iterate.trueAndFalse) {
            Vec3 normalScaledN13 = normal.scale(front ? 0 : -8 / 16f);
            float contract = 16 - (front ? 4 : 4);
            AABB bb = CUBE_AABB.contract(normal.x * contract / 16, normal.y * contract / 16, normal.z * contract / 16);
            if (!front)
                bb = bb.move(normalScaled8);

            for (int i = 0; i < size; i++) {
                BakedQuad quad = templateQuads.get(i);
                Direction direction = quad.getDirection();

                if (front && direction == facing)
                    continue;
                if (!front && direction == facing.getOpposite())
                    continue;

                quads.add(BakedQuadHelper.cloneWithCustomGeometry(quad,
                        BakedModelHelper.cropAndMove(quad.getVertices(), quad.getSprite(), bb, normalScaledN13)));
            }

        }

        return quads;
    }

}
