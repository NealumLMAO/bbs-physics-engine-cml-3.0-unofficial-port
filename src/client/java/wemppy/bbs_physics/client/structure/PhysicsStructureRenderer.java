package wemppy.bbs_physics.client.structure;

import mchorse.bbs_mod.forms.forms.StructureForm;
import mchorse.bbs_mod.forms.renderers.FormRenderingContext;
import mchorse.bbs_mod.forms.structure.StructureRenderData;
import org.joml.Matrix4f;
import wemppy.bbs_physics.structure.StructureDestruction;

/** Renderer-owned CML debris cache; no global references retain discarded renderers. */
public final class PhysicsStructureRenderer
{
    private DestructionRender cached;
    public boolean render(StructureForm form, StructureRenderData data, FormRenderingContext context)
    {
        var state = StructureDestruction.state(form);
        if (state == null || !StructureDestruction.isEnabled(form) || !state.structure.equals(form.structure.get()))
        {
            this.cached = null;
            return false;
        }
        if (!state.isBroken() || data == null) return false;
        String biome = form.biome.get();
        DestructionRender render = this.cached;
        if (render == null || render.state != state || render.source != data || !render.biome.equals(biome))
            this.cached = render = new DestructionRender(form, state, data, biome);
        var detached = state.detached();
        var remainder = render.remainder(detached);
        if (remainder != null) remainder.renderer.render(context, null);
        Matrix4f matrix = new Matrix4f();
        for (int i = detached.nextSetBit(0); i >= 0; i = detached.nextSetBit(i + 1))
            render.block(i).renderer.render(context, state.transform(i, context.transition, matrix));
        return true;
    }
}