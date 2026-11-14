package wemppy.bbs_physics.client.forms;
import mchorse.bbs_mod.ui.framework.elements.UIElement;
public final class CmlUi
{
    public static <T> T ancestor(UIElement child, Class<T> type)
    {
        for (UIElement parent = child.getParent(); parent != null; parent = parent.getParent())
            if (type.isInstance(parent)) return type.cast(parent);
        return null;
    }
    private CmlUi() {}
}