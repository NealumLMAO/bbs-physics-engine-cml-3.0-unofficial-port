package wemppy.bbs_physics.client.clips;

import com.mojang.blaze3d.systems.RenderSystem;
import mchorse.bbs_mod.ui.framework.elements.UIElement;
import mchorse.bbs_mod.film.FilmControllerContext;
import mchorse.bbs_mod.utils.Pair;
import mchorse.bbs_mod.ui.framework.elements.input.drag.TransformSpace;
import mchorse.bbs_mod.ui.film.controller.UIFilmController;
import mchorse.bbs_mod.ui.framework.UIContext;
import mchorse.bbs_mod.ui.framework.elements.utils.StencilMap;
import mchorse.bbs_mod.ui.utils.Gizmo;
import mchorse.bbs_mod.ui.utils.GizmoDrag;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.util.math.MatrixStack;
import wemppy.bbs_physics.actions.ImpulseActionClip;

/** Move and radius tools share the film controller's lifecycle and undo context. */
public final class PhysicsFilmTool extends UIElement
{
    private final UIFilmController controller;
    private final ImpulseGizmo gizmo;
    private final ImpulseRadiusHandle radius;

    public PhysicsFilmTool(UIFilmController controller)
    {
        this.controller = controller;
        this.gizmo = new ImpulseGizmo(controller.panel);
        this.radius = new ImpulseRadiusHandle(controller);
        this.add(this.gizmo, this.radius);
        this.setVisible(false);
    }

    public Pair<String, TransformSpace> target()
    {
        ImpulseActionClip clip = ImpulseGizmo.selected(this.controller.panel);
        return clip == null ? null : this.gizmo.select(clip);
    }

    public Boolean startGizmo(UIContext context, int stencilIndex)
    {
        ImpulseActionClip clip = ImpulseGizmo.selected(this.controller.panel);
        if (clip == null) return null;
        this.gizmo.select(clip);
        var camera = new mchorse.bbs_mod.camera.Camera();
        this.controller.panel.copyViewportCamera(camera);
        GizmoDrag drag = GizmoDrag.fromRenderedGizmo(camera, this.controller.panel.preview.getViewport());
        return !this.controller.panel.isFlying() && drag != null
            && Gizmo.INSTANCE.start(stencilIndex, context.mouseX, context.mouseY, this.gizmo, drag) && this.gizmo.isEditing();
    }

    public boolean click(UIContext context) { return this.radius.click(context); }
    public boolean release(UIContext context) { return this.radius.release(context); }
    public boolean key(UIContext context) { return this.radius.key(context); }
    public void updateTool(UIContext context) { this.radius.update(context); }
    public void renderTool(WorldRenderContext context) { this.radius.render(context); }
    public void stopTool() { this.radius.finish(false); }

    public static boolean drawGizmo(FilmControllerContext context, StencilMap stencil, MatrixStack stack)
    {
        String target = context.bone;
        if (target == null || !target.startsWith(ImpulseGizmo.TARGET_PREFIX) || context.replay == null) return false;
        for (ImpulseActionClip clip : context.replay.actions.getClips(ImpulseActionClip.class))
        {
            if (!target.equals(ImpulseGizmo.TARGET_PREFIX + clip.getId())) continue;
            var point = clip.point.get();
            stack.push();
            try
            {
                if (stencil == null) Gizmo.bakeModelViewForCapture(stack.peek().getPositionMatrix());
                stack.translate(point.x - context.camera.getPos().x, point.y - context.camera.getPos().y, point.z - context.camera.getPos().z);
                if (stencil == null) Gizmo.INSTANCE.captureVisual(stack, ImpulseGizmo.MASK);
                else Gizmo.INSTANCE.renderStencil(stack, stencil, ImpulseGizmo.MASK);
            }
            finally { stack.pop(); }
            RenderSystem.enableDepthTest();
            return true;
        }
        return true;
    }
}
