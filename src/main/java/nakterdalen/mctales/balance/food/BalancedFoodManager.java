package nakterdalen.mctales.balance.food;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.*;

import io.netty.buffer.ByteBuf;
import nakterdalen.mctales.balance.MinecraftTalesBalance;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gamerules.GameRules;
import org.jspecify.annotations.NonNull;

public class BalancedFoodManager implements CustomPacketPayload{

    private List<FoodType> hungerBar = new ArrayList<>();
    private float exhaustion;
    private FoodType lastFoodType;
    private int tickTimer = 0;

    public BalancedFoodManager() {
        exhaustion = 0f;
        hungerBar.addAll(Collections.nCopies(10, FoodType.MEAT));
        lastFoodType = FoodType.NONE;
    }

    public BalancedFoodManager(BalancedFoodManager manager) {
        this(manager.getHungerBar(), manager.getExhaustion(), manager.getLastFoodType());
        this.tickTimer = 0;
    }

    public BalancedFoodManager(List<FoodType> hungerBar, float exhaustion,  FoodType lastFoodType) {
        this.hungerBar = hungerBar;
        this.exhaustion = exhaustion;
        this.lastFoodType = lastFoodType;
    }

    private BalancedFoodManager(float exhaustion, byte[] hungerArray, byte foodType) {
        ArrayList<FoodType> list = new ArrayList<>();
        for (byte b : hungerArray) {
            list.add(FoodType.values()[b]);
        }
        this(list, exhaustion, FoodType.values()[foodType]);
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

        return (health > mapDiff) && emptyBar;
    }

    public boolean willHeal() {
        long fullBars = this.hungerBar.stream().filter(type -> !type.equals(FoodType.NONE)).count();
        return fullBars >= 9;
    }

    public boolean canRun() {
        long fullBars = this.hungerBar.stream().filter(type -> !type.equals(FoodType.NONE)).count();
        return fullBars > 3;
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
            hungerBar = exhaustion > 10f ? hungerBar.subList(1,11) :  hungerBar.subList(0, 10);
            resetExhaustion();
        } else {
            while (hungerBar.size() < 10) hungerBar.addFirst(BalancedFoodManager.FoodType.NONE);
        }
    }

    public void eatCake(float chance) {
        if (chance < 0.3f) {
            addFoodValue(BalancedFoodItems.CAKE_MEAT);
        } else {
            addFoodValue(BalancedFoodItems.CAKE_GRAIN);
        }
    }

    public void addExhaustion(float value, FoodType type, Player player) {
        addExhaustion(value, type, player, 1f);
    }

    public void addExhaustion(float value, FoodType type, Player player, float chance) {
        if (!player.isCreative() && !player.isSpectator() && player.level().getDifficulty() != Difficulty.PEACEFUL && !player.level().isClientSide()) {
            long numberInList = this.hungerBar.stream().filter(f -> f == type).count();
            float reduction = 1 - (float)(numberInList / 10) * 0.8f;
            reduction = player.hasEffect(MobEffects.SATURATION) ? 0f : reduction;
            int hungerPenalty = !player.hasEffect(MobEffects.HUNGER) ? 1 :
                    (Objects.requireNonNull(player.getEffect(MobEffects.HUNGER)).getAmplifier() + 1) * 20;
            this.exhaustion += value * reduction * hungerPenalty;
            if (chance >= player.getRandom().nextFloat()) {
                updateLastFoodType(type);
            }
        }
    }

    public float getExhaustion() {
        return this.exhaustion;
    }

    public FoodType getLastFoodType() {
        return this.lastFoodType;
    }

    public byte getLastByteType() {
        return (byte)this.lastFoodType.ordinal();
    }

    public boolean canEat() {
        return this.hungerBar.stream().anyMatch(type -> type.equals(FoodType.NONE));
    }

    public void updateLastFoodType(FoodType foodType) {
        this.lastFoodType = foodType;
    }

    public ArrayList<FoodType> getHungerBar() {
        return new ArrayList<>(hungerBar);
    }

    private byte[] getByteHunger() {
        byte[] bytes = new byte[this.hungerBar.size()];
        for (int i = 0; i < this.hungerBar.size(); i++) {
            bytes[i] = ((byte) this.hungerBar.get(i).ordinal());
        }
        return bytes;
    }

    public static final Codec<BalancedFoodManager> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            FoodType.CODEC.listOf().fieldOf("food_types").xmap(
                    ArrayList::new,
                            t -> t)
                    .forGetter(BalancedFoodManager::getHungerBar),
            Codec.floatRange(0, 20).fieldOf("exhaustion").forGetter(BalancedFoodManager::getExhaustion),
            FoodType.CODEC.orElse(FoodType.NONE).fieldOf("last_food_type").forGetter(BalancedFoodManager::getLastFoodType)
    ).apply(instance, BalancedFoodManager::new));

    public static final StreamCodec<ByteBuf, BalancedFoodManager> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, BalancedFoodManager::getExhaustion,
            ByteBufCodecs.BYTE_ARRAY, BalancedFoodManager::getByteHunger,
            ByteBufCodecs.BYTE, BalancedFoodManager::getLastByteType,
            BalancedFoodManager::new
    );

    public static final AttachmentType<BalancedFoodManager> FOOD_ATTACHMENT = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath(MinecraftTalesBalance.MOD_ID, "food_attachment"), builder ->
                    builder.initializer(BalancedFoodManager::new)
                    .syncWith(STREAM_CODEC, AttachmentSyncPredicate.targetOnly())
                    .persistent(CODEC)
    );

    public static final CustomPacketPayload.Type<BalancedFoodManager> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(MinecraftTalesBalance.MOD_ID, "food_packet"));

    public void foodTick(Player player) {

        if (player.isLocalPlayer()) return;

        if (this.exhaustion > 20.0f) {
            resetExhaustion();
            decrementHunger();
        }

        ServerLevel level = (ServerLevel)player.level();

        boolean naturalRegen = level.getGameRules().get(GameRules.NATURAL_HEALTH_REGENERATION);
        if (!player.isCreative() && naturalRegen && player.isHurt() && willHeal()) {
            this.tickTimer++;
            int regenDiscount = 100 * (lastFoodType.equals(FoodType.MEAT) ? 1 : 0);
            if (this.tickTimer == 150 - regenDiscount) {
                player.heal(1.0f);
                ((IFoodManager)player).balance$regenHunger();
                this.tickTimer = 0;
            }
        } else if (this.willStarve(player.getHealth(), player.level().getDifficulty())) {
            this.tickTimer++;
            if (this.tickTimer == 80) {
                player.hurtServer(level, player.damageSources().starve(), 1.0F);
                this.tickTimer = 0;
            }
        } else {
            this.tickTimer = 0;
        }
    }

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
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
