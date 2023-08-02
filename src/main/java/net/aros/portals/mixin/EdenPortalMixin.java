package net.aros.portals.mixin;

import net.aros.portals.Portals;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.edenring.world.EdenPortal;

@Mixin(EdenPortal.class)
public class EdenPortalMixin {
    @Inject(method = "checkNewPortal", at = @At("HEAD"), cancellable = true)
    private static void checkNewPortal(World level, BlockPos center, CallbackInfoReturnable<Boolean> cir) {
        if (Portals.CONFIG.main.disableEdenPortals) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client != null && client.player != null && !level.isClient) {
                client.player.sendMessage(Text.literal(Portals.CONFIG.messages.edenPortalMessage));
            }
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
