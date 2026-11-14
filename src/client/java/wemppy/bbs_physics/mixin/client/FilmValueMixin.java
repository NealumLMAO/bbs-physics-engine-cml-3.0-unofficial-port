package wemppy.bbs_physics.mixin.client;
import mchorse.bbs_mod.settings.values.base.BaseValue;
import mchorse.bbs_mod.film.Film;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wemppy.bbs_physics.client.scene.FilmScenes;
@Mixin(BaseValue.class)
public class FilmValueMixin
{
    @Inject(method = "postNotify(Lmchorse/bbs_mod/settings/values/base/BaseValue;I)V", at = @At("TAIL"))
    private void bbs_physics$edited(BaseValue value, int flag, CallbackInfo ci)
    { if ((Object) this instanceof Film) FilmScenes.onFilmEdited(value); }
}