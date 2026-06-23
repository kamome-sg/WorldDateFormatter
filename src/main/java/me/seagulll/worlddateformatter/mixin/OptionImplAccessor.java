package me.seagulll.worlddateformatter.mixin;

import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.impl.OptionImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(OptionImpl.class)
public interface OptionImplAccessor {
    @Accessor("description")
    void setDescription(OptionDescription description);
}