package dev.lyze.projectTrianglePlatforming.utils;

import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ShortArray;
import lombok.experimental.UtilityClass;
import lombok.var;

@UtilityClass
public class PolygonUtils {
    public float[] transformVertices(float[] vertices, float scale) {
        return transformVertices(vertices, scale, 0, 0);
    }

    public float[] transformVertices(float[] vertices, float scale, float x, float y) {
        float[] newVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; i += 2) {
            newVertices[i] = vertices[i] * scale + x;
            newVertices[i + 1] = vertices[i + 1] * scale + y;
        }

        return newVertices;
    }

    public Array<float[]> triangulate(float[] vertices) {
        var triangulator = new EarClippingTriangulator();

        var indexes = triangulator.computeTriangles(vertices);
        var triangulatedVertices = applyIndexes(vertices, indexes);
        var triangles = convertToTriangles(triangulatedVertices);

        return triangles;
    }

    private float[] applyIndexes(float[] vertices, ShortArray indexes) {
        float[] newVertices = new float[indexes.size * 2];

        for (short i = 0; i < indexes.size; i++) {
            var index = indexes.get(i);
            newVertices[i * 2] = vertices[index * 2];
            newVertices[i * 2 + 1] = vertices[index * 2 + 1];
        }

        return newVertices;
    }

    private Array<float[]> convertToTriangles(float[] points) {
        Array<float[]> triangles = new Array<>();

        for (int i = 0; i < points.length; i += 6) {
            float[] triangle = new float[6];
            System.arraycopy(points, i, triangle, 0, triangle.length);
            triangles.add(triangle);
        }

        return triangles;
    }
}
