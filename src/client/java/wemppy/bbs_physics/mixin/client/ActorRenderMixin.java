package wemppy.bbs_physics.mixin.client;
import mchorse.bbs_mod.film.BaseFilmController;
import mchorse.bbs_mod.film.FilmControllerContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wemppy.bbs_physics.client.scene.SceneActor;
import wemppy.bbs_physics.client.clips.PhysicsFilmTool;
@Mixin(BaseFilmController.class)
public class ActorRenderMixin
{
    @Inject(method = "renderEntity(Lmchorse/bbs_mod/film/FilmControllerContext;)V", at = @At("HEAD"), cancellable = true)
    private static void bbs_physics$prepare(FilmControllerContext context, CallbackInfo ci)
    {
        if (PhysicsFilmTool.drawGizmo(context, context.map, context.stack)) { ci.cancel(); return; }
        SceneActor.prepareRender(context.entity, context.entities, context.transition, context.replay == null || !context.relative);
    }
}