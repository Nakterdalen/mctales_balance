package nakterdalen.mctales.balance.enchanting;

public interface IEnchantingHandler {

    void balance$setEnchantingListener(Runnable activate);

    void balance$getBookCount(Runnable count);

    void balance$getEnchants(Runnable enchants);

    int balance$getCount();

    int[] balance$transferEnchants();

}
