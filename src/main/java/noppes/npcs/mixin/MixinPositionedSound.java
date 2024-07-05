package noppes.npcs.mixin;

import net.minecraft.client.audio.PositionedSound;
import noppes.npcs.mixinutil.IPositionedSoundAccessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mixin(PositionedSound.class)
public abstract class MixinPositionedSound implements IPositionedSoundAccessor{

    @Shadow
    protected float xPosF;

    @Shadow
    protected float yPosF;

    @Shadow
    protected float zPosF;

    public void setXPosF(float xPosF) {
        this.xPosF = xPosF;
    }

    public void setYPosF(float yPosF) {
        this.yPosF = yPosF;
    }

    public void setZPosF(float zPosF) {
        this.zPosF = zPosF;
    }
}
