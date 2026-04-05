package nakterdalen.mctales.balance.enchanting;

import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface IEnchantingHandler {

    void balance$setEnchantingListener(Runnable activate);

    void balance$getBookCount(Runnable count);

    void balance$getEnchants(Runnable enchants);

    int balance$getCount();

    int[] balance$transferEnchants();

}
