package noppes.npcs.mixin;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.audio.SoundManager;
import noppes.npcs.mixinutil.ISoundManagerAccessor;
import paulscode.sound.SoundSystem;
import net.minecraft.client.audio.ISound;

@SideOnly(Side.CLIENT)
@Mixin(SoundManager.class)
public abstract class MixinSoundManager implements ISoundManagerAccessor {
	    
    @Shadow
    private boolean loaded;
    @Shadow
    private Map<ISound, String> invPlayingSounds;
	

    public void fadeOut(ISound sound, String filename, long millis) {
        
        if (this.loaded) {
            String sourcename = this.invPlayingSounds.get(sound);

            if (sourcename != null) {
                try {
                    // 使用反射获取 SoundManager 类中的 sndSystem 字段
                	Field sndSystemField = SoundManager.class.getDeclaredField("field_148620_e");
                    sndSystemField.setAccessible(true);
                    SoundSystem sndSystem = (SoundSystem) sndSystemField.get(this);

                    // 调用 fadeOut 方法
                    sndSystem.fadeOut(sourcename, filename, millis);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        

    
    }
    
    
}
