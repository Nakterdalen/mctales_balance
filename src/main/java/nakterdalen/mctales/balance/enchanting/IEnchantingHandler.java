package nakterdalen.mctales.balance.enchanting;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface IEnchantingHandler {

    void balance$setEnchantingListener(Runnable activate);

    void balance$getBookCount(Runnable count);

    int balance$getCount();

}
