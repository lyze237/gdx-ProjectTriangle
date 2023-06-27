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
        var newVertices = new float[vertices.length];

        for (var i = 0; i < vertices.length; i += 2) {
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
        var newVertices = new float[indexes.size * 2];

        for (var i = 0; i < indexes.size; i++) {
            var index = indexes.get(i);
            newVertices[i * 2] = vertices[index * 2];
            newVertices[i * 2 + 1] = vertices[index * 2 + 1];
        }

        return newVertices;
    }

    private Array<float[]> convertToTriangles(float[] points) {
        var triangles = new Array<float[]>();

        for (int i = 0; i < points.length; i += 6) {
            var triangle = new float[6];
            System.arraycopy(points, i, triangle, 0, triangle.length);
            triangles.add(triangle);
        }

        return triangles;
    }

    // chat gpt
    public boolean isConcave(float[] polygon) {
        var n = polygon.length / 2;

        if (n < 3) {
            // A polygon with less than 3 vertices is neither concave nor convex
            return false;
        }

        var isClockwise = false;
        var isCounterClockwise = false;

        for (var currentIndex = 0; currentIndex < n; currentIndex++) {
            var prevIndex = (currentIndex + n - 1) % n;
            var nextIndex = (currentIndex + 1) % n;

            var crossProduct = crossProduct(
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
