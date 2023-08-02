package net.aros.portals.mixin;

import net.aros.portals.Portals;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderEyeItem.class)
public class EnderEyeMixin {
    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    private void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (Portals.CONFIG.main.disableEndPortals) {
            World world = context.getWorld();
            BlockPos blockPos = context.getBlockPos();
            BlockState blockState = context.getWorld().getBlockState(blockPos);

            if (world.isClient) {
                cir.setReturnValue(ActionResult.SUCCESS);
            } else {
                BlockState blockState2 = blockState.with(EndPortalFrameBlock.EYE, true);

                Block.pushEntitiesUpBeforeBlockChange(blockState, blockState2, world, blockPos);
                world.setBlockState(blockPos, blockState2, Block.NOTIFY_LISTENERS);
                world.updateComparators(blockPos, Blocks.END_PORTAL_FRAME);
                context.getStack().decrement(1);

                world.syncWorldEvent(WorldEvents.END_PORTAL_FRAME_FILLED, blockPos, 0);
                BlockPattern.Result result = EndPortalFrameBlock.getCompletedFramePattern().searchAround(world, blockPos);

                if (result != null) {
                    context.getPlayer().sendMessage(Text.literal(Portals.CONFIG.messages.endPortalMessage));
                }

                cir.setReturnValue(ActionResult.CONSUME);
            }
            cir.cancel();
        }
    }
}
