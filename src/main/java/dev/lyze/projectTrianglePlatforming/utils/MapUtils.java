package dev.lyze.projectTrianglePlatforming.utils;

import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import lombok.experimental.UtilityClass;
import lombok.var;

@UtilityClass
public class MapUtils {
    public void extractPolygon(World world, float[] vertices) {
        var body = Box2dUtils.createStaticBody(world);

        if (vertices.length / 2 <= 8) {
            createPolygonShape(body, vertices);
        } else {
            var triangles = PolygonUtils.triangulate(vertices);

            for (float[] triangle : triangles)
                createPolygonShape(body, triangle);
        }
    }

    private void createPolygonShape(Body body, float[] vertices) {
        var shape = new PolygonShape();
        shape.set(vertices);

        Box2dUtils.createFixture(body, shape);
        shape.dispose();
    }

    public void extractPolyline(World world, float[] vertices) {
        var body = Box2dUtils.createStaticBody(world);

        var shape = new ChainShape();
        shape.createChain(vertices);

        Box2dUtils.createFixture(body, shape);
        shape.dispose();
    }

    public static void extractRectangle(World world, float x, float y, float width, float height) {
        var bodyDef = Box2dUtils.createStaticBodyDef();
        bodyDef.position.set(x, y);

        var body = world.createBody(bodyDef);

        var shape = new PolygonShape();
        shape.setAsBox(width / 2f, height / 2f);

        Box2dUtils.createFixture(body, shape);
        shape.dispose();
    }

    public static void extractCircle(World world, float x, float y, float radius) {
        var bodyDef = Box2dUtils.createStaticBodyDef();
        bodyDef.position.set(x, y);

        var body = world.createBody(bodyDef);

        var shape = new CircleShape();
        shape.setRadius(radius);

        Box2dUtils.createFixture(body, shape);
        shape.dispose();
    }

}
