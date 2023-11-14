package uwu.lopyluna.create_dd.item.ItemProperties.compound;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RefinedRadiance extends NoGravMagical {

    public RefinedRadiance(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    protected void onCreated(ItemEntity entity, CompoundTag persistentData) {
        super.onCreated(entity, persistentData);
        entity.setDeltaMovement(entity.getDeltaMovement()
                .add(0, .25f, 0));
    }
}
