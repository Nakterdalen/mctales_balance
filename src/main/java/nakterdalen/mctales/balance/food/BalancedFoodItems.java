package nakterdalen.mctales.balance.food;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BalancedFoodItems {

    private final String identifier;
    private final List<BalancedFoodManager.FoodType> foodValue;
    private final BalancedFoodManager.FoodWeight weight;

    public BalancedFoodItems(String identifier, List<BalancedFoodManager.FoodType> foodValue, BalancedFoodManager.FoodWeight weight) {
        this.identifier = identifier;
        this.foodValue = foodValue;
        this.weight = weight;
    }

    public void addFoodValue(BalancedFoodManager manager) {
        List <BalancedFoodManager.FoodType> bar = manager.getHungerBar();

        bar.removeIf(t -> t.equals(BalancedFoodManager.FoodType.NONE));
        bar.addAll(foodValue);

        if (bar.size() > 10) {
            bar = bar.subList(0,10);
        } else {
            while (bar.size() < 10) bar.add(BalancedFoodManager.FoodType.NONE);
        }
        manager.setHungerBar(bar);
    }

    private static List<BalancedFoodManager.FoodType> createUniformList(BalancedFoodManager.FoodType type, int number) {
        ArrayList<BalancedFoodManager.FoodType> list = new ArrayList<>(number);
        list.addAll(Collections.nCopies(number, type));
        return list;
    }

    private static BalancedFoodItems createFoodItem(String id, BalancedFoodManager.FoodType type, int amount, BalancedFoodManager.FoodWeight weight) {
        return new BalancedFoodItems(id, createUniformList(type, amount), weight);
    }

    static {
        final BalancedFoodItems APPLE = createFoodItem("apple", BalancedFoodManager.FoodType.GRAIN, 2, BalancedFoodManager.FoodWeight.MEDIUM);
        final BalancedFoodItems BAKED_POTATO = createFoodItem("baked_potato", BalancedFoodManager.FoodType.VEGETABLE, 3, BalancedFoodManager.FoodWeight.MEDIUM);
        final BalancedFoodItems BEETROOT = createFoodItem("beetroot", BalancedFoodManager.FoodType.VEGETABLE, 1, BalancedFoodManager.FoodWeight.MEDIUM);
        final BalancedFoodItems BEETROOT_SOUP = createFoodItem("beetroot_soup", BalancedFoodManager.FoodType.VEGETABLE, 6, BalancedFoodManager.FoodWeight.MEDIUM);
        final BalancedFoodItems BREAD = createFoodItem("bread", BalancedFoodManager.FoodType.GRAIN, 3, BalancedFoodManager.FoodWeight.MEDIUM);
        final BalancedFoodItems CARROT = createFoodItem("carrot", BalancedFoodManager.FoodType.VEGETABLE, 2, BalancedFoodManager.FoodWeight.MEDIUM);
        final BalancedFoodItems CHORUS_FRUIT = createFoodItem("chorus_fruit", BalancedFoodManager.FoodType.VEGETABLE, 2, BalancedFoodManager.FoodWeight.MEDIUM);
        final BalancedFoodItems COOKED_CHICKEN = createFoodItem("cooked_chicken", BalancedFoodManager.FoodType.MEAT, 3, BalancedFoodManager.FoodWeight.MEDIUM);
        final BalancedFoodItems COOKED_COD = createFoodItem("cooked_cod", BalancedFoodManager.FoodType.MEAT, 3, BalancedFoodManager.FoodWeight.MEDIUM);
        final BalancedFoodItems COOKED_BEEF = createFoodItem("cooked_beef", BalancedFoodManager.FoodType.MEAT, 5, BalancedFoodManager.FoodWeight.LARGE);
        final BalancedFoodItems COOKED_MUTTON = createFoodItem("cooked_mutton", BalancedFoodManager.FoodType.MEAT, 4, BalancedFoodManager.FoodWeight.MEDIUM);
        final BalancedFoodItems COOKED_PORKCHOP = createFoodItem("cooked_porkchop", BalancedFoodManager.FoodType.MEAT, 5, BalancedFoodManager.FoodWeight.LARGE);
        final BalancedFoodItems COOKED_RABBIT = createFoodItem("cooked_rabbit", BalancedFoodManager.FoodType.MEAT, 3, BalancedFoodManager.FoodWeight.MEDIUM);
        final BalancedFoodItems COOKED_SALMON = createFoodItem("cooked_salmon", BalancedFoodManager.FoodType.MEAT, 4, BalancedFoodManager.FoodWeight.LARGE);
        final BalancedFoodItems COOKIE = createFoodItem("cookie", BalancedFoodManager.FoodType.GRAIN, 1, BalancedFoodManager.FoodWeight.SMALL);
        final BalancedFoodItems DRIED_KELP = createFoodItem("dried_kelp", BalancedFoodManager.FoodType.VEGETABLE, 1, BalancedFoodManager.FoodWeight.SMALL);
        final BalancedFoodItems ENCHANTED_GOLDEN_APPLE = createFoodItem("enchanted_golden_apple", BalancedFoodManager.FoodType.VEGETABLE, 2, BalancedFoodManager.FoodWeight.LARGE);
        final BalancedFoodItems GOLDEN_APPLE = createFoodItem("golden_apple", BalancedFoodManager.FoodType.VEGETABLE, 2, BalancedFoodManager.FoodWeight.LARGE);
        final BalancedFoodItems GOLDEN_CARROT = createFoodItem("golden_carrot", BalancedFoodManager.FoodType.VEGETABLE, 3, BalancedFoodManager.FoodWeight.LARGE);
        final BalancedFoodItems GLOW_BERRIES = createFoodItem("glow_berries", BalancedFoodManager.FoodType.VEGETABLE, 1, BalancedFoodManager.FoodWeight.MEDIUM);
        final BalancedFoodItems HONEY_BOTTLE = createFoodItem("honey_bottle", BalancedFoodManager.FoodType.GRAIN, 1, BalancedFoodManager.FoodWeight.SMALL);
        final BalancedFoodItems MELON_SLICE = createFoodItem("melon_slice", BalancedFoodManager.FoodType.VEGETABLE, 1, BalancedFoodManager.FoodWeight.SMALL);
        final BalancedFoodItems MUSHROOM_STEW = createFoodItem("mushroom_stew", BalancedFoodManager.FoodType.MEAT, 3, BalancedFoodManager.FoodWeight.MEDIUM);
        final BalancedFoodItems POISONOUS_POTATO = createFoodItem("poisonous_potato", BalancedFoodManager.FoodType.VEGETABLE, 1, BalancedFoodManager.FoodWeight.MEDIUM);
        final BalancedFoodItems POTATO = createFoodItem("potato", BalancedFoodManager.FoodType.VEGETABLE, 1, BalancedFoodManager.FoodWeight.MEDIUM);
        final BalancedFoodItems PUFFERFISH = createFoodItem("pufferfish", BalancedFoodManager.FoodType.MEAT, 1, BalancedFoodManager.FoodWeight.LARGE);
        final BalancedFoodItems PUMPKIN_PIE = new BalancedFoodItems("pumpkin_pie", List.of(
                BalancedFoodManager.FoodType.GRAIN,
                BalancedFoodManager.FoodType.GRAIN,
                BalancedFoodManager.FoodType.VEGETABLE,
                BalancedFoodManager.FoodType.VEGETABLE),
                BalancedFoodManager.FoodWeight.LARGE);
        final BalancedFoodItems RABBIT_STEW = new BalancedFoodItems("rabbit_stew", List.of(
                BalancedFoodManager.FoodType.MEAT,
                BalancedFoodManager.FoodType.MEAT,
                BalancedFoodManager.FoodType.MEAT,
                BalancedFoodManager.FoodType.MEAT,
                BalancedFoodManager.FoodType.VEGETABLE,
                BalancedFoodManager.FoodType.VEGETABLE,
                BalancedFoodManager.FoodType.VEGETABLE,
                BalancedFoodManager.FoodType.VEGETABLE),
                BalancedFoodManager.FoodWeight.MEDIUM);
        final BalancedFoodItems BEEF = createFoodItem("beef", BalancedFoodManager.FoodType.MEAT, 2, BalancedFoodManager.FoodWeight.LARGE);
        final BalancedFoodItems CHICKEN = createFoodItem("chicken", BalancedFoodManager.FoodType.MEAT, 1, BalancedFoodManager.FoodWeight.MEDIUM);
        final BalancedFoodItems COD = createFoodItem("cod", BalancedFoodManager.FoodType.MEAT, 1, BalancedFoodManager.FoodWeight.MEDIUM);
        final BalancedFoodItems MUTTON = createFoodItem("mutton", BalancedFoodManager.FoodType.MEAT, 2, BalancedFoodManager.FoodWeight.MEDIUM);
        final BalancedFoodItems PORKCHOP = createFoodItem("porkchop", BalancedFoodManager.FoodType.MEAT, 2, BalancedFoodManager.FoodWeight.LARGE);
        final BalancedFoodItems RABBIT = createFoodItem("rabbit", BalancedFoodManager.FoodType.MEAT, 1, BalancedFoodManager.FoodWeight.MEDIUM);
        final BalancedFoodItems ROTTEN_FLESH = createFoodItem("rotten_flesh", BalancedFoodManager.FoodType.MEAT, 2, BalancedFoodManager.FoodWeight.LARGE);
        final BalancedFoodItems SPIDER_EYE = createFoodItem("spider_eye", BalancedFoodManager.FoodType.MEAT, 1, BalancedFoodManager.FoodWeight.SMALL);
        final BalancedFoodItems SUSPICIOUS_STEW = createFoodItem("suspicious_stew", BalancedFoodManager.FoodType.MEAT, 3, BalancedFoodManager.FoodWeight.MEDIUM);
        final BalancedFoodItems SWEET_BERRIES = createFoodItem("sweet_berries", BalancedFoodManager.FoodType.VEGETABLE, 1, BalancedFoodManager.FoodWeight.SMALL);
        final BalancedFoodItems TROPICAL_FISH = createFoodItem("tropical_fish", BalancedFoodManager.FoodType.MEAT, 1, BalancedFoodManager.FoodWeight.MEDIUM);
    }

}
