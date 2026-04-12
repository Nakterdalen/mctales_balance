package nakterdalen.mctales.balance.food;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import nakterdalen.mctales.balance.MinecraftTalesBalance;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gamerules.GameRules;
import org.jspecify.annotations.NonNull;

public class BalancedFoodManager {

    private List<FoodType> hungerBar = new ArrayList<>();
    private float exhaustion;
    private int tickTimer = 0;

    public BalancedFoodManager() {
        exhaustion = 0f;
        for (int i = 0; i < 10; i++) {
            hungerBar.add(FoodType.MEAT);
        }
    }

    public BalancedFoodManager(List<FoodType> hungerBar, float exhaustion) {
        this.hungerBar = hungerBar;
        this.exhaustion = exhaustion;
    }

    private BalancedFoodManager(float exhaustion, byte[] hungerArray) {
        ArrayList<FoodType> list = new ArrayList<>();
        for (byte b : hungerArray) {
            list.add(FoodType.values()[b]);
        }
        this(list, exhaustion);
    }

    private void incExhaustion(float value) {
        this.exhaustion += value;
    }

    public void resetExhaustion() {
        this.exhaustion = 0f;
    }

    public boolean willStarve(float health, Difficulty difficulty) {
        boolean emptyBar = this.hungerBar.stream().allMatch(type -> type.equals(FoodType.NONE));
        float mapDiff = switch (difficulty) {
            case EASY -> 10;
            case NORMAL -> 1;
            case HARD -> 0;
            default -> 20;
        };

        return health < mapDiff && emptyBar;
    }

    public boolean willHeal() {
        long fullBars = this.hungerBar.stream().filter(type -> !type.equals(FoodType.NONE)).count();
        return fullBars >= 9;
    }

    public boolean canRun() {
        long fullBars = this.hungerBar.stream().filter(type -> !type.equals(FoodType.NONE)).count();
        return fullBars >= 3;
    }

    public void decrementHunger() {
        hungerBar.removeIf(t -> t.equals(BalancedFoodManager.FoodType.NONE));
        hungerBar.removeFirst();
        while (hungerBar.size() < 10) hungerBar.addFirst(FoodType.NONE);
    }

    public void addFoodValue(BalancedFoodItems item) {
        hungerBar.removeIf(t -> t.equals(BalancedFoodManager.FoodType.NONE));
        hungerBar.addAll(item.getFoodValue());

        if (hungerBar.size() > 10) {
            hungerBar = hungerBar.subList(0,10);
        } else {
            while (hungerBar.size() < 10) hungerBar.addFirst(BalancedFoodManager.FoodType.NONE);
        }
    }

    public void addExhaustion(float value, FoodType type) {
        long numberInList = this.hungerBar.stream().filter(f -> f == type).count();
        float reduction = 1 - (float)(numberInList / 10) * 0.8f;
        incExhaustion(value * reduction * 20);
    }

    public float getExhaustion() {
        return this.exhaustion;
    }

    public boolean canEat() {
        return this.hungerBar.stream().anyMatch(type -> type.equals(FoodType.NONE));
    }

    public List<FoodType> getHungerBar() {
        return this.hungerBar;
    }

    private byte[] getByteHunger() {
        byte[] bytes = new byte[this.hungerBar.size()];
        for (int i = 0; i < this.hungerBar.size(); i++) {
            bytes[i] = ((byte) this.hungerBar.get(i).ordinal());
        }
        return bytes;
    }

    public static final Codec<BalancedFoodManager> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            FoodType.CODEC.listOf().fieldOf("food_types").forGetter(BalancedFoodManager::getHungerBar),
            Codec.floatRange(0, 20).fieldOf("exhaustion").forGetter(BalancedFoodManager::getExhaustion)
    ).apply(instance, BalancedFoodManager::new));

    public static final StreamCodec<ByteBuf, BalancedFoodManager> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, BalancedFoodManager::getExhaustion,
            ByteBufCodecs.BYTE_ARRAY, BalancedFoodManager::getByteHunger,
            BalancedFoodManager::new
    );

    public static final AttachmentType<BalancedFoodManager> FOOD_ATTACHMENT = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(MinecraftTalesBalance.MOD_ID, "food_attachment"), builder ->
                    builder.initializer(BalancedFoodManager::new)
                    .syncWith(STREAM_CODEC, AttachmentSyncPredicate.targetOnly())
                    .persistent(CODEC)
    );

    public void foodTick(Player player) {

        if (this.exhaustion > 20.0f) {
            resetExhaustion();
            decrementHunger();
        }

        ServerLevel level = (ServerLevel)player.level();

        boolean naturalRegen = level.getGameRules().get(GameRules.NATURAL_HEALTH_REGENERATION);
        if (!player.isCreative() && naturalRegen && player.isHurt() && willHeal()) {
            this.tickTimer++;
            if (this.tickTimer == 200) {
                player.heal(1.0f);
                addExhaustion(1.0f, BalancedFoodManager.FoodType.MEAT);
                this.tickTimer = 0;
            }
        } else if (this.willStarve(player.getHealth(), player.level().getDifficulty())) {
            this.tickTimer++;
            if (this.tickTimer == 80) {
                player.hurtServer(level, player.damageSources().starve(), 1.0F);
            }
            this.tickTimer = 0;
        } else {
            this.tickTimer = 0;
        }

        //Printing for now
        if (player.tickCount % 40 == 0) {
            System.out.println("Exhaustion: " +  this.getExhaustion());
            System.out.print("Bars: ");
            this.getHungerBar().forEach(bar -> System.out.print(" " + bar.toString()));
            System.out.println(" ");
        }
    }

    public enum FoodType implements StringRepresentable{
        NONE,
        MEAT,
        GRAIN,
        VEGETABLE;

        public static final EnumCodec<FoodType> CODEC = StringRepresentable.fromEnum(FoodType::values);

        @Override
        public @NonNull String getSerializedName() {
            return this.toString();
        }
    }

    public enum FoodWeight {
        SMALL(0.8f, 32),
        MEDIUM(1.6f, 16),
        LARGE(3.2f, 8),
        POTION(0.8f, 8);

        private final float eatingTime;
        private final int stackSize;

        FoodWeight(float eatingTime, int stackSize) {
            this.eatingTime = eatingTime;
            this.stackSize = stackSize;
        }

        public float getEatingTime() {return eatingTime;}
        public int getStackSize() {return stackSize;}
    }

}
