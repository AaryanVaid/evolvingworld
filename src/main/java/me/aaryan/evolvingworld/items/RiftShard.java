package me.aaryan.evolvingworld.items;

import me.aaryan.evolvingworld.EvolvingWorld;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Random;

public class RiftShard {

    private static NamespacedKey SHARD_KEY;
    private static final Random random = new Random();

    // 🔥 TYPES
    public enum ShardType {
        FIRE,
        VOID,
        STORM
    }

    // 🔧 INIT (call in onEnable)
    public static void init(EvolvingWorld plugin) {
        SHARD_KEY = new NamespacedKey(plugin, "rift_shard_type");
    }

    // 🔥 CREATE RANDOM SHARD
    public static ItemStack createRandom() {
        ShardType[] types = ShardType.values();
        ShardType type = types[random.nextInt(types.length)];
        return create(type);
    }

    // 🔥 CREATE SPECIFIC SHARD
    public static ItemStack create(ShardType type) {

        ItemStack item = new ItemStack(Material.AMETHYST_SHARD);
        ItemMeta meta = item.getItemMeta();

        // Name
        meta.setDisplayName(getColor(type) + type.name() + " Rift Shard");

        // Lore
        meta.setLore(List.of(
                "§7A fragment of " + type.name().toLowerCase() + " energy...",
                "§7Use to unlock powerful abilities"
        ));

        // Store type
        meta.getPersistentDataContainer().set(
                SHARD_KEY,
                PersistentDataType.STRING,
                type.name()
        );

        item.setItemMeta(meta);
        return item;
    }

    // 🔍 CHECK IF SHARD
    public static boolean isShard(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;

        return item.getItemMeta()
                .getPersistentDataContainer()
                .has(SHARD_KEY, PersistentDataType.STRING);
    }

    // 🔍 GET TYPE
    public static ShardType getType(ItemStack item) {
        if (!isShard(item)) return null;

        String type = item.getItemMeta()
                .getPersistentDataContainer()
                .get(SHARD_KEY, PersistentDataType.STRING);

        return ShardType.valueOf(type);
    }

    // 🎨 COLORS PER TYPE
    private static String getColor(ShardType type) {
        return switch (type) {
            case FIRE -> "§c";   // red
            case VOID -> "§5";   // purple
            case STORM -> "§b";  // cyan
        };
    }
}