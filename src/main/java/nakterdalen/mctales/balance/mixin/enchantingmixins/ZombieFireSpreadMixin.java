package nakterdalen.mctales.balance.mixin.enchantingmixins;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ZombieEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.atomic.AtomicInteger;

@Mixin(ZombieEntity.class)
public abstract class ZombieFireSpreadMixin {

    @Redirect(method = "tryAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setOnFireFor(F)V"))
    private void fireProtectionBuff(Entity instance, float seconds) {

        AtomicInteger levelFireProt = new AtomicInteger(0);

        if (instance instanceof LivingEntity) {


            EnchantmentHelper.forEachEnchantment((LivingEntity) instance, ((enchantment, level, context) -> {
                if (enchantment.matchesKey(Enchantments.FIRE_PROTECTION)) {
                    levelFireProt.addAndGet(level);
                }
            }));
        }

        if (instance.getRandom().nextFloat() > 0.1f * levelFireProt.get()) {
            System.out.println("Procced with probability of: " + 0.1f * levelFireProt.get());
            instance.setOnFireFor(seconds);
        }
    }

}
