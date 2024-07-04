package noppes.npcs.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.audio.ISound;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(ISound.AttenuationType.class)
public class MixinAttenuationType {

    @Inject(method = "<clinit>", at = @At(value = "TAIL"))
    private static void addNewEnum(CallbackInfo ci) {
        addEnum("TABULAR", 3);
    }

    private static void addEnum(String name, int value) {
        try {
            // 使用反射获取枚举类的相关字段
            Field valuesField = ISound.AttenuationType.class.getDeclaredField("$VALUES");
            valuesField.setAccessible(true);
            
            ISound.AttenuationType[] oldValues = (ISound.AttenuationType[]) valuesField.get(null);
            List<ISound.AttenuationType> newValues = new ArrayList<>(Arrays.asList(oldValues));
            
            // 使用反射创建新的枚举实例
            Constructor<ISound.AttenuationType> constructor = ISound.AttenuationType.class.getDeclaredConstructor(String.class, int.class, int.class);
            constructor.setAccessible(true);
            ISound.AttenuationType newValue = constructor.newInstance(name, oldValues.length, value);
            
            newValues.add(newValue);
            
            // 将新的枚举数组设置回枚举类
            valuesField.set(null, newValues.toArray(new ISound.AttenuationType[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
