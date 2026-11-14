package wemppy.bbs_physics.checks;

import mchorse.bbs_mod.BBSMod;
import mchorse.bbs_mod.BBSModClient;
import mchorse.bbs_mod.film.Film;
import mchorse.bbs_mod.film.replays.FormProperties;
import mchorse.bbs_mod.forms.FormUtilsClient;
import mchorse.bbs_mod.forms.forms.*;
import mchorse.bbs_mod.ui.forms.UIFormPalette;
import mchorse.bbs_mod.ui.forms.editors.UIFormEditor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import wemppy.bbs_physics.BBSPhysics;
import wemppy.bbs_physics.balloon.BalloonForm;
import wemppy.bbs_physics.chain.ChainForm;
import wemppy.bbs_physics.cloth.ClothForm;
import wemppy.bbs_physics.engine.JoltEngine;
import wemppy.bbs_physics.forms.*;
import wemppy.bbs_physics.structure.IStructurePhysicsForm;

/** Runs against real Fabric mixins, registries, OpenGL, and Jolt after Minecraft starts. */
public class CmlClientCheck implements ClientModInitializer
{
    @Override public void onInitializeClient()
    {
        ClientLifecycleEvents.CLIENT_STARTED.register(client ->
        {
            try
            {
                for (String target : new String[]{
                    "mchorse.bbs_mod.ui.film.ClipTimelineActions",
                    "mchorse.bbs_mod.ui.film.controller.UIFilmController",
                    "mchorse.bbs_mod.ui.film.replays.UIReplaysEditor",
                    "mchorse.bbs_mod.ui.forms.editors.FormEditorGizmoController",
                    "mchorse.bbs_mod.forms.renderers.StructureFormRenderer",
                    "mchorse.bbs_mod.forms.renderers.ModelFormRenderer",
                    "mchorse.bbs_mod.ui.forms.editors.utils.UIPickableFormRenderer",
                    "mchorse.bbs_mod.cubic.physics.ModelPhysicsRuntime",
                    "mchorse.bbs_mod.cubic.physics.ModelPhysicsCache",
                    "mchorse.bbs_mod.cubic.render.ModelPivotFrames",
                    "mchorse.bbs_mod.ui.film.controller.FilmPickingController",
                    "mchorse.bbs_mod.ui.film.utils.keyframes.UIFilmKeyframes"})
                    Class.forName(target);
                require(JoltEngine.available(), "Jolt native initialization");
                var model = new ModelForm();
                require(model instanceof IModelPhysicsForm, "Model mixin");
                var structure = new StructureForm();
                require(structure instanceof IStructurePhysicsForm, "Structure mixin");
                Form[] forms = {new BlockForm(), model, structure, new ClothForm(), new BalloonForm(), new ChainForm()};
                UIFormPalette palette = new UIFormPalette(form -> {});
                for (Form form : forms)
                {
                    require(form instanceof IPhysicsForm, "Base form mixin");
                    PhysicsForms.setType(form, PhysicsType.BODY);
                    var physics = (IPhysicsForm) form;
                    physics.bbs_physics$getBodyKnob(BodyKnob.MASS).set(12F);
                    PhysicsForms.setAuthority(form, 0.25F);
                    var copy = BBSMod.getForms().fromData(form.toData().asMap());
                    require(copy != null && PhysicsForms.getBody(copy).mass() == 12F, "Physics serialization: " + form.getClass());
                    require(PhysicsForms.getAuthority(copy) == 0.25F, "Authority serialization");
                    FormProperties tracks = new FormProperties("properties");
                    var mass = tracks.getOrCreate(form, BodyKnob.MASS.id);
                    require(mass != null, "CML numeric track registration");
                    mass.insert(0F, 2F);
                    mass.insert(10F, 8F);
                    tracks.applyProperties(form, 10);
                    require(PhysicsForms.getBody(form).mass() == 8F, "Keyframed physics value");
                    tracks.applyProperties(form, 0);
                    require(PhysicsForms.getBody(form).mass() == 2F, "Physics track rewind");
                    require(physics.bbs_physics$getBodyKnob(BodyKnob.MASS).getOriginalValue() == 12F, "Authored value preservation");
                    var editor = UIFormEditor.createPanel(form);
                    require(editor != null, "Editor registration");
                    editor.setEditor(palette.editor);
                    editor.startEdit(form);
                    require(FormUtilsClient.getRenderer(form) != null, "Renderer registration");
                    editor.finishEdit();
                }
                checkCast();
                BBSPhysics.LOGGER.info("CML_CLIENT_CHECK PASSED: all integration targets, six form editors/renderers, serialization, keyframes, indexed replay sampling, and Jolt.");
            }
            catch (Throwable failure)
            {
                BBSPhysics.LOGGER.error("CML_CLIENT_CHECK FAILED", failure);
                System.exit(1);
            }
            // Let CML's asynchronous model/texture loads finish before closing Minecraft.
            int[] remaining = {100};
            net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK.register(ticking ->
            { if (--remaining[0] == 0) ticking.scheduleStop(); });
        });
    }
    private static void checkCast()
    {
        var film = new Film();
        var firstReplay = film.replays.addReplay();
        var secondReplay = film.replays.addReplay();
        firstReplay.stableId.set("not-an-index-a");
        secondReplay.stableId.set("not-an-index-b");
        var first = new mchorse.bbs_mod.forms.entities.StubEntity();
        var second = new mchorse.bbs_mod.forms.entities.StubEntity();
        first.setForm(new BlockForm());
        second.setForm(new BlockForm());
        var controller = new mchorse.bbs_mod.film.BaseFilmController(film)
        {
            public java.util.Map<String, Integer> getActors() { return java.util.Map.of(); }
            public int getTick() { return 0; }
        };
        controller.getEntities().put(0, first);
        controller.getEntities().put(1, second);
        var cast = new wemppy.bbs_physics.client.scene.SceneCast(controller);
        require(cast.first() == first && cast.find(secondReplay).entity == second, "CML replay index mapping");
        firstReplay.keyframes.x.insert(0F, 1D);
        firstReplay.keyframes.x.insert(10F, 9D);
        cast.borrow();
        cast.apply(10);
        require(first.getX() == 9D, "Integer tick sampling");
        cast.apply(0);
        require(first.getX() == 1D, "Replay rewind");
        cast.restore(0);
        require(first.getX() == 0D, "Borrowed placement restoration");
    }
    private static void require(boolean condition, String message)
    { if (!condition) throw new AssertionError(message); }
}