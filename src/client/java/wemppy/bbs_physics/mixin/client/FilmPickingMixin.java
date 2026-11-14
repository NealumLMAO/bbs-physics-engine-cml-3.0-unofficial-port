package wemppy.bbs_physics.mixin.client;
import mchorse.bbs_mod.ui.film.controller.FilmPickingController;
import mchorse.bbs_mod.ui.framework.UIContext;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import wemppy.bbs_physics.client.clips.CmlFilmTools;
@Mixin(FilmPickingController.class)
public class FilmPickingMixin
{
    @Shadow @Final private FilmPickingController.Host host;
    @Inject(method = "startGizmo", at = @At("HEAD"), cancellable = true)
    private void bbs_physics$start(UIContext context, int stencilIndex, CallbackInfoReturnable<Boolean> ci)
    {
        Boolean started = ((CmlFilmTools) this.host.panel().getController()).bbs_physics$tool().startGizmo(context, stencilIndex);
        if (started != null) ci.setReturnValue(started);
    }
}