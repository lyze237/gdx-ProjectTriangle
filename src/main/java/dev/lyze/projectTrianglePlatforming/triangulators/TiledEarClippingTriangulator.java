package dev.lyze.projectTrianglePlatforming.triangulators;

import com.badlogic.gdx.math.EarClippingTriangulator;
import dev.lyze.projectTrianglePlatforming.utils.PolygonUtils;

public class TiledEarClippingTriangulator implements ITriangulator {
    private final EarClippingTriangulator triangulator = new EarClippingTriangulator();

    @Override
    public float[] triangulate(float[] vertices) {
        return PolygonUtils.applyIndexes(vertices, triangulator.computeTriangles(vertices));
    }
}
