package noppes.npcs.mixinutil;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

public interface IPositionedSoundRecordAccessor {
	PositionedSoundRecord generateRecordWithVolumeAndPitch(ResourceLocation soundResource, float volume, float pitch);

}
