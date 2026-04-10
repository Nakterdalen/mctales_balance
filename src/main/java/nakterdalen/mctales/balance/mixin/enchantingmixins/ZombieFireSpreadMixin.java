package nakterdalen.mctales.balance.mixin.enchantingmixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

@Mixin(Zombie.class)
public abstract class ZombieFireSpreadMixin {

    @Redirect(method = "doHurtTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;igniteForSeconds(F)V"))
    private void fireProtectionBuff(Entity instance, float seconds) {

        AtomicInteger levelFireProt = new AtomicInteger(0);

        if (instance instanceof LivingEntity) {


            EnchantmentHelper.runIterationOnEquipment((LivingEntity) instance, ((enchantment, level, context) -> {
                if (enchantment.is(Enchantments.FIRE_PROTECTION)) {
                    levelFireProt.addAndGet(level);
                }
            }));
        }

        if (instance.getRandom().nextFloat() > 0.1f * levelFireProt.get()) {
            System.out.println("Procced with probability of: " + 0.1f * levelFireProt.get());
            instance.igniteForSeconds(seconds);
        }
    }

}
