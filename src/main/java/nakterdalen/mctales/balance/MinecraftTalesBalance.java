package nakterdalen.mctales.balance;

import nakterdalen.mctales.balance.armor.WeightEvents;
import nakterdalen.mctales.balance.food.BalancedFoodHelper;
import nakterdalen.mctales.balance.food.BalancedFoodManager;
import nakterdalen.mctales.balance.food.FoodEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinecraftTalesBalance implements ModInitializer {
    public static final String MOD_ID = "minecraft-tales-balance";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        WeightEvents.registerWeightEvents();
        FoodEvents.registerMineFoodEvent();

        PayloadTypeRegistry.clientboundPlay().register(BalancedFoodManager.TYPE, BalancedFoodManager.STREAM_CODEC);

        LOGGER.info("Hello balanced world!");
    }
}