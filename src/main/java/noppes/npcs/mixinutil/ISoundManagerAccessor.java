package noppes.npcs.mixinutil;

import net.minecraft.client.audio.ISound;

public interface ISoundManagerAccessor {
	void fadeOut(ISound sound, String filename, long millis);

}
