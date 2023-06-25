package dev.lyze.projectTrianglePlatforming.triangulators;

import com.badlogic.gdx.math.DelaunayTriangulator;
import dev.lyze.projectTrianglePlatforming.utils.PolygonUtils;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TiledDelaunayTriangulator implements ITriangulator {
    private final DelaunayTriangulator triangulator = new DelaunayTriangulator();

    @Override
    public float[] triangulate(float[] vertices) {
        return PolygonUtils.applyIndexes(vertices, triangulator.computeTriangles(vertices, false));
    }
}
