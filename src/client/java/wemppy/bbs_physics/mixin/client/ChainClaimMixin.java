package wemppy.bbs_physics.mixin.client;
import mchorse.bbs_mod.cubic.IModel;
import mchorse.bbs_mod.forms.forms.ModelForm;
import mchorse.bbs_mod.forms.forms.utils.FormBone;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import wemppy.bbs_physics.client.chain.ChainMute;

@Mixin(targets = "mchorse.bbs_mod.cubic.physics.ModelPhysicsCache")
public class ChainClaimMixin
{
    @Redirect(method = "compileFresh", at = @At(value = "INVOKE", target = "Lmchorse/bbs_mod/forms/forms/utils/FormBone;hasPhysicsChain()Z"))
    private static boolean bbs_physics$claim(FormBone bone, IModel model, ModelForm form)
    { return bone.hasPhysicsChain() && !ChainMute.claims(form, bone.getId(), bone.physicsEnd.get(), null); }
}