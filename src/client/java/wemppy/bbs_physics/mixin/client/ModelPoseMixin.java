package wemppy.bbs_physics.mixin.client;
import mchorse.bbs_mod.cubic.ModelInstance;
import mchorse.bbs_mod.cubic.physics.ModelPhysicsRuntime;
import mchorse.bbs_mod.forms.forms.ModelForm;
import mchorse.bbs_mod.forms.entities.IEntity;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wemppy.bbs_physics.client.ragdoll.RagdollPoseApplier;

@Mixin(ModelPhysicsRuntime.class)
public class ModelPoseMixin
{
    @Inject(method = "apply", at = @At("HEAD"))
    private static void bbs_physics$pose(IEntity entity, ModelInstance model, ModelForm form, float transition, Matrix4f base, CallbackInfo ci)
    { if (model != null) RagdollPoseApplier.apply(form, model, transition); }
}