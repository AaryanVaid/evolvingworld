package me.aaryan.evolvingworld.aura;

import me.aaryan.evolvingworld.util.ToolMasteryUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AuraManager {

    private final NamespacedKey KEY;

    public AuraManager(NamespacedKey key) {
        this.KEY = key;
    }

    public void tick(Player player) {

        ItemStack item = player.getInventory().getItemInMainHand();

        // check held item first
        if (item != null && ToolMasteryUtil.isMastered(item, KEY)) {
            spawnAura(player, item.getType());
            return;
        }

        // check armor
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor == null) continue;

            if (ToolMasteryUtil.isMastered(armor, KEY)) {
                spawnAura(player, armor.getType());
                return;
            }
        }
    }

    private void spawnAura(Player player, Material material) {

        Particle particle;
        Color color;

        if (material.toString().contains("DIAMOND")) {
            particle = Particle.DUST;
            color = Color.AQUA;
        }
        else if (material.toString().contains("NETHERITE")) {
            particle = Particle.DUST;
            color = Color.fromRGB(30, 0, 50);
        }
        else if (material.toString().contains("GOLD")) {
            particle = Particle.DUST;
            color = Color.YELLOW;
        }
        else if (material.toString().contains("IRON")) {
            particle = Particle.DUST;
            color = Color.fromRGB(200, 200, 200);
        }
        else {
            particle = Particle.ENCHANT;
            color = null;
        }

        Location loc = player.getLocation().add(0, 1, 0);

        if (particle == Particle.DUST) {
            player.getWorld().spawnParticle(
                    particle,
                    loc,
                    10,
                    0.4, 0.6, 0.4,
                    new Particle.DustOptions(color, 1.2F)
            );
        } else {
            player.getWorld().spawnParticle(
                    particle,
                    loc,
                    10,
                    0.4, 0.6, 0.4
            );
        }
    }
}