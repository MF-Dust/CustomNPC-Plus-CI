package noppes.npcs.mixin;

//引入相关的 Mixin 类
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import noppes.npcs.mixinutil.ISoundHandlerAccessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

//声明 Mixin 类，目标是 SoundHandler
@SideOnly(Side.CLIENT)
@Mixin(SoundHandler.class)
public class MixinSoundHandler implements ISoundHandlerAccessor {

	// 使用 @Shadow 注解来声明和访问 SoundHandler 类中的私有字段 sndManager
	@Shadow
	private SoundManager sndManager;

	// 你可以在这里添加其他方法来操作 sndManager 或者其他逻辑
	public SoundManager getSoundManager() {
		return sndManager;
	}
}
