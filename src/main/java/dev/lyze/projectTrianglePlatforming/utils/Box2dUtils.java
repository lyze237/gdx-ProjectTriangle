package dev.lyze.projectTrianglePlatforming.utils;

import com.badlogic.gdx.physics.box2d.*;
import dev.lyze.projectTrianglePlatforming.BodyFixtureOptions;
import lombok.experimental.UtilityClass;
import lombok.var;

@UtilityClass
public class Box2dUtils {
    public Body createBody(World world, BodyFixtureOptions options) {
        return world.createBody(options.toBodyDef());
    }

    public Fixture createFixture(Body body, Shape shape, BodyFixtureOptions options) {
        var fixtureDef = options.toFixtureDef();
        fixtureDef.shape = shape;

        return body.createFixture(fixtureDef);
    }
}
