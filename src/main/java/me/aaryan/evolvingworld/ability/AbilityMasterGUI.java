package me.aaryan.evolvingworld.ability;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class AbilityMasterGUI {

    private static final Random r = new Random();
    private static final Map<UUID, Integer> pity = new HashMap<>();

    public static void open(Player p, int tier) {

        Inventory gui = Bukkit.createInventory(null, 27, "Rolling Ability...");
        p.openInventory(gui);

        startRoll(p, gui, tier);
    }

    // ================= 🎰 CORE ROLL =================

    private static void startRoll(Player p, Inventory gui, int tier) {

        String rarity = rollRarity(p, tier);

        List<ItemStack> strip = generateStrip(rarity);

        new BukkitRunnable() {

            int index = 0;
            final int stopIndex = 20;

            @Override
            public void run() {

                if (!p.isOnline()) {
                    cancel();
                    return;
                }

                // render middle row
                for (int i = 0; i < 9; i++) {
                    gui.setItem(9 + i, strip.get(index + i));
                }

                // ticking sound
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 0.3f, 1.8f);

                index++;

                // slow down near end
                if (index > stopIndex - 5) {
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException ignored) {}
                }

                if (index >= stopIndex) {
                    cancel();
                    finishRoll(p, gui, rarity);
                }

            }

        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("EvolvingWorld"), 0, 2);
    }

    // ================= 🎯 RARITY SYSTEM =================

    private static String rollRarity(Player p, int tier) {

        int spins = pity.getOrDefault(p.getUniqueId(), 0) + 1;
        pity.put(p.getUniqueId(), spins);

        double common, rare, legendary, mythic;

        if (tier == 1) {
            common = 0.80;
            rare = 0.18;
            legendary = 0.02;
            mythic = 0.0;
        } else if (tier == 2) {
            common = 0.70;
            rare = 0.25;
            legendary = 0.045;
            mythic = 0.005;
        } else {
            common = 0.60;
            rare = 0.30;
            legendary = 0.08;
            mythic = 0.02;
        }

        double boost = spins * 0.0005;
        mythic += boost;
        legendary += boost * 0.5;

        if (spins >= 50) {
            pity.put(p.getUniqueId(), 0);
            return (r.nextDouble() < 0.7) ? "LEGENDARY" : "MYTHIC";
        }

        double roll = r.nextDouble();

        if (roll < common) return "COMMON";
        else if (roll < common + rare) return "RARE";
        else if (roll < common + rare + legendary) {
            pity.put(p.getUniqueId(), 0);
            return "LEGENDARY";
        } else {
            pity.put(p.getUniqueId(), 0);
            return "MYTHIC";
        }
    }

    // ================= 🎞️ STRIP =================

    private static List<ItemStack> generateStrip(String result) {

        List<ItemStack> strip = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            strip.add(getRandomPane());
        }

        // place result so it lands center
        strip.set(20 + 4, getPane(result));

        return strip;
    }

    // ================= 🎉 FINISH =================

    private static void finishRoll(Player p, Inventory gui, String rarity) {

        int spins = pity.getOrDefault(p.getUniqueId(), 0);

        gui.setItem(13, getPane(rarity));

        // sounds
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

        // pity feedback
        if (rarity.equals("COMMON") || rarity.equals("RARE")) {

            if (spins >= 40) {
                p.sendMessage("§cYour luck is peaking... (" + spins + "/50)");
            } else {
                p.sendMessage("§7Pity: §e" + spins + "/50");
            }

        } else {
            p.sendMessage("§6Pity reset.");
        }

        ItemStack item = AbilityItemUtil.createAbilityItem(ability);
        AbilityHotbarManager.giveAbility(p, item);
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
            case "COMMON" -> Material.GREEN_WOOL;
            case "RARE" -> Material.BLUE_WOOL;
            case "LEGENDARY" -> Material.YELLOW_WOOL;
            case "MYTHIC" -> Material.RED_WOOL;
            default -> Material.GRAY_WOOL;
        };

        return new ItemStack(mat);
    }
}