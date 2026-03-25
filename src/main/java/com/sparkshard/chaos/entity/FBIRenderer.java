public class FBIRenderer extends GeoEntityRenderer<EntityFBI> {
    public FBIRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new FBIModel());
    }
}
