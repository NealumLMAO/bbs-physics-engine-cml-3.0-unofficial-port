package wemppy.bbs_physics.client.structure;
import mchorse.bbs_mod.forms.forms.StructureForm;
import mchorse.bbs_mod.forms.renderers.StructureFormRenderer;
import mchorse.bbs_mod.forms.renderers.FormRenderingContext;
import mchorse.bbs_mod.forms.structure.StructureRenderData;
import mchorse.bbs_mod.utils.MatrixStackUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/** Reuses CML's native structure renderer for lighting, fluids, block entities, and soft opacity. */
public final class StructureRenderPart
{
    private final CmlStructureRenderer renderer;
    public StructureRenderPart(StructureForm form, StructureRenderData data, String biome, Vector3f offset)
    {
        this.renderer = (CmlStructureRenderer) new StructureFormRenderer(form);
        this.renderer.bbs_physics$setPart(data, biome, offset);
    }
    public void render(FormRenderingContext context, Matrix4f transform)
    {
        context.stack.push();
        try
        {
            if (transform != null) MatrixStackUtils.multiply(context.stack, transform);
            this.renderer.bbs_physics$drawPart(context);
        }
        finally { context.stack.pop(); }
    }
}