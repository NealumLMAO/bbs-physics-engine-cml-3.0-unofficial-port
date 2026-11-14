package wemppy.bbs_physics.client.structure;
import mchorse.bbs_mod.forms.renderers.FormRenderingContext;
import mchorse.bbs_mod.forms.structure.StructureRenderData;
import org.joml.Vector3f;
public interface CmlStructureRenderer
{
    void bbs_physics$setPart(StructureRenderData data, String biome, Vector3f offset);
    void bbs_physics$drawPart(FormRenderingContext context);
}