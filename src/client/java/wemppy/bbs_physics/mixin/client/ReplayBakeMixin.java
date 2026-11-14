package wemppy.bbs_physics.mixin.client;
import mchorse.bbs_mod.ui.film.UIFilmPanel;
import mchorse.bbs_mod.ui.film.replays.UIReplaysEditor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wemppy.bbs_physics.client.scene.FilmBake;
@Mixin(UIReplaysEditor.class)
public class ReplayBakeMixin
{
    @Inject(method = "<init>", at = @At("TAIL"))
    private void bbs_physics$bake(UIFilmPanel panel, CallbackInfo ci)
    {
        UIReplaysEditor self = (UIReplaysEditor) (Object) this;
        self.replayProperties.properties.add(FilmBake.button(panel, self::getReplay));
    }
}