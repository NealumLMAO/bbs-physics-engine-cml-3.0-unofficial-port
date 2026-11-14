package wemppy.bbs_physics.mixin.client;
import mchorse.bbs_mod.ui.film.UIFilmPanel;
import mchorse.bbs_mod.ui.film.controller.UIFilmController;
import mchorse.bbs_mod.ui.framework.UIContext;
import mchorse.bbs_mod.ui.utils.Area;
import mchorse.bbs_mod.utils.Pair;
import mchorse.bbs_mod.ui.framework.elements.input.drag.TransformSpace;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import wemppy.bbs_physics.client.clips.*;
@Mixin(UIFilmController.class)
public class FilmToolsMixin implements CmlFilmTools
{
    @Unique private PhysicsFilmTool bbs_physics$tool;
    public PhysicsFilmTool bbs_physics$tool() { return this.bbs_physics$tool; }
    @Inject(method = "<init>", at = @At("TAIL"))
    private void bbs_physics$attach(UIFilmPanel panel, CallbackInfo ci)
    {
        UIFilmController self = (UIFilmController) (Object) this;
        this.bbs_physics$tool = new PhysicsFilmTool(self);
        self.add(this.bbs_physics$tool);
    }
    @Inject(method = "getBone", at = @At("HEAD"), cancellable = true)
    private void bbs_physics$target(CallbackInfoReturnable<Pair<String, TransformSpace>> ci)
    { var target = this.bbs_physics$tool.target(); if (target != null) ci.setReturnValue(target); }
    @Inject(method = "isReplayGizmoActive", at = @At("HEAD"), cancellable = true)
    private void bbs_physics$customGizmo(CallbackInfoReturnable<Boolean> ci)
    { if (this.bbs_physics$tool.target() != null) ci.setReturnValue(false); }
    @Inject(method = "subMouseClicked", at = @At("HEAD"), cancellable = true)
    private void bbs_physics$click(UIContext context, CallbackInfoReturnable<Boolean> ci)
    { if (this.bbs_physics$tool.click(context)) ci.setReturnValue(true); }
    @Inject(method = "subMouseReleased", at = @At("HEAD"), cancellable = true)
    private void bbs_physics$release(UIContext context, CallbackInfoReturnable<Boolean> ci)
    { if (this.bbs_physics$tool.release(context)) ci.setReturnValue(true); }
    @Inject(method = "subKeyPressed", at = @At("HEAD"), cancellable = true)
    private void bbs_physics$key(UIContext context, CallbackInfoReturnable<Boolean> ci)
    { if (this.bbs_physics$tool.key(context)) ci.setReturnValue(true); }
    @Inject(method = "renderFrame", at = @At("TAIL"))
    private void bbs_physics$render(WorldRenderContext context, CallbackInfo ci) { this.bbs_physics$tool.renderTool(context); }
    @Inject(method = "renderHUD", at = @At("HEAD"))
    private void bbs_physics$update(UIContext context, Area area, CallbackInfo ci) { this.bbs_physics$tool.updateTool(context); }
}