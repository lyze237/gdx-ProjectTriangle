package dev.lyze.projectTrianglePlatforming.utils;

import com.badlogic.gdx.physics.box2d.*;
import dev.lyze.projectTrianglePlatforming.triangulators.ITriangulator;
import lombok.experimental.UtilityClass;
import lombok.var;

@UtilityClass
public class MapUtils {
    public void extractPolygon(World world, float[] vertices, ITriangulator triangulator, String bodyType) {
        var body = Box2dUtils.createBody(world, bodyType);

        var simplifiedVertices = PolygonUtils.simplifyPolygon(vertices);

        if (simplifiedVertices.length / 2 <= 8 && !PolygonUtils.isConcave(simplifiedVertices)) {
            createPolygonShape(body, simplifiedVertices);
        } else {
            var triangles = PolygonUtils.triangulate(simplifiedVertices, triangulator);

            for (int i = 0; i < triangles.size; i++) {
                float[] floats = triangles.get(i);
                if (PolygonUtils.areCollinear(floats[0], floats[1], floats[2], floats[3], floats[4], floats[5]))
                    continue;

                createPolygonShape(body, floats);
            }
        }
    }

    private void createPolygonShape(Body body, float[] vertices) {
        var shape = new PolygonShape();
        shape.set(vertices);

        Box2dUtils.createFixture(body, shape);
        shape.dispose();
    }

    public void extractPolyline(World world, float[] vertices, String bodyType) {
        var body = Box2dUtils.createBody(world, bodyType);

        var shape = new ChainShape();
        shape.createChain(vertices);

        Box2dUtils.createFixture(body, shape);
        shape.dispose();
    }

    public static void extractRectangle(World world, float x, float y, float width, float height, String bodyType) {
        var bodyDef = Box2dUtils.createBodyDef(bodyType);
        bodyDef.position.set(x + width / 2f, y + height / 2f);

        var body = world.createBody(bodyDef);

        var shape = new PolygonShape();
        shape.setAsBox(width / 2f, height / 2f);

        Box2dUtils.createFixture(body, shape);
        shape.dispose();
    }

    public static void extractCircle(World world, float x, float y, float radius, String bodyType) {
        var bodyDef = Box2dUtils.createBodyDef(bodyType);
        bodyDef.position.set(x, y);

        var body = world.createBody(bodyDef);

        var shape = new CircleShape();
        shape.setRadius(radius);

        Box2dUtils.createFixture(body, shape);
        shape.dispose();
    }
}
