package me.aaryan.evolvingworld.ability;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class AbilityMasterGUI implements Listener {

    private static final Random r = new Random();
    private static final Map<UUID, Integer> pity = new HashMap<>();

    // ================= 📖 OPEN =================

    public static void open(Player p, int tier) {
        Inventory gui = Bukkit.createInventory(null, 27, "Rolling Ability...");
        p.openInventory(gui);
        startRoll(p, gui, tier, 0, 2L);
    }

    // ================= 🎰 CORE ROLL =================

    private static void startRoll(Player p, Inventory gui, int tier, int startIndex, long tickDelay) {

        // Roll rarity once at the very beginning (startIndex == 0 only)
        // We pass rarity through a wrapper so recursive calls share the same result
        String rarity = (startIndex == 0) ? rollRarity(p, tier) : null;
        startRollWithRarity(p, gui, rarity, startIndex, tickDelay);
    }

    // Overload that carries rarity across recursive calls
    private static void startRollWithRarity(Player p, Inventory gui, String rarity, int startIndex, long tickDelay) {

        // If rarity is null this is a continuation — rarity was already determined,
        // but we need to pass it along. Use a single-element array as a mutable holder.
        // Instead, we just always require rarity to be passed in — see open() for the entry point.

        Plugin plugin = Bukkit.getPluginManager().getPlugin("EvolvingWorld");
        List<ItemStack> strip = generateStrip(rarity);

        new BukkitRunnable() {

            int index = startIndex;

            @Override
            public void run() {

                if (!p.isOnline()) {
                    cancel();
                    return;
                }

                // Render the middle row
                for (int slot = 0; slot < 9; slot++) {
                    gui.setItem(9 + slot, strip.get(index + slot));
                }

                // Ticking sound — pitch rises as it slows
                float pitch = (index < 15) ? 1.8f : 1.2f;
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 0.3f, pitch);

                index++;

                // Near the end — cancel and relaunch at slower tick rate
                if (index == 15 && tickDelay == 2L) {
                    cancel();
                    startRollWithRarity(p, gui, rarity, index, 5L);
                    return;
                }

                if (index >= 20) {
                    cancel();
                    finishRoll(p, gui, rarity);
                }
            }

        }.runTaskTimer(plugin, 0L, tickDelay);
    }

    // ================= 🎯 RARITY SYSTEM =================

    private static String rollRarity(Player p, int tier) {

        int spins = pity.getOrDefault(p.getUniqueId(), 0) + 1;
        pity.put(p.getUniqueId(), spins);

        // Pity guarantee at 50 spins
        if (spins >= 50) {
            pity.put(p.getUniqueId(), 0);
            return (r.nextDouble() < 0.7) ? "LEGENDARY" : "MYTHIC";
        }

        double common, rare, legendary, mythic;

        if (tier == 1) {
            common    = 0.80;
            rare      = 0.18;
            legendary = 0.02;
            mythic    = 0.00;
        } else if (tier == 2) {
            common    = 0.70;
            rare      = 0.25;
            legendary = 0.045;
            mythic    = 0.005;
        } else {
            common    = 0.60;
            rare      = 0.30;
            legendary = 0.08;
            mythic    = 0.02;
        }

        // Pity boost — increases mythic/legendary chances each spin
        double boost = spins * 0.0005;
        mythic    += boost;
        legendary += boost * 0.5;

        double roll = r.nextDouble();

        if (roll < mythic) {
            pity.put(p.getUniqueId(), 0);
            return "MYTHIC";
        } else if (roll < mythic + legendary) {
            pity.put(p.getUniqueId(), 0);
            return "LEGENDARY";
        } else if (roll < mythic + legendary + rare) {
            return "RARE";
        } else {
            return "COMMON";
        }
    }

    // ================= 🎞️ STRIP =================

    private static List<ItemStack> generateStrip(String result) {

        // 35 items — index 0..34
        // At stopIndex=20, center slot = index + 4 = 24. Safe with size 35.
        List<ItemStack> strip = new ArrayList<>();

        for (int i = 0; i < 35; i++) {
            strip.add(getRandomPane());
        }

        // Place result pane at center landing position
        strip.set(24, getPane(result));

        return strip;
    }

    // ================= 🎉 FINISH =================

    private static void finishRoll(Player p, Inventory gui, String rarity) {

        // Read pity count BEFORE any reset so we can display the correct number
        int spinsBeforeReset = pity.getOrDefault(p.getUniqueId(), 0);

        gui.setItem(13, getPane(rarity));

        switch (rarity) {
            case "MYTHIC" -> {
                p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
                Bukkit.broadcastMessage("§c§l" + p.getName() + " pulled a MYTHIC ability!");
            }
            case "LEGENDARY" -> p.playSound(p.getLocation(), Sound.UI_TOAST_IN, 1f, 1.2f);
            default -> p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
        }

        String ability = AbilityPool.getRandomAbility(rarity);
        p.sendMessage("§aYou got: §e" + ability + " §7[" + rarity + "]");

        // Pity feedback
        switch (rarity) {
            case "COMMON", "RARE" -> {
                if (spinsBeforeReset >= 40) {
                    p.sendMessage("§cYour luck is peaking... (" + spinsBeforeReset + "/50)");
                } else {
                    p.sendMessage("§7Pity: §e" + spinsBeforeReset + "/50");
                }
            }
            default -> p.sendMessage("§6Pity reset at §e" + spinsBeforeReset + "§6 spins.");
        }

        ItemStack item = AbilityItemUtil.createAbilityItem(ability);
        AbilityHotbarManager.giveAbility(p, item);
    }

    // ================= 🧹 MEMORY CLEANUP =================

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        pity.remove(e.getPlayer().getUniqueId());
    }

    // ================= 🎨 VISUAL =================

    private static ItemStack getRandomPane() {

        int roll = r.nextInt(100);

        if (roll < 60) return getPane("COMMON");
        if (roll < 90) return getPane("RARE");
        if (roll < 98) return getPane("LEGENDARY");
        return getPane("MYTHIC");
    }

    private static ItemStack getPane(String rarity) {

        Material mat = switch (rarity) {
            case "COMMON"    -> Material.GREEN_WOOL;
            case "RARE"      -> Material.BLUE_WOOL;
            case "LEGENDARY" -> Material.YELLOW_WOOL;
            case "MYTHIC"    -> Material.RED_WOOL;
            default          -> Material.GRAY_WOOL;
        };

        return new ItemStack(mat);
    }
}