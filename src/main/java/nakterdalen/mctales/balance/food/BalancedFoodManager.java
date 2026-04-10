package nakterdalen.mctales.balance.food;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.StringRepresentable;

public class BalancedFoodManager {

    public static final String FOOD_DATA = "minecraft-tales-balance.fooddata";

    private final List<FoodType> hungerBar;
    private final int exhaustion;

    public BalancedFoodManager() {
        exhaustion = 0;
        this.hungerBar = new ArrayList<>();
    }

    public BalancedFoodManager(List<FoodType> hungerBar, int exhaustion) {
        this.hungerBar = hungerBar;
        this.exhaustion = exhaustion;
    }

    public int getExhaustion() {
        return this.exhaustion;
    }

    public List<FoodType> getHungerBar() {
        return this.hungerBar;
    }

    //change to xmap
    public static final Codec<BalancedFoodManager> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            FoodType.CODEC.listOf().fieldOf("food_types").forGetter(BalancedFoodManager::getHungerBar),
            Codec.intRange(0, 20).fieldOf("exhaustion").forGetter(BalancedFoodManager::getExhaustion)
    ).apply(instance, BalancedFoodManager::new));

    public enum FoodType implements StringRepresentable{
        NONE,
        MEAT,
        GRAIN,
        VEGETABLE;

        public static final EnumCodec<FoodType> CODEC = StringRepresentable.fromEnum(FoodType::values);

        @Override
        public String getSerializedName() {
            return this.toString();
        }
    }

}
