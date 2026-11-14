package wemppy.bbs_physics.mixin.client;
import mchorse.bbs_mod.forms.forms.StructureForm;
import mchorse.bbs_mod.forms.renderers.StructureFormRenderer;
import mchorse.bbs_mod.forms.renderers.FormRenderingContext;
import mchorse.bbs_mod.forms.structure.*;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import wemppy.bbs_physics.client.structure.*;

@Mixin(StructureFormRenderer.class)
public abstract class StructureFormRendererMixin implements CmlStructureRenderer
{
    @Shadow private StructureRenderData data;
    @Shadow private StructureRenderWorld world;
    @Shadow private BakedStructure baked;
    @Shadow private void ensureData() {}
    @Unique private Vector3f bbs_physics$partOffset;
    @Unique private final PhysicsStructureRenderer bbs_physics$destruction = new PhysicsStructureRenderer();
    public void bbs_physics$setPart(StructureRenderData data, String biome, Vector3f offset)
    {
        this.data = data;
        this.world = new StructureRenderWorld(data, biome);
        this.baked = null;
        this.bbs_physics$partOffset = new Vector3f(offset);
    }
    @Invoker("render3D") public abstract void bbs_physics$drawPart(FormRenderingContext context);
    @Inject(method = "ensureData", at = @At("HEAD"), cancellable = true)
    private void bbs_physics$keepPart(CallbackInfo ci) { if (this.bbs_physics$partOffset != null) ci.cancel(); }
    @Inject(method = "getOffset", at = @At("HEAD"), cancellable = true)
    private void bbs_physics$partOffset(CallbackInfoReturnable<Vector3f> ci)
    { if (this.bbs_physics$partOffset != null) ci.setReturnValue(this.bbs_physics$partOffset); }
    @Inject(method = "render3D", at = @At("HEAD"), cancellable = true)
    private void bbs_physics$debris(FormRenderingContext context, CallbackInfo ci)
    {
        if (this.bbs_physics$partOffset != null) return;
        this.ensureData();
        StructureForm form = ((StructureFormRenderer) (Object) this).getForm();
        if (this.bbs_physics$destruction.render(form, this.data, context)) ci.cancel();
    }
}