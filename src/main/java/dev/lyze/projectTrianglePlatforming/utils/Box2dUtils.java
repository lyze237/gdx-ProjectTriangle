package dev.lyze.projectTrianglePlatforming.utils;

import com.badlogic.gdx.physics.box2d.*;
import lombok.experimental.UtilityClass;
import lombok.var;

@UtilityClass
public class Box2dUtils {
    public BodyDef createStaticBodyDef() {
        var bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        return bodyDef;
    }

    public Body createStaticBody(World world) {
        var bodyDef = createStaticBodyDef();

        return world.createBody(bodyDef);
    }

    public Fixture createFixture(Body body, Shape shape) {
        var fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        return body.createFixture(fixtureDef);
    }
}
