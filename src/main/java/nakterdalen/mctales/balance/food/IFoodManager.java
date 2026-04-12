package nakterdalen.mctales.balance.food;

public interface IFoodManager {

    BalancedFoodManager balance$getFoodManager();

    void balance$mineHunger();

    boolean balance$canRun();

    void balance$setFoodManager(BalancedFoodManager manager);

    boolean balance$canEat();

}
