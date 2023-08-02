package net.aros.portals.mixin;

import net.aros.portals.Portals;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndGatewayBlockEntity.class)
public class EndGatewayMixin {
    @Inject(method = "canTeleport", at = @At("HEAD"), cancellable = true)
    private static void canTeleport(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (Portals.CONFIG.main.disableEndGateways) {
            if (entity instanceof PlayerEntity player)
                player.sendMessage(Text.literal(Portals.CONFIG.messages.endGatewayMessage));

            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
