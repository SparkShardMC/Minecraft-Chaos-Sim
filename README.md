# 🌪️ Minecraft Chaos Civilization Sim: 100 AI vs. The Apocalypse
**100 Autonomous Agents. 10 World-Ending Disasters. Zero Scarcity. One Winner.**

This repository contains the source code for a custom NeoForge mod designed for high-stakes social experiments in Minecraft. Watch as 100 AI players form civilizations, build "Super-Bunkers," and attempt to survive both natural disasters and tactical raids.

---

## 🎬 The Experiment
I have removed all resource scarcity. Every chest is filled with Enchanted Netherite and TNT. The AI players aren't fighting for survival against hunger—they are fighting for survival against **ME**. 

Using custom commands, I trigger 10 distinct disasters to test the "Super-Building" logic of the agents.

### 🎭 The "Everything Has a Name" System
In this simulation, **nothing is anonymous**. 
- **Players:** Named using a mix of Serial IDs (`Person_67`), Real names (`Alice`, `Kevin`), and Chaos names (`CreeperMagnet`, `Wait_Wheres_The_Roof`). And some super short names like (`The one that did a big airplane fart and disturbed the entire world and the fart was so loud it was louder than krakatoa and it smeeled so bad people couldnt smeel it also SUBSCRIBE`)
- **The Legend of Bob:** There is a 1% chance for a "Bob" to spawn. Bob is immune to panic logic. Bob just mines. Bob is eternal.
- **Sentient Blocks:** Every block placed by an AI is assigned a name (e.g., `Load-Bearing Dirt`, `Gary the Glass`).

---

## ⚡ The Disaster Console (`/disaster <name>`)
Triggered manually in Spectator Mode to test bunker integrity:

| Command | Type | Threat Level |
| :--- | :--- | :--- |
| `/disaster tornado` | Physics | **High** - Rips non-reinforced blocks into the sky. |
| `/disaster meteor_shower` | Explosive | **Extreme** - Constant bombardment from above. |
| `/disaster tsunami` | Fluid | **High** - Massive water wall; requires airtight seals. |
| `/disaster earthquake` | Terrain | **Medium** - Opens fissures that split bunkers in half. |
| `/disaster mafia` | Tactical | **High** - 30+ Vindicators in suits attempting to breach doors. |
| `/disaster fbi` | Tactical | **Extreme** - Pillagers with high-accuracy "Rifles" and TNT. |
| `/disaster firestorm` | Thermal | **Medium** - Rapid fire spread and lightning strikes. |
| `/disaster acid_rain` | Status | **Low** - Constant chip damage to anyone without a roof. |
| `/disaster sinkhole` | Terrain | **Extreme** - The ground vanishes under the densest player cluster. |
| `/disaster hurricane` | Weather | **Medium** - Zero visibility and extreme knockback. |

---

## 🤖 AI Logic: "Super-Building"
The agents are powered by a custom **Baritone API** bridge. Their behavior cycles through:
1. **The Scramble:** Identifying high-value pre-built bunkers or looting city chests.
2. **Fortification:** When a disaster is detected, agents switch to "Super-Build" mode, prioritizing Obsidian and Stone to seal themselves in.
3. **The Breach:** If the **FBI** or **Mafia** spawn, agents move to "Bottle-neck" positions to defend their bunker entrances.

---

## 🛠️ Installation & Tech Stack
- **Version:** Minecraft 1.21.x (NeoForge)
- **Key Mods Integrated:**
    - **Weather2:** For Tornado/Hurricane physics.
    - **The Lost Cities:** For the "Wasteland" city generation.
    - **Replay Mod:** Optimized for capturing 100+ entities without lag.

### 📸 Recording for Replay Mod
If you are using this mod for video creation:
1. Enable **"Show Nameplates"** in Replay settings.
2. Use **Spectator Mode** to fly through walls—all "Super-Building" is visible from the inside!
3. Look for **Bob**. If you find him, follow him. He usually survives the longest.

---

## 📄 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
