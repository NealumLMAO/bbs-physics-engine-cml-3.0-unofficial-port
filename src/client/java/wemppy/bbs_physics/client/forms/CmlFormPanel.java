package wemppy.bbs_physics.client.forms;

import mchorse.bbs_mod.forms.forms.Form;
import mchorse.bbs_mod.l10n.keys.IKey;
import mchorse.bbs_mod.ui.forms.editors.forms.UIForm;
import mchorse.bbs_mod.ui.forms.editors.panels.UIFormPanel;
import mchorse.bbs_mod.ui.framework.elements.UISection;
import mchorse.bbs_mod.ui.utils.BoneSelection;

/** Shared CML panel behavior, including selection and persistent section identities. */
public abstract class CmlFormPanel extends UIFormPanel<Form> implements mchorse.bbs_mod.ui.utils.IBoneSelectionHost
{
    private final BoneSelection selection = new BoneSelection();
    public CmlFormPanel(UIForm editor) { super(editor); }
    @Override public BoneSelection getBoneSelection() { return this.editor instanceof mchorse.bbs_mod.ui.utils.IBoneSelectionHost host ? host.getBoneSelection() : this.selection; }
    public BoneSelection boneSelection() { return this.editor instanceof mchorse.bbs_mod.ui.utils.IBoneSelectionHost host ? host.getBoneSelection() : this.selection; }
    public UISection section(IKey title, String id, boolean expanded)
    {
        UISection section = new UISection(title).stateId("bbs_physics." + id);
        section.setExpanded(expanded);
        return section;
    }
    public boolean pickBoneInList(String bone) { return false; }
    @Override public void pickBone(String bone) { this.pickBoneInList(bone); }
    protected float getDefaultOptionsWidth() { return 0.3F; }
}