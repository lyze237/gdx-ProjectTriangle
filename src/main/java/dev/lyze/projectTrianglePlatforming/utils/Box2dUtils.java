package dev.lyze.projectTrianglePlatforming.utils;

import com.badlogic.gdx.physics.box2d.*;
import dev.lyze.projectTrianglePlatforming.BodyFixtureOptions;
import lombok.experimental.UtilityClass;
import lombok.var;

@UtilityClass
public class Box2dUtils {
    public Body createBody(World world, BodyFixtureOptions options) {
        var body = world.createBody(options.toBodyDef());
        body.setUserData(options.getOwner());
        return body;
    }

    public Fixture createFixture(Body body, Shape shape, BodyFixtureOptions options) {
        var fixtureDef = options.toFixtureDef();
        fixtureDef.shape = shape;

        var fixture = body.createFixture(fixtureDef);
        fixture.setUserData(options.getOwner());
        return fixture;
    }
}
