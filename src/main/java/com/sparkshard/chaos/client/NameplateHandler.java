package com.sparkshard.chaos.client;

import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import org.joml.Vector3f;

public class NameplateHandler {

    /**
     * Spawns a high-quality text bar that scales with the length of the name.
     */
    public static void spawnChaosNameplate(Level level, BlockPos pos, String nameText) {
        // Create the 1.21.1 Text Display Entity
        Display.TextDisplay textDisplay = EntityType.TEXT_DISPLAY.create(level);
        if (textDisplay == null) return;

        // 1. Position it 0.6 blocks above the center of the block
        textDisplay.setPos(pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5);

        // 2. Set the content (Supports your long airplane fart prefix)
        textDisplay.setText(Component.literal(nameText));

        // 3. THE MAGIC SETTINGS: Scaling & Extension
        // We set the line width extremely high so it doesn't wrap unless we want it to.
        textDisplay.setLineWidth(1000); 
        
        // Make the background slightly transparent gray for that "Name Tag" look
        textDisplay.setBackgroundColor(0x40000000); 
        
        // Ensure it always faces the player/camera
        textDisplay.setBillboardConstraints(Display.BillboardConstraints.CENTER);

        // Scale it down slightly so long names don't cover the whole screen
        textDisplay.setScale(new Vector3f(0.8f, 0.8f, 0.8f));

        level.addFreshEntity(textDisplay);
    }
}
