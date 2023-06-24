package dev.lyze.projectTrianglePlatforming.triangulators;

import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.utils.ShortArray;

public class TiledEarClippingTriangulator implements ITriangulator {
    private final EarClippingTriangulator triangulator = new EarClippingTriangulator();

    @Override
    public ShortArray triangulate(float[] vertices) {
        return triangulator.computeTriangles(vertices);
    }
}
