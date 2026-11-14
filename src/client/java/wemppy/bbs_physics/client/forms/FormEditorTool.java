package wemppy.bbs_physics.client.forms;
import mchorse.bbs_mod.ui.framework.elements.input.UIPropTransform;
import mchorse.bbs_mod.ui.framework.elements.input.drag.TransformSpace;
import org.joml.Matrix4f;
public interface FormEditorTool
{
    UIPropTransform getGizmoTransform();
    Matrix4f getGizmoOrigin(float transition, TransformSpace space);
}