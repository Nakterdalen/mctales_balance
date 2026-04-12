package nakterdalen.mctales.balance.food;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum BalancedFoodItems {

    APPLE(BalancedFoodManager.FoodType.GRAIN, 2, BalancedFoodManager.FoodWeight.MEDIUM),
    BAKED_POTATO(BalancedFoodManager.FoodType.VEGETABLE, 3, BalancedFoodManager.FoodWeight.MEDIUM),
    BEETROOT(BalancedFoodManager.FoodType.VEGETABLE, 1, BalancedFoodManager.FoodWeight.MEDIUM),
    BEETROOT_SOUP(BalancedFoodManager.FoodType.VEGETABLE, 6, BalancedFoodManager.FoodWeight.MEDIUM),
    BREAD(BalancedFoodManager.FoodType.GRAIN, 3, BalancedFoodManager.FoodWeight.MEDIUM),
    CARROT(BalancedFoodManager.FoodType.VEGETABLE, 2, BalancedFoodManager.FoodWeight.MEDIUM),
    CHORUS_FRUIT(BalancedFoodManager.FoodType.VEGETABLE, 2, BalancedFoodManager.FoodWeight.MEDIUM),
    COOKED_CHICKEN(BalancedFoodManager.FoodType.MEAT, 3, BalancedFoodManager.FoodWeight.MEDIUM),
    COOKED_COD(BalancedFoodManager.FoodType.MEAT, 3, BalancedFoodManager.FoodWeight.MEDIUM),
    COOKED_BEEF(BalancedFoodManager.FoodType.MEAT, 5, BalancedFoodManager.FoodWeight.LARGE),
    COOKED_MUTTON(BalancedFoodManager.FoodType.MEAT, 4, BalancedFoodManager.FoodWeight.MEDIUM),
    COOKED_PORKCHOP(BalancedFoodManager.FoodType.MEAT, 5, BalancedFoodManager.FoodWeight.LARGE),
    COOKED_RABBIT(BalancedFoodManager.FoodType.MEAT, 3, BalancedFoodManager.FoodWeight.MEDIUM),
    COOKED_SALMON(BalancedFoodManager.FoodType.MEAT, 4, BalancedFoodManager.FoodWeight.LARGE),
    COOKIE(BalancedFoodManager.FoodType.GRAIN, 1, BalancedFoodManager.FoodWeight.SMALL),
    DRIED_KELP(BalancedFoodManager.FoodType.VEGETABLE, 1, BalancedFoodManager.FoodWeight.SMALL),
    ENCHANTED_GOLDEN_APPLE(BalancedFoodManager.FoodType.VEGETABLE, 2, BalancedFoodManager.FoodWeight.LARGE),
    GOLDEN_APPLE(BalancedFoodManager.FoodType.VEGETABLE, 2, BalancedFoodManager.FoodWeight.LARGE),
    GOLDEN_CARROT(BalancedFoodManager.FoodType.VEGETABLE, 3, BalancedFoodManager.FoodWeight.LARGE),
    GLOW_BERRIES(BalancedFoodManager.FoodType.VEGETABLE, 1, BalancedFoodManager.FoodWeight.MEDIUM),
    HONEY_BOTTLE(BalancedFoodManager.FoodType.GRAIN, 1, BalancedFoodManager.FoodWeight.POTION),
    MELON_SLICE(BalancedFoodManager.FoodType.VEGETABLE, 1, BalancedFoodManager.FoodWeight.SMALL),
    MUSHROOM_STEW(BalancedFoodManager.FoodType.MEAT, 3, BalancedFoodManager.FoodWeight.MEDIUM),
    POISONOUS_POTATO(BalancedFoodManager.FoodType.VEGETABLE, 1, BalancedFoodManager.FoodWeight.MEDIUM),
    POTATO(BalancedFoodManager.FoodType.VEGETABLE, 1, BalancedFoodManager.FoodWeight.MEDIUM),
    PUFFERFISH(BalancedFoodManager.FoodType.MEAT, 1, BalancedFoodManager.FoodWeight.LARGE),
    PUMPKIN_PIE(List.of(
            BalancedFoodManager.FoodType.GRAIN,
            BalancedFoodManager.FoodType.GRAIN,
            BalancedFoodManager.FoodType.VEGETABLE,
            BalancedFoodManager.FoodType.VEGETABLE),
            BalancedFoodManager.FoodWeight.LARGE),
    RABBIT_STEW(List.of(
            BalancedFoodManager.FoodType.MEAT,
            BalancedFoodManager.FoodType.MEAT,
            BalancedFoodManager.FoodType.MEAT,
            BalancedFoodManager.FoodType.MEAT,
            BalancedFoodManager.FoodType.VEGETABLE,
            BalancedFoodManager.FoodType.VEGETABLE,
            BalancedFoodManager.FoodType.VEGETABLE,
            BalancedFoodManager.FoodType.VEGETABLE),
            BalancedFoodManager.FoodWeight.MEDIUM),
    BEEF(BalancedFoodManager.FoodType.MEAT, 2, BalancedFoodManager.FoodWeight.LARGE),
    CHICKEN(BalancedFoodManager.FoodType.MEAT, 1, BalancedFoodManager.FoodWeight.MEDIUM),
    COD(BalancedFoodManager.FoodType.MEAT, 1, BalancedFoodManager.FoodWeight.MEDIUM),
    MUTTON(BalancedFoodManager.FoodType.MEAT, 2, BalancedFoodManager.FoodWeight.MEDIUM),
    PORKCHOP(BalancedFoodManager.FoodType.MEAT, 2, BalancedFoodManager.FoodWeight.LARGE),
    RABBIT(BalancedFoodManager.FoodType.MEAT, 1, BalancedFoodManager.FoodWeight.MEDIUM),
    ROTTEN_FLESH(BalancedFoodManager.FoodType.MEAT, 2, BalancedFoodManager.FoodWeight.LARGE),
    SALMON(BalancedFoodManager.FoodType.MEAT, 3, BalancedFoodManager.FoodWeight.LARGE),
    SPIDER_EYE(BalancedFoodManager.FoodType.MEAT, 1, BalancedFoodManager.FoodWeight.SMALL),
    SUSPICIOUS_STEW(BalancedFoodManager.FoodType.MEAT, 3, BalancedFoodManager.FoodWeight.MEDIUM),
    SWEET_BERRIES(BalancedFoodManager.FoodType.VEGETABLE, 1, BalancedFoodManager.FoodWeight.SMALL),
    TROPICAL_FISH(BalancedFoodManager.FoodType.MEAT, 1, BalancedFoodManager.FoodWeight.MEDIUM),
    MILK_BUCKET(BalancedFoodManager.FoodType.MEAT, 0, BalancedFoodManager.FoodWeight.POTION),
    POTION(BalancedFoodManager.FoodType.NONE, 0, BalancedFoodManager.FoodWeight.POTION),
    OMINOUS_BOTTLE(BalancedFoodManager.FoodType.NONE, 0, BalancedFoodManager.FoodWeight.POTION);

    private final List<BalancedFoodManager.FoodType> foodValue;
    private final BalancedFoodManager.FoodWeight weight;

    BalancedFoodItems(List<BalancedFoodManager.FoodType> foodValue, BalancedFoodManager.FoodWeight weight) {
        this.foodValue = foodValue;
        this.weight = weight;
    }

    BalancedFoodItems(BalancedFoodManager.FoodType type, int amount, BalancedFoodManager.FoodWeight weight) {
        this(createUniformList(type, amount), weight);
    }

    private static List<BalancedFoodManager.FoodType> createUniformList(BalancedFoodManager.FoodType type, int number) {
        ArrayList<BalancedFoodManager.FoodType> list = new ArrayList<>(number);
        list.addAll(Collections.nCopies(number, type));
        return list;
    }

    public List<BalancedFoodManager.FoodType> getFoodValue() {
        return this.foodValue;
    }

    public float getEatSeconds() {
        return this.weight.getEatingTime();
    }

    public int getStackSize() {
        return this.weight.getStackSize();
    }

    public static BalancedFoodItems getBalancedFoodItem(String identifier) {

        return valueOf(identifier.split(":")[1].toUpperCase());
    }

    public static boolean hasBalancedFoodItem(String identifier) {
        return Arrays.stream(values()).anyMatch(value -> value.name().equals(identifier.split(":")[1].toUpperCase()));
    }


}
