package dev.lyze.projectTrianglePlatforming.utils;

import com.badlogic.gdx.physics.box2d.*;
import dev.lyze.projectTrianglePlatforming.BodyFixtureOptions;
import dev.lyze.projectTrianglePlatforming.triangulators.ITriangulator;
import lombok.experimental.UtilityClass;
import lombok.var;

@UtilityClass
public class MapUtils {
    public void extractPolygon(World world, float[] vertices, ITriangulator triangulator, BodyFixtureOptions options) {
        var body = Box2dUtils.createBody(world, options);
        body.setUserData(options);

        var simplifiedVertices = PolygonUtils.simplifyPolygon(vertices);

        if (simplifiedVertices.length / 2 <= 8 && !PolygonUtils.isConcave(simplifiedVertices)) {
            createPolygonShape(body, simplifiedVertices, options);
        } else {
            var triangles = PolygonUtils.triangulate(simplifiedVertices, triangulator);

            for (int i = 0; i < triangles.size; i++) {
                float[] floats = triangles.get(i);
                if (PolygonUtils.areCollinear(floats[0], floats[1], floats[2], floats[3], floats[4], floats[5]))
                    continue;

                createPolygonShape(body, floats, options);
            }
        }
    }

    private void createPolygonShape(Body body, float[] vertices, BodyFixtureOptions options) {
        var shape = new PolygonShape();
        shape.set(vertices);

        var fixture = Box2dUtils.createFixture(body, shape, options);
        fixture.setUserData(options.getOwner());
        shape.dispose();
    }

    public void extractPolyline(World world, float[] vertices, BodyFixtureOptions options) {
        var body = Box2dUtils.createBody(world, options);
        body.setUserData(options.getOwner());

        var shape = new ChainShape();
        shape.createChain(vertices);

        var fixture = Box2dUtils.createFixture(body, shape, options);
        fixture.setUserData(options.getOwner());
        shape.dispose();
    }

    public static void extractRectangle(World world, float x, float y, float width, float height, BodyFixtureOptions options) {
        var bodyDef = options.toBodyDef();
        bodyDef.position.set(x + width / 2f, y + height / 2f);

        var body = world.createBody(bodyDef);
        body.setUserData(options.getOwner());

        var shape = new PolygonShape();
        shape.setAsBox(width / 2f, height / 2f);

        var fixture = Box2dUtils.createFixture(body, shape, options);
        fixture.setUserData(options.getOwner());
        shape.dispose();
    }

    public static void extractCircle(World world, float x, float y, float radius, BodyFixtureOptions options) {
        var bodyDef = options.toBodyDef();
        bodyDef.position.set(x, y);

        var body = world.createBody(bodyDef);
        body.setUserData(options.getOwner());

        var shape = new CircleShape();
        shape.setRadius(radius);

        var fixture = Box2dUtils.createFixture(body, shape, options);
        fixture.setUserData(options.getOwner());
        shape.dispose();
    }
}
