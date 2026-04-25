package me.aaryan.evolvingworld.ability;

import me.aaryan.evolvingworld.EvolvingWorld;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.scheduler.BukkitRunnable;

public class CooldownVisual {

    public static void start(Player p, ItemStack item, long cooldown) {

        new BukkitRunnable() {

            long start = System.currentTimeMillis();

            @Override
            public void run() {

                if (item == null || !item.hasItemMeta()) {
                    cancel();
                    return;
                }

                long now = System.currentTimeMillis();
                double progress = (double) (now - start) / cooldown;

                Damageable meta = (Damageable) item.getItemMeta();

                if (progress >= 1.0) {
                    meta.setDamage(0);
                    item.setItemMeta(meta);

                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 2f);
                    cancel();
                    return;
                }

                int max = item.getType().getMaxDurability();
                int damage = (int) (max - (max * progress));

                meta.setDamage(damage);
                item.setItemMeta(meta);
            }

        }.runTaskTimer(EvolvingWorld.getPlugin(EvolvingWorld.class), 0, 2);
    }
}