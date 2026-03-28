# 🌪️ Minecraft Chaos Civilization Sim: 100 AI vs. The Apocalypse
**100 Autonomous Agents. 10 World-Ending Disasters. Zero Scarcity. One Winner.**

This repository contains the source code for a custom Fabric mod designed for high-stakes social experiments in Minecraft. Watch as 100 AI players form civilizations, build "Super-Bunkers," and attempt to survive both natural disasters and tactical raids.

---

## 🏗️ Project Status: [BETA - 1.21.1]
We are currently porting the AI logic to Minecraft 1.21.1 using **Fabric Loom 1.7**. The simulation is optimized for high-entity counts and custom Baritone pathfinding.

---

## 🎬 The Experiment
In this world, scarcity is a myth. Every chest is stocked with Enchanted Netherite and TNT. The AI players aren't fighting for food—they are fighting to survive the **Chaos Events** triggered via the Disaster Console.

### 🎭 The "Everything Has a Name" System
To make the simulation feel alive, **nothing is anonymous**:
- **Dynamic Naming:** Players receive IDs (`Person_67`), Real names (`Alice`), or "Chaos Names" (e.g., `Wait_Wheres_The_Roof`).
- **The Legend of Bob:** A rare (1%) "Bob" entity may spawn. Bob ignores panic logic, focuses entirely on mining, and has a statistically higher survival rate than "intelligent" agents.
- **Sentient Blocks:** Every block placed by an AI is tracked and named (e.g., `Gary the Glass`, `Load-Bearing Dirt`).

---

## ⚡ The Disaster Console (`/disaster <name>`)
Used in Spectator Mode to stress-test AI bunker logic:

| Command | Type | Threat Level | logic |
| :--- | :--- | :--- | :--- |
| `/disaster tornado` | Physics | **High** | Rips blocks into the sky; tests structural integrity. |
| `/disaster meteor_shower` | Explosive | **Extreme** | Constant bombardment from the sky. |
| `/disaster tsunami` | Fluid | **High** | Massive water wall; requires airtight sealing. |
| `/disaster earthquake` | Terrain | **Medium** | Opens fissures that split bunkers in half. |
| `/disaster mafia` | Tactical | **High** | Vindicators in suits attempting to breach doors. |
| `/disaster fbi` | Tactical | **Extreme** | Pillagers with high-accuracy "Rifles" and TNT. |
| `/disaster sinkhole` | Terrain | **Extreme** | The ground vanishes under the densest player cluster. |
| `/disaster acid_rain` | Status | **Low** | Chip damage to any entity without a roof. |

---

## 🤖 AI Logic: "Super-Building"
Powered by a custom **Baritone API** bridge, agents follow a three-stage survival cycle:
1. **The Scramble:** Identifying high-value pre-built structures or looting "City Chests."
2. **Fortification:** Upon disaster detection, agents enter "Super-Build" mode, prioritizing Obsidian and Stone to seal themselves in.
3. **The Breach:** During Tactical Raids (Mafia/FBI), agents move to "Bottle-neck" positions to defend entrances.

---

## 🛠️ Tech Stack & Requirements
- **Version:** Minecraft 1.21.1 (Fabric)
- **Tooling:** Gradle 8.7 / Java 21
- **Key Dependencies:**
    - **Weather2:** Advanced Tornado/Hurricane physics.
    - **Baritone API:** Autonomous pathfinding and building.
    - **The Lost Cities:** Pre-generated "Wasteland" cityscapes.
    - **Iris/Sodium:** Essential for maintaining 60FPS with 100+ active agents.

---

## 📝 NOTE: There are a lot of particles and entities that will be spawning, to not risk overloading the device, sodium is required. And I was 40k feet in the air above las vegas while doing this lol🤣

## 🚀 Development Quick Start
If you are building this in a **GitHub Codespace**:
1. Open the Terminal.
2. Run `./gradlew build` to compile the mod.
3. Find your compiled `.jar` in `build/libs/`.

---

## 📄 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
