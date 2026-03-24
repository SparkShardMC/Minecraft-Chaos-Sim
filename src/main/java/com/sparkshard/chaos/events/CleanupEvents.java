@SubscribeEvent
public void onBlockBreak(BlockEvent.BreakEvent event) {
    // Find any TextDisplay entities within a 0.5 radius of the broken block
    AABB area = new AABB(event.getPos()).inflate(0.1);
    List<Display.TextDisplay> tags = event.getLevel().getEntitiesOfClass(Display.TextDisplay.class, area);
    
    for (Display.TextDisplay tag : tags) {
        tag.discard(); // Remove the nameplate instantly
    }
}
