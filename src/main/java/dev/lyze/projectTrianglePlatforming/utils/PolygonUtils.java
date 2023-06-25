package dev.lyze.projectTrianglePlatforming.utils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ShortArray;
import dev.lyze.projectTrianglePlatforming.triangulators.ITriangulator;
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

    public Array<float[]> triangulate(float[] vertices, ITriangulator triangulator) {
        var triangulatedVertices = triangulator.triangulate(vertices);

        return convertToTriangles(triangulatedVertices);
    }

    public float[] applyIndexes(float[] vertices, ShortArray indexes) {
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

    // chat gpt
    public boolean isConcave(float[] polygon) {
        int n = polygon.length / 2;

        if (n < 3) {
            // A polygon with less than 3 vertices is neither concave nor convex
            return false;
        }

        boolean isClockwise = false;
        boolean isCounterClockwise = false;

        for (int i = 0; i < n; i++) {
            int prevIndex = (i + n - 1) % n;
            int currentIndex = i;
            int nextIndex = (i + 1) % n;

            float crossProduct = crossProduct(
                    polygon[prevIndex * 2], polygon[prevIndex * 2 + 1],
                    polygon[currentIndex * 2], polygon[currentIndex * 2 + 1],
                    polygon[nextIndex * 2], polygon[nextIndex * 2 + 1]);

            if (crossProduct > 0) {
                isCounterClockwise = true;
            } else if (crossProduct < 0) {
                isClockwise = true;
            }

            if (isClockwise && isCounterClockwise) {
                // The polygon has both clockwise and counterclockwise turns, so it is concave
                return true;
            }
        }

        // If the polygon is either entirely clockwise or entirely counterclockwise, it is convex
        return false;
    }

    private float crossProduct(float ax, float ay, float bx, float by, float cx, float cy) {
        // Calculate the cross product of vectors AB and BC
        return (bx - ax) * (cy - by) - (by - ay) * (cx - bx);
    }
}
