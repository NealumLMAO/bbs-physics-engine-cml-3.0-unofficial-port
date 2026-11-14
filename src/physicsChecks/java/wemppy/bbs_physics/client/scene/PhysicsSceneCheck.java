package wemppy.bbs_physics.client.scene;

import mchorse.bbs_mod.BBSSettings;

import mchorse.bbs_mod.film.BaseFilmController;
import mchorse.bbs_mod.film.Film;
import mchorse.bbs_mod.forms.forms.Form;

import mchorse.bbs_mod.settings.values.numeric.ValueBoolean;
import mchorse.bbs_mod.settings.values.numeric.ValueInt;
import wemppy.bbs_physics.BBSPhysicsSettings;
import wemppy.bbs_physics.chain.ChainForm;
import wemppy.bbs_physics.engine.JoltEngine;
import wemppy.bbs_physics.forms.FormTreeWalk;
import wemppy.bbs_physics.forms.PhysicsForms;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Runs without a Minecraft client or native physics world. */
public final class PhysicsSceneCheck
{
    public static void main(String[] args) throws Exception
    {
        BBSSettings.recordingTransformOverlays = new ValueInt("overlays", 0);
        BBSPhysicsSettings.enabled = new ValueBoolean("enabled", true);
        BBSPhysicsSettings.debug = new ValueBoolean("debug", true);
        Film film = null;
        Controller controller = new Controller(film);
        FilmScenes.onSetup(controller);

        for (int tick = 0; tick < 100; tick++)
        {
            FilmScenes.onTick(controller, tick);
            FilmScenes.onRender(controller, null);
        }

        require(empty().contains(controller), "Empty film must stay dormant");
        require(FilmScenes.getSceneCount() == 0 && FilmScenes.getStatus(controller) == null,
            "Empty film must not have a simulation or cache status");
        require(!attemptedJolt(), "Empty film must not initialize Jolt, even with debug enabled");

        Form root = new Form() {};
        checkPoseSampling(root);
        require(PhysicsForms.isSimulatedTree(new ChainForm()), "Rope must be detected as physics");
        require(!PhysicsForms.isSimulatedTree(new Form() {}), "Plain form must not be simulated");
        int[] visits = {0};
        require(FormTreeWalk.any(root, form -> { visits[0]++; return true; }) && visits[0] == 1,
            "Presence search must stop at its first match");

        FilmScenes.onSetup(controller);
        require(!empty().contains(controller), "Cast setup must allow a fresh demand check");
        FilmScenes.onTick(controller, 100);
        FilmScenes.onShutdown(controller);
        require(empty().isEmpty(), "Shutdown must release dormant controllers");

        require(!SceneEdits.matters(List.of("replays", "actor", "form", "color_overlay")), "Overlay is cosmetic");
        require(!SceneEdits.matters(List.of("replays", "actor", "properties", "nested/render_layer", "0")), "Layer track is cosmetic");
        require(SceneEdits.matters(List.of("replays", "actor", "form", "visible")), "Visibility affects collisions");
        require(SceneEdits.matters(List.of("replays", "actor", "properties", "nested/transform", "0")), "Transform affects physics");
        FilmScenes.clear();
        System.out.println("PhysicsSceneCheck passed: absent films, form presence, lifecycle cleanup and edit filtering.");
    }

    /** CML has no channel epoch cache; every integer sample must update the runtime value. */
    private static void checkPoseSampling(Form form)
    {
        var knob = new mchorse.bbs_mod.settings.values.numeric.ValueFloat("physics_sample", 0F, 0F, 20F);
        form.add(knob);
        var properties = new mchorse.bbs_mod.film.replays.FormProperties("properties");
        var channel = properties.getOrCreate(form, "physics_sample");
        require(channel != null, "CML must expose visible numeric form values as tracks");
        channel.insert(0F, 0F);
        channel.insert(10F, 10F);
        for (int tick : new int[]{0, 10, 0, 10})
        {
            properties.applyProperties(form, tick);
            require(knob.get() == (float) tick, "Scrubbing must resample the numeric track");
        }
        require(knob.getOriginalValue() == 0F, "Sampling must preserve authored values");
    }
    private static Set<?> empty() throws Exception
    {
        Field field = FilmScenes.class.getDeclaredField("EMPTY");
        field.setAccessible(true);
        return (Set<?>) field.get(null);
    }

    private static boolean attemptedJolt() throws Exception
    {
        Field field = JoltEngine.class.getDeclaredField("attempted");
        field.setAccessible(true);
        return field.getBoolean(null);
    }

    private static void require(boolean condition, String message)
    {
        if (!condition) throw new AssertionError(message);
    }

    private static final class Controller extends BaseFilmController
    {
        Controller(Film film) { super(film); }
        @Override public Map<String, Integer> getActors() { return Map.of(); }
        @Override public int getTick() { return 0; }
    }
}
