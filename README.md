# 🔌 bsruCrates Plugin

<div align="center">

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![GitHub Repo stars](https://img.shields.io/github/stars/Nattapat2871/BsruCrates?style=flat-square)](https://github.com/Nattapat2871/BsruCrates/stargazers)
![Visitor Badge](https://api.visitorbadge.io/api/VisitorHit?user=Nattapat2871&repo=BsruCratess&countColor=%237B1E7A&style=flat-square)

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/Nattapat2871)

</div>

<p align= "center">
        <b>English</b>　<a href="/README_TH.md">ภาษาไทย</a>

A highly flexible and powerful crate system for modern Spigot/Paper servers. bsruCrates moves beyond traditional random gacha crates by allowing players to **choose their desired reward** from a GUI. It features a unique, dual-key system, allowing crates to be opened with either physical key items or a virtual point currency.

This plugin is designed for server owners who want to reward their players with valuable, specific items (including custom items from other plugins) in a balanced and engaging way.

---
## ✨ Features

- **Player-Choice Rewards:** Instead of random luck, players right-click a crate and choose the exact reward they want from a GUI.
- **Dual-Key System:** Every crate can be opened with one of two methods, with physical keys always taking priority:
    1.  **Physical Keys:** Unique, NBT-tagged items that cannot be faked. Admins can give these to players.
    2.  **Points System:** A virtual currency, stored per-player and per-crate-type. Ideal for rewards from voting, quests, or your server's economy.
- **In-Game Crate Editor:**
    - **`/bsrucrates additem <type> <slot>`:** Add any item to a crate's reward GUI directly from your hand, preserving all custom names, lore, enchantments, and NBT data from other plugins.
    - **`/bsrucrates removeitem <type> <slot>`:** Easily remove rewards from the GUI.
- **Two-Step Confirmation:** A confirmation screen prevents accidental purchases, ensuring players are certain about their choice.
- **Full Admin Control:** A comprehensive suite of commands to manage every aspect of the plugin:
    - Create/remove crate blocks in the world.
    - Give physical keys.
    - Manage player points (give/set/take).
    - Reload all configurations live.
- **Highly Configurable:** Customize all messages, GUI titles, and sound effects for a unique server experience.
- **PlaceholderAPI Support:** Display player key counts and points anywhere on your server.

---
## 🎮 Compatibility

- **Minecraft Version:** `1.18` - `1.21+`
- **Server Software:** Spigot, Paper, and their forks.

---
## 🛠️ Installation

1.  Download the latest `.jar` file from the [Releases](https://github.com/Nattapat2871/BsruCrates/releases) page.
2.  Place the downloaded `.jar` file into your server's `/plugins` directory.
3.  (Optional but Recommended) Install [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.624/).
4.  Start or restart your server.
5.  The plugin will generate `config.yml`, `crates.yml`, and other necessary files for you to configure.

---
## 📋 Commands & Permissions

All admin commands require the permission `bsrucrates.admin`. Player usage requires `bsrucrates.use`.

| Command | Description |
| :--- | :--- |
| `/bsrucrates` | Shows plugin information. |
| `/bsrucrates help` | Shows all plugin commands. |
| `/bsrucrates reload` | Reloads all config files. |
| `/bsrucrates key give <player> <type> [amount]` | Gives a physical crate key. |
| `/bsrucrates points <give\|set\|take> <player> <type> <amount>`| Manages a player's points for a crate type. |
| `/bsrucrates set <type>` | Sets the target block as a crate. |
| `/bsrucrates remove` | Removes the target crate block. |
| `/bsrucrates additem <type> <slot>` | Adds the item in your hand to a crate's GUI. |
| `/bsrucrates removeitem <type> <slot>` | Removes an item from a crate's GUI. |

---
## 🔌 Placeholders (PlaceholderAPI)

- `%bsrucrates_physicalkeys_<cratetype>%`
    - Displays the number of physical keys a player has for a specific crate type.
    - **Example:** `%bsrucrates_physicalkeys_vote%`

- `%bsrucrates_points_<cratetype>%`
    - Displays the number of points a player has for a specific crate type.
    - **Example:** `%bsrucrates_points_vote%`

---
## ⚙️ Configuration (`config.yml`)

This file controls all messages and sound effects for the plugin.

```yaml
messages:
  prefix: "&8[&bCrates&8] "
  no_permission: "&cYou don't have permission to use this command."
  player_not_found: "&cCould not find player '{player}'."
  crate_type_not_found: "&cCould not find a crate type named '{type}'."
  key_given: "&aYou have given {amount}x {key_name} &ato {player}."
  key_received: "&aYou have received {amount}x {key_name}&a!"
  inventory_full: "&c{player}'s inventory is full. The key(s) were dropped on the ground."
  set_crate_success: "&aSuccessfully set the block you are looking at as a '{type}' crate."
  remove_crate_success: "&aSuccessfully removed the crate at this location."
  not_a_crate: "&cThis block is not a crate."
  look_at_block: "&cYou must be looking at a block."
  no_key: "&cYou don't have the required key or enough points to open this crate!"
  reward_received: "&aYou received {reward_name}&a!"
  reload_success: "&aSuccessfully reloaded all configurations."

sounds:
  select_item: "ui.button.click"
  cancel_purchase: "entity.villager.no"
  reward_received: "entity.experience_orb.pickup"
  no_key: "entity.villager.no"
```

---

## 🧑‍💻 Author
* Nattapat2871
* GitHub: https://github.com/Nattapat2871/BsruCrates
