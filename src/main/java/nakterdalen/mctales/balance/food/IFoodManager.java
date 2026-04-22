package nakterdalen.mctales.balance.food;

public interface IFoodManager {

    BalancedFoodManager balance$getFoodManager();

    void balance$mineHunger();

    void balance$regenHunger();

    void balance$setFoodManager(BalancedFoodManager manager);

    boolean balance$canEat();

    void balance$markDirtyFood();

    void balance$createFoodData();

    void balance$saveFoodData();

}
