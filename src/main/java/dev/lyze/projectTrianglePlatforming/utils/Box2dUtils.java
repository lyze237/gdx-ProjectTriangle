package dev.lyze.projectTrianglePlatforming.utils;

import com.badlogic.gdx.physics.box2d.*;
import lombok.experimental.UtilityClass;
import lombok.var;

@UtilityClass
public class Box2dUtils {
    public Body createBody(World world, String bodyType) {
        return world.createBody(createBodyDef(bodyType));
    }

    public BodyDef createBodyDef(String bodyType) {
        var bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.valueOf(bodyType);

        return bodyDef;
    }

    public Fixture createFixture(Body body, Shape shape) {
        var fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        return body.createFixture(fixtureDef);
    }
}
