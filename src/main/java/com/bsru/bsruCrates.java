package com.bsru;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent; // [เพิ่ม] import ที่ต้องใช้
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class bsruCrates extends JavaPlugin implements TabExecutor, Listener {

    private FileConfiguration cratesConfig;
    private File cratesFile;
    private FileConfiguration locationsConfig;
    private File locationsFile;
    private FileConfiguration playerDataConfig;
    private File playerDataFile;

    private static final NamespacedKey KEY_TYPE_NBT = new NamespacedKey("bsrucrates", "key_type");
    private static final NamespacedKey ACTION_NBT = new NamespacedKey("bsrucrates", "action");

    @Override
    public void onEnable() {
        loadConfigs();
        getServer().getPluginManager().registerEvents(this, this);
        Objects.requireNonNull(getCommand("bsrucrates")).setExecutor(this);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new CratePlaceholders(this).register();
            getLogger().info("Successfully hooked into PlaceholderAPI!");
        }
        getLogger().info("bsruCrates has been enabled!");
    }

    public void loadConfigs() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        reloadConfig();

        cratesFile = new File(getDataFolder(), "crates.yml");
        if (!cratesFile.exists()) saveResource("crates.yml", false);
        cratesConfig = YamlConfiguration.loadConfiguration(cratesFile);

        locationsFile = new File(getDataFolder(), "locations.yml");
        if (!locationsFile.exists()) {
            try { locationsFile.createNewFile(); } catch (IOException e) { e.printStackTrace(); }
        }
        locationsConfig = YamlConfiguration.loadConfiguration(locationsFile);

        playerDataFile = new File(getDataFolder(), "playerdata.yml");
        if (!playerDataFile.exists()) {
            try { playerDataFile.createNewFile(); } catch (IOException e) { e.printStackTrace(); }
        }
        playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);
    }

    private void saveConfig(FileConfiguration config, File file) {
        try {
            config.save(file);
        } catch (IOException e) {
            getLogger().severe("Could not save file: " + file.getName());
        }
    }

    // --- Player Points Data Management ---
    public int getPoints(UUID uuid, String crateType) {
        return playerDataConfig.getInt(uuid + "." + crateType.toLowerCase(), 0);
    }

    public void setPoints(UUID uuid, String crateType, int amount) {
        playerDataConfig.set(uuid + "." + crateType.toLowerCase(), amount);
        saveConfig(playerDataConfig, playerDataFile);
    }

    public void addPoints(UUID uuid, String crateType, int amount) {
        int currentPoints = getPoints(uuid, crateType);
        setPoints(uuid, crateType, currentPoints + amount);
    }

    public void takePoints(UUID uuid, String crateType, int amount) {
        int currentPoints = getPoints(uuid, crateType);
        setPoints(uuid, crateType, Math.max(0, currentPoints - amount));
    }

    // --- Command and Tab-Completion ---
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player p)) {
                sender.sendMessage("Please use /bsrucrates help to see available commands.");
                return true;
            }
            p.sendMessage(formatColor("&8&m----------------------------------"));
            p.sendMessage(formatColor("&b&l         BsruCrates Plugin"));
            p.sendMessage(formatColor(""));
            p.sendMessage(formatColor("&e  A flexible crate system with physical"));
            p.sendMessage(formatColor("&e  keys and a virtual point system."));
            p.sendMessage(formatColor(""));
            p.sendMessage(formatColor("&e  Created by: &fNattapat2871"));
            p.sendMessage(formatColor("&e  GitHub: &fgithub.com/Nattapat2871/BsruCrates"));
            p.sendMessage(formatColor(""));
            p.sendMessage(formatColor("&7  Use &a/bsrucrates help &7for a list of all commands."));
            p.sendMessage(formatColor("&8&m----------------------------------"));
            return true;
        }

        if (!sender.hasPermission("bsrucrates.admin")) {
            sender.sendMessage(getMessage("no_permission"));
            return true;
        }

        String subCommand = args[0].toLowerCase();
        switch (subCommand) {
            case "key" -> handleKeyCommand(sender, args);
            case "set" -> handleSetCommand(sender, args);
            case "remove" -> handleRemoveCommand(sender);
            case "additem" -> handleItemCommand(sender, args, true);
            case "removeitem" -> handleItemCommand(sender, args, false);
            case "points" -> handlePointsCommand(sender, args);
            case "reload" -> {
                loadConfigs();
                sender.sendMessage(getMessage("reload_success"));
            }
            case "help" -> {
                sender.sendMessage(formatColor("&8&m---------- &b&lBsruCrates Help &8&m----------"));
                sender.sendMessage(formatColor("&e/bsrucrates &7- Shows plugin info."));
                sender.sendMessage(formatColor("&e/bsrucrates help &7- Shows this help message."));
                sender.sendMessage(formatColor("&e/bsrucrates reload &7- Reloads all config files."));
                sender.sendMessage(formatColor("&e/bsrucrates key give <player> <type> [amount] &7- Gives a crate key."));
                sender.sendMessage(formatColor("&e/bsrucrates points <give|set|take> <player> <type> <amount> &7- Manages player points."));
                sender.sendMessage(formatColor("&e/bsrucrates set <type> &7- Sets the target block as a crate."));
                sender.sendMessage(formatColor("&e/bsrucrates remove &7- Removes the target crate block."));
                sender.sendMessage(formatColor("&e/bsrucrates additem <type> <slot> &7- Adds the item in your hand to a crate."));
                sender.sendMessage(formatColor("&e/bsrucrates removeitem <type> <slot> &7- Removes an item from a crate."));
                sender.sendMessage(formatColor("&8&m---------------------------------------------"));
            }
            default -> sender.sendMessage(ChatColor.GOLD + "Unknown command. Use /bsrucrates help.");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("bsrucrates.admin")) return Collections.emptyList();

        final List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            suggestions.addAll(List.of("key", "set", "remove", "additem", "removeitem", "points", "reload", "help"));
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "key" -> suggestions.add("give");
                case "points" -> suggestions.addAll(List.of("give", "set", "take"));
                case "set", "additem", "removeitem" -> {
                    ConfigurationSection cs = cratesConfig.getConfigurationSection("crates");
                    if (cs != null) suggestions.addAll(cs.getKeys(false));
                }
            }
        } else if (args.length == 3 && (args[0].equalsIgnoreCase("key") || args[0].equalsIgnoreCase("points"))) {
            Bukkit.getOnlinePlayers().forEach(p -> suggestions.add(p.getName()));
        } else if (args.length == 4 && (args[0].equalsIgnoreCase("key") || args[0].equalsIgnoreCase("points"))) {
            ConfigurationSection cs = cratesConfig.getConfigurationSection("crates");
            if (cs != null) suggestions.addAll(cs.getKeys(false));
        }

        final List<String> completions = new ArrayList<>();
        StringUtil.copyPartialMatches(args[args.length - 1], suggestions, completions);
        Collections.sort(completions);
        return completions;
    }

    // --- Command Handlers ---
    private void handlePointsCommand(CommandSender sender, String[] args) {
        if (args.length == 1) {
            if (!(sender instanceof Player p)) {
                sender.sendMessage(ChatColor.RED + "The console must specify a player to check points: /bsrucrates points <give|set|take> ...");
                return;
            }
            p.sendMessage(formatColor("&8&m---------- &b&lYour Crate Points &8&m----------"));
            ConfigurationSection cratesSection = cratesConfig.getConfigurationSection("crates");
            if (cratesSection == null || cratesSection.getKeys(false).isEmpty()) {
                p.sendMessage(formatColor("&cThere are no crate types defined."));
                return;
            }
            boolean hasPoints = false;
            for (String crateType : cratesSection.getKeys(false)) {
                int points = getPoints(p.getUniqueId(), crateType);
                if (points > 0) {
                    p.sendMessage(formatColor("&e" + crateType + ": &f" + points + " Points"));
                    hasPoints = true;
                }
            }
            if (!hasPoints) {
                p.sendMessage(formatColor("&7You don't have any crate points."));
            }
            p.sendMessage(formatColor("&8&m---------------------------------------"));
            return;
        }

        if (args.length < 5) {
            sender.sendMessage(ChatColor.RED + "Usage: /bsrucrates points <give|set|take> <player> <type> <amount>");
            return;
        }
        String operation = args[1].toLowerCase();
        Player target = Bukkit.getPlayer(args[2]);
        if (target == null) {
            sender.sendMessage(getMessage("player_not_found").replace("{player}", args[2]));
            return;
        }
        String crateType = args[3].toLowerCase();
        if (cratesConfig.getConfigurationSection("crates." + crateType) == null) {
            sender.sendMessage(getMessage("crate_type_not_found").replace("{type}", args[3]));
            return;
        }
        int amount;
        try {
            amount = Integer.parseInt(args[4]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Amount must be a number.");
            return;
        }
        switch (operation) {
            case "give" -> {
                addPoints(target.getUniqueId(), crateType, amount);
                sender.sendMessage(ChatColor.GREEN + "Gave " + amount + " points for crate '" + crateType + "' to " + target.getName());
            }
            case "set" -> {
                setPoints(target.getUniqueId(), crateType, amount);
                sender.sendMessage(ChatColor.GREEN + "Set points for crate '" + crateType + "' to " + amount + " for " + target.getName());
            }
            case "take" -> {
                takePoints(target.getUniqueId(), crateType, amount);
                sender.sendMessage(ChatColor.GREEN + "Took " + amount + " points for crate '" + crateType + "' from " + target.getName());
            }
            default -> sender.sendMessage(ChatColor.RED + "Unknown operation. Use give, set, or take.");
        }
    }
    private void handleItemCommand(CommandSender sender, String[] args, boolean isAdding) {
        if (!(sender instanceof Player p)) { sender.sendMessage("This command must be run by a player."); return; }
        if (args.length < 3) { p.sendMessage(ChatColor.RED + "Usage: /bsrucrates " + (isAdding ? "additem" : "removeitem") + " <crate_type> <slot>"); return; }
        String crateType = args[1].toLowerCase();
        if (cratesConfig.getConfigurationSection("crates." + crateType) == null) { p.sendMessage(getMessage("crate_type_not_found").replace("{type}", args[1])); return; }
        int slot;
        try { slot = Integer.parseInt(args[2]); } catch (NumberFormatException e) { p.sendMessage(ChatColor.RED + "Slot must be a number."); return; }
        String path = "crates." + crateType + ".rewards." + slot;
        if (isAdding) {
            ItemStack itemInHand = p.getInventory().getItemInMainHand();
            if (itemInHand.getType() == Material.AIR) { p.sendMessage(ChatColor.RED + "You must be holding an item."); return; }
            cratesConfig.set(path + ".item", itemInHand);
            p.sendMessage(ChatColor.GREEN + "Added item to slot " + slot + " of crate '" + crateType + "'.");
        } else {
            if (!cratesConfig.contains(path)) { p.sendMessage(ChatColor.RED + "No item in slot " + slot + " of crate '" + crateType + "'."); return; }
            cratesConfig.set(path, null);
            p.sendMessage(ChatColor.GREEN + "Removed item from slot " + slot + " of crate '" + crateType + "'.");
        }
        saveConfig(cratesConfig, cratesFile);
    }
    private void handleKeyCommand(CommandSender sender, String[] args) {
        if (args.length < 4 || !args[1].equalsIgnoreCase("give")) { sender.sendMessage(ChatColor.GOLD + "Usage: /bsrucrates key give <player> <type> [amount]"); return; }
        Player target = Bukkit.getPlayer(args[2]);
        if (target == null) { sender.sendMessage(getMessage("player_not_found").replace("{player}", args[2])); return; }
        String crateType = args[3].toLowerCase();
        if (cratesConfig.getConfigurationSection("crates." + crateType) == null) { sender.sendMessage(getMessage("crate_type_not_found").replace("{type}", args[3])); return; }
        int amount = 1;
        if (args.length > 4) { try { amount = Integer.parseInt(args[4]); } catch (NumberFormatException e) { sender.sendMessage(ChatColor.RED + "Amount must be a number."); return; } }
        ItemStack key = getKey(crateType, amount);
        if (key == null) { sender.sendMessage(getMessage("crate_type_not_found").replace("{type}", args[3])); return; }
        HashMap<Integer, ItemStack> couldNotFit = target.getInventory().addItem(key);
        if (!couldNotFit.isEmpty()) { couldNotFit.values().forEach(item -> target.getWorld().dropItemNaturally(target.getLocation(), item)); sender.sendMessage(getMessage("inventory_full").replace("{player}", target.getName())); }
        String keyName = key.hasItemMeta() && key.getItemMeta().hasDisplayName() ? key.getItemMeta().getDisplayName() : crateType;
        sender.sendMessage(getMessage("key_given").replace("{amount}", String.valueOf(amount)).replace("{key_name}", keyName).replace("{player}", target.getName()));
        target.sendMessage(getMessage("key_received").replace("{amount}", String.valueOf(amount)).replace("{key_name}", keyName));
    }
    private void handleSetCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player p)) { sender.sendMessage("This command must be run by a player."); return; }
        if (args.length < 2) { p.sendMessage(ChatColor.RED + "Usage: /bsrucrates set <type>"); return; }
        String crateType = args[1].toLowerCase();
        if (cratesConfig.getConfigurationSection("crates." + crateType) == null) { p.sendMessage(getMessage("crate_type_not_found").replace("{type}", args[1])); return; }
        Block targetBlock = p.getTargetBlockExact(5);
        if (targetBlock == null) { p.sendMessage(getMessage("look_at_block")); return; }
        Location loc = targetBlock.getLocation();
        String locationString = String.format("%s;%d;%d;%d", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        locationsConfig.set(locationString, crateType);
        saveConfig(locationsConfig, locationsFile);
        p.sendMessage(getMessage("set_crate_success").replace("{type}", crateType));
    }
    private void handleRemoveCommand(CommandSender sender) {
        if (!(sender instanceof Player p)) { sender.sendMessage("This command must be run by a player."); return; }
        Block targetBlock = p.getTargetBlockExact(5);
        if (targetBlock == null) { p.sendMessage(getMessage("look_at_block")); return; }
        Location loc = targetBlock.getLocation();
        String locationString = String.format("%s;%d;%d;%d", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        if (!locationsConfig.contains(locationString)) { p.sendMessage(getMessage("not_a_crate")); return; }
        locationsConfig.set(locationString, null);
        saveConfig(locationsConfig, locationsFile);
        p.sendMessage(getMessage("remove_crate_success"));
    }

    // --- Event Handlers & GUI Logic ---

    // [เพิ่ม] Event Handler ใหม่สำหรับป้องกันการวางกุญแจ
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack itemInHand = event.getItemInHand();
        if (itemInHand.hasItemMeta()) {
            // ตรวจสอบว่าไอเทมมี NBT Tag ของเราฝังอยู่หรือไม่
            if (itemInHand.getItemMeta().getPersistentDataContainer().has(KEY_TYPE_NBT, PersistentDataType.STRING)) {
                // ถ้าใช่ ให้ยกเลิกการวาง
                event.setCancelled(true);
                Player player = event.getPlayer();
                player.sendMessage(getMessage("cannot_place_key")); // เราจะเพิ่มข้อความนี้ใน config.yml
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;
        Location loc = clickedBlock.getLocation();
        String locationString = String.format("%s;%d;%d;%d", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        if (locationsConfig.contains(locationString)) {
            event.setCancelled(true);
            Player player = event.getPlayer();
            if (!player.hasPermission("bsrucrates.use")) {
                player.sendMessage(getMessage("no_permission"));
                return;
            }
            String crateType = locationsConfig.getString(locationString);
            openCrateGUI(player, crateType);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        InventoryHolder holder = event.getClickedInventory().getHolder();
        if (holder instanceof CrateGUIHolder crateHolder) {
            event.setCancelled(true);
            playSound(player, "select_item");
            openConfirmationGUI(player, crateHolder.getCrateType(), event.getSlot());
        } else if (holder instanceof ConfirmationGUIHolder confirmHolder) {
            event.setCancelled(true);
            ItemMeta meta = clickedItem.getItemMeta();
            if (meta == null) return;
            String action = meta.getPersistentDataContainer().get(ACTION_NBT, PersistentDataType.STRING);
            if (action != null) {
                if (action.equals("confirm")) {
                    processCrateOpening(player, confirmHolder.getCrateType(), confirmHolder.getRewardSlot());
                    player.closeInventory();
                } else if (action.equals("cancel")) {
                    playSound(player, "cancel_purchase");
                    openCrateGUI(player, confirmHolder.getCrateType());
                }
            }
        }
    }

    private void openCrateGUI(Player player, String crateType) {
        String path = "crates." + crateType;
        String title = formatColor(cratesConfig.getString(path + ".gui_title", "&8Crate"));
        int rows = cratesConfig.getInt(path + ".gui_rows", 3);
        Inventory gui = Bukkit.createInventory(new CrateGUIHolder(crateType), rows * 9, title);
        ConfigurationSection rewardsSection = cratesConfig.getConfigurationSection(path + ".rewards");
        if (rewardsSection != null) {
            for (String slotStr : rewardsSection.getKeys(false)) {
                try {
                    int slot = Integer.parseInt(slotStr);
                    if (slot < gui.getSize()) {
                        ItemStack displayItem = cratesConfig.getItemStack(path + ".rewards." + slot + ".item");
                        if (displayItem != null) gui.setItem(slot, displayItem);
                    }
                } catch (NumberFormatException ignored) {}
            }
        }
        player.openInventory(gui);
    }

    private void openConfirmationGUI(Player player, String crateType, int rewardSlot) {
        ItemStack rewardDisplay = cratesConfig.getItemStack("crates." + crateType + ".rewards." + rewardSlot + ".item");
        if (rewardDisplay == null) {
            player.closeInventory();
            return;
        }
        Inventory confirmGui = Bukkit.createInventory(new ConfirmationGUIHolder(crateType, rewardSlot), 27, formatColor("&8Confirm Purchase"));
        confirmGui.setItem(13, rewardDisplay);

        ItemStack confirmButton = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta confirmMeta = confirmButton.getItemMeta();
        confirmMeta.setDisplayName(formatColor("&a&lCONFIRM"));
        confirmMeta.getPersistentDataContainer().set(ACTION_NBT, PersistentDataType.STRING, "confirm");
        confirmButton.setItemMeta(confirmMeta);

        ItemStack cancelButton = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta cancelMeta = cancelButton.getItemMeta();
        cancelMeta.setDisplayName(formatColor("&c&lCANCEL"));
        cancelMeta.getPersistentDataContainer().set(ACTION_NBT, PersistentDataType.STRING, "cancel");
        cancelButton.setItemMeta(cancelMeta);

        confirmGui.setItem(15, confirmButton);
        confirmGui.setItem(11, cancelButton);
        player.openInventory(confirmGui);
    }

    private void processCrateOpening(Player player, String crateType, int rewardSlot) {
        boolean requirementMet = false;
        ItemStack keyToCheck = getKey(crateType, 1);
        if (keyToCheck != null && player.getInventory().containsAtLeast(keyToCheck, 1)) {
            player.getInventory().removeItem(keyToCheck);
            requirementMet = true;
        } else {
            int cost = cratesConfig.getInt("crates." + crateType + ".point_cost", -1);
            if (cost >= 0 && getPoints(player.getUniqueId(), crateType) >= cost) {
                takePoints(player.getUniqueId(), crateType, cost);
                requirementMet = true;
            }
        }
        if (!requirementMet) {
            player.sendMessage(getMessage("no_key"));
            playSound(player, "no_key");
            return;
        }
        giveReward(player, crateType, rewardSlot);
    }

    private void giveReward(Player player, String crateType, int rewardSlot) {
        ItemStack rewardItem = cratesConfig.getItemStack("crates." + crateType + ".rewards." + rewardSlot + ".item");
        if (rewardItem != null) {
            HashMap<Integer, ItemStack> couldNotFit = player.getInventory().addItem(rewardItem.clone());
            if (!couldNotFit.isEmpty()) {
                couldNotFit.values().forEach(item -> player.getWorld().dropItemNaturally(player.getLocation(), item));
            }
            String rewardName = rewardItem.hasItemMeta() && rewardItem.getItemMeta().hasDisplayName() ? rewardItem.getItemMeta().getDisplayName() : rewardItem.getType().name();
            player.sendMessage(getMessage("reward_received").replace("{reward_name}", rewardName));
            playSound(player, "reward_received");
        }
    }

    // --- Utility Methods ---
    public int countPhysicalKeys(Player player, String crateType) {
        ItemStack keySample = getKey(crateType, 1);
        if (keySample == null) return 0;
        int count = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.isSimilar(keySample)) {
                count += item.getAmount();
            }
        }
        return count;
    }

    private ItemStack getKey(String crateType, int amount) {
        String path = "crates." + crateType.toLowerCase() + ".key_item";
        ConfigurationSection keySection = cratesConfig.getConfigurationSection(path);
        if (keySection == null) return null;

        String materialName = keySection.getString("material", "TRIPWIRE_HOOK");
        Material material = Material.getMaterial(materialName.toUpperCase());
        if (material == null) material = Material.TRIPWIRE_HOOK;

        ItemStack key = new ItemStack(material, amount);
        ItemMeta meta = key.getItemMeta();
        if (meta == null) return key;

        meta.setDisplayName(formatColor(keySection.getString("name", "Key")));
        meta.setLore(keySection.getStringList("lore").stream().map(this::formatColor).collect(Collectors.toList()));
        if (keySection.getBoolean("enchanted", false)) {
            meta.addEnchant(Enchantment.LURE, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        meta.getPersistentDataContainer().set(KEY_TYPE_NBT, PersistentDataType.STRING, crateType.toLowerCase());
        key.setItemMeta(meta);
        return key;
    }

    private void playSound(Player player, String configKey) {
        String soundName = getConfig().getString("sounds." + configKey);
        if (soundName == null || soundName.isEmpty()) return;
        try {
            Sound sound = Sound.valueOf(soundName.toUpperCase().replace(".", "_"));
            player.playSound(player.getLocation(), sound, SoundCategory.MASTER, 1.0f, 1.0f);
        } catch (IllegalArgumentException e) {
            getLogger().warning("Invalid sound name in config.yml for key 'sounds." + configKey + "': " + soundName);
        }
    }

    private static class CrateGUIHolder implements InventoryHolder {
        private final String crateType;
        public CrateGUIHolder(String crateType) { this.crateType = crateType; }
        public String getCrateType() { return crateType; }
        @Override public Inventory getInventory() { return null; }
    }

    private static class ConfirmationGUIHolder implements InventoryHolder {
        private final String crateType;
        private final int rewardSlot;
        public ConfirmationGUIHolder(String crateType, int rewardSlot) { this.crateType = crateType; this.rewardSlot = rewardSlot; }
        public String getCrateType() { return crateType; }
        public int getRewardSlot() { return rewardSlot; }
        @Override public Inventory getInventory() { return null; }
    }

    private String getMessage(String path) {
        String message = getConfig().getString("messages." + path, "&cMessage not found: " + path);
        String prefix = getConfig().getString("messages.prefix", "");
        return formatColor(prefix + message);
    }
    private String formatColor(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}