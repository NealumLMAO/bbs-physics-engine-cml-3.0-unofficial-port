package wemppy.bbs_physics.mixin.client;
import mchorse.bbs_mod.cubic.render.ModelPivotFrames;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import wemppy.bbs_physics.client.ragdoll.RagdollPoseApplier;
@Mixin(ModelPivotFrames.class)
public class PivotOffsetMixin
{
    @ModifyVariable(method = "collect(Lmchorse/bbs_mod/cubic/IModel;Ljava/util/Set;Ljava/util/Map;Lorg/joml/Matrix4f;Z)V", at = @At("HEAD"), argsOnly = true)
    private static boolean bbs_physics$offsets(boolean stretch) { return stretch || RagdollPoseApplier.isChainStretch(); }
}