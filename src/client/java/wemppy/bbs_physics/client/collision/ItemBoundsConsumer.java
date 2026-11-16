package wemppy.bbs_physics.client.collision;

import net.minecraft.client.render.VertexConsumer;
import org.joml.Vector3f;

/** Measures the transformed geometry emitted by every layer of a resolved item model. */
final class ItemBoundsConsumer implements VertexConsumer
{
    final Vector3f min = new Vector3f(Float.POSITIVE_INFINITY);
    final Vector3f max = new Vector3f(Float.NEGATIVE_INFINITY);

    boolean hasVertices()
    {
        return this.min.isFinite() && this.max.isFinite();
    }

    @Override
    public VertexConsumer vertex(float x, float y, float z)
    {
        this.min.min(new Vector3f(x, y, z));
        this.max.max(new Vector3f(x, y, z));
        return this;
    }

    @Override
    public VertexConsumer color(int red, int green, int blue, int alpha) { return this; }

    @Override
    public VertexConsumer texture(float u, float v) { return this; }

    @Override
    public VertexConsumer overlay(int u, int v) { return this; }

    @Override
    public VertexConsumer light(int u, int v) { return this; }

    @Override
    public VertexConsumer normal(float x, float y, float z) { return this; }
}
