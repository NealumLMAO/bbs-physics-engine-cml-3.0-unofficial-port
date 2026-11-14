package wemppy.bbs_physics.mixin.client;
import mchorse.bbs_mod.ui.forms.editors.forms.UIForm;
import mchorse.bbs_mod.ui.framework.elements.input.UIPropTransform;
import mchorse.bbs_mod.ui.framework.elements.input.drag.TransformSpace;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import wemppy.bbs_physics.client.forms.FormEditorTool;
@Mixin(UIForm.class)
public class FormToolsMixin implements mchorse.bbs_mod.ui.utils.IBoneSelectionHost
{
    @Unique private final mchorse.bbs_mod.ui.utils.BoneSelection bbs_physics$selection = new mchorse.bbs_mod.ui.utils.BoneSelection();
    public mchorse.bbs_mod.ui.utils.BoneSelection getBoneSelection() { return this.bbs_physics$selection; }
    @Unique private FormEditorTool bbs_physics$active()
    {
        var view = ((UIForm) (Object) this).view;
        return view instanceof FormEditorTool tool && tool.getGizmoTransform() != null ? tool : null;
    }
    @Inject(method = "getEditableTransform", at = @At("HEAD"), cancellable = true)
    private void bbs_physics$transform(CallbackInfoReturnable<UIPropTransform> ci)
    { var tool = this.bbs_physics$active(); if (tool != null) ci.setReturnValue(tool.getGizmoTransform()); }
    @Inject(method = "getOrigin(F)Lorg/joml/Matrix4f;", at = @At("HEAD"), cancellable = true)
    private void bbs_physics$origin(float transition, CallbackInfoReturnable<Matrix4f> ci)
    { var tool = this.bbs_physics$active(); if (tool != null) ci.setReturnValue(tool.getGizmoOrigin(transition, tool.getGizmoTransform().getSpace())); }
    @Inject(method = "getOriginMatrix", at = @At("HEAD"), cancellable = true)
    private void bbs_physics$own(float transition, CallbackInfoReturnable<Matrix4f> ci)
    { var tool = this.bbs_physics$active(); if (tool != null) ci.setReturnValue(tool.getGizmoOrigin(transition, TransformSpace.LOCAL)); }
    @Inject(method = "getParentOriginMatrix", at = @At("HEAD"), cancellable = true)
    private void bbs_physics$parent(float transition, CallbackInfoReturnable<Matrix4f> ci)
    { var tool = this.bbs_physics$active(); if (tool != null) ci.setReturnValue(tool.getGizmoOrigin(transition, TransformSpace.PARENT)); }
    @Inject(method = "getGizmoSpace", at = @At("HEAD"), cancellable = true)
    private void bbs_physics$space(CallbackInfoReturnable<TransformSpace> ci)
    { var tool = this.bbs_physics$active(); if (tool != null) ci.setReturnValue(tool.getGizmoTransform().getSpace()); }
}