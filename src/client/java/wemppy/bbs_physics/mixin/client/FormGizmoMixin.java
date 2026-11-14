package wemppy.bbs_physics.mixin.client;
import mchorse.bbs_mod.forms.forms.Form;
import mchorse.bbs_mod.ui.forms.editors.FormEditorGizmoController;
import mchorse.bbs_mod.ui.forms.editors.UIFormEditor;
import mchorse.bbs_mod.ui.framework.elements.input.UIPropTransform;
import mchorse.bbs_mod.ui.framework.elements.input.drag.TransformSpace;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import wemppy.bbs_physics.client.forms.*;

/** CML resolves gizmo targets in a controller rather than through panel extensions. */
@Mixin(FormEditorGizmoController.class)
public class FormGizmoMixin
{
    @Shadow @Final private UIFormEditor editor;
    @Unique private FormEditorTool bbs_physics$tool()
    {
        if (this.editor.statesEditor.isVisible() || this.editor.editor == null) return null;
        var panel = this.editor.editor.view;
        return panel instanceof FormEditorTool tool && tool.getGizmoTransform() != null ? tool : null;
    }
    @Inject(method = "getGizmoDragTransform", at = @At("HEAD"), cancellable = true)
    private void bbs_physics$transform(CallbackInfoReturnable<UIPropTransform> ci)
    { var tool = this.bbs_physics$tool(); if (tool != null) ci.setReturnValue(tool.getGizmoTransform()); }
    @Inject(method = "getOrigin", at = @At("HEAD"), cancellable = true)
    private void bbs_physics$origin(float transition, CallbackInfoReturnable<Matrix4f> ci)
    { var tool = this.bbs_physics$tool(); if (tool != null) ci.setReturnValue(tool.getGizmoOrigin(transition, tool.getGizmoTransform().getSpace())); }
    @Inject(method = "getOriginMatrix", at = @At("HEAD"), cancellable = true)
    private void bbs_physics$own(float transition, CallbackInfoReturnable<Matrix4f> ci)
    { var tool = this.bbs_physics$tool(); if (tool != null) ci.setReturnValue(tool.getGizmoOrigin(transition, TransformSpace.LOCAL)); }
    @Inject(method = "getParentOriginMatrix", at = @At("HEAD"), cancellable = true)
    private void bbs_physics$parent(float transition, CallbackInfoReturnable<Matrix4f> ci)
    { var tool = this.bbs_physics$tool(); if (tool != null) ci.setReturnValue(tool.getGizmoOrigin(transition, TransformSpace.PARENT)); }
    @Inject(method = "getGizmoSpace", at = @At("HEAD"), cancellable = true)
    private void bbs_physics$space(CallbackInfoReturnable<TransformSpace> ci)
    { var tool = this.bbs_physics$tool(); if (tool != null) ci.setReturnValue(tool.getGizmoTransform().getSpace()); }
    @Inject(method = "pickFormBone", at = @At("HEAD"), cancellable = true)
    private void bbs_physics$pick(Form form, String bone, CallbackInfo ci)
    {
        if (this.editor.editor != null && this.editor.editor.form == form
            && this.editor.editor.view instanceof CmlFormPanel panel && panel.pickBoneInList(bone)) ci.cancel();
    }
}