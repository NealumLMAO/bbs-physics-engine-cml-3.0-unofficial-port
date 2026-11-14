package wemppy.bbs_physics.mixin.client;
import mchorse.bbs_mod.forms.renderers.ModelFormRenderer;
import mchorse.bbs_mod.forms.entities.IEntity;
import mchorse.bbs_mod.forms.renderers.utils.MatrixCache;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wemppy.bbs_physics.client.ragdoll.RagdollPoseApplier;

@Mixin(ModelFormRenderer.class)
public class ModelMatrixPoseMixin
{
    @Inject(method = "collectMatrices(Lmchorse/bbs_mod/forms/entities/IEntity;Lnet/minecraft/client/util/math/MatrixStack;Lmchorse/bbs_mod/forms/renderers/utils/MatrixCache;Ljava/lang/String;F)V", at = @At("HEAD"))
    private void bbs_physics$parent(IEntity entity, MatrixStack stack, MatrixCache matrices, String prefix, float transition, CallbackInfo ci)
    {
        var form = ((ModelFormRenderer) (Object) this).getForm();
        var state = wemppy.bbs_physics.forms.PhysicsForms.getState(form);
        if (state != null) state.captureWalkParentFrame(stack.peek().getPositionMatrix());
    }
    @Inject(method = "collectMatrices(Lmchorse/bbs_mod/forms/entities/IEntity;Lnet/minecraft/client/util/math/MatrixStack;Lmchorse/bbs_mod/forms/renderers/utils/MatrixCache;Ljava/lang/String;F)V",
        at = @At(value = "INVOKE", target = "Lmchorse/bbs_mod/forms/renderers/model/ModelFormDrawPipeline;captureMatrices(Lmchorse/bbs_mod/cubic/ModelInstance;)V"))
    private void bbs_physics$pose(IEntity entity, MatrixStack stack, MatrixCache matrices, String prefix, float transition, CallbackInfo ci)
    {
        ModelFormRenderer self = (ModelFormRenderer) (Object) this;
        RagdollPoseApplier.apply(self.getForm(), self.getModel(), transition);
    }
}