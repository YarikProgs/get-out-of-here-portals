package net.aros.portals.mixin;

import net.aros.portals.Portals;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.world.dimension.AreaHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AreaHelper.class)
public class NetherPortalMixin {
    @Inject(method = "createPortal", at = @At("HEAD"), cancellable = true)
    private void shouldLightPortalAt(CallbackInfo ci) {
        if (Portals.CONFIG.main.disableNetherPortals) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client != null && client.player != null)
                client.player.sendMessage(Text.literal(Portals.CONFIG.messages.netherPortalMessage));
            ci.cancel();
        }
    }
}
