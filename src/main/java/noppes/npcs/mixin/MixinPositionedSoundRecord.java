package noppes.npcs.mixin;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.mixinutil.IPositionedSoundRecordAccessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PositionedSoundRecord.class)
public abstract class MixinPositionedSoundRecord implements IPositionedSoundRecordAccessor{

    @Invoker("<init>")
    private static PositionedSoundRecord invokeConstructor(ResourceLocation soundResource, float volume, float pitch, boolean repeat, int repeatDelay, ISound.AttenuationType attenuationType, float xPos, float yPos, float zPos) {
        throw new AssertionError();
    }

    public PositionedSoundRecord generateRecordWithVolumeAndPitch(ResourceLocation soundResource, float volume, float pitch) {
        return invokeConstructor(soundResource, volume, pitch, false, 0, ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F);
    }
}