package dev.lyze.projectTrianglePlatforming.triangulators;

import com.badlogic.gdx.math.DelaunayTriangulator;
import com.badlogic.gdx.utils.ShortArray;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class TiledDelaunayTriangulator implements ITriangulator {
    private final DelaunayTriangulator triangulator = new DelaunayTriangulator();
    private boolean sorted = true;

    @Override
    public ShortArray triangulate(float[] vertices) {
        return triangulator.computeTriangles(vertices, sorted);
    }
}
