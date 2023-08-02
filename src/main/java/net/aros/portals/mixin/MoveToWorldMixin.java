package net.aros.portals.mixin;

import net.aros.portals.Portals;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public class MoveToWorldMixin {
    @Inject(method = "moveToWorld", at = @At("HEAD"), cancellable = true)
    private void moveToWorld(ServerWorld destination, CallbackInfoReturnable<Entity> cir) {
        if (Portals.CONFIG.main.blockedDimensions.contains(destination.getDimensionKey().getValue())) {
            PlayerEntity me = ((ServerPlayerEntity) (Object) this);

            me.sendMessage(Text.literal(Portals.CONFIG.messages.blockedDimension));
            cir.setReturnValue(me);
            cir.cancel();
        }
    }
}
