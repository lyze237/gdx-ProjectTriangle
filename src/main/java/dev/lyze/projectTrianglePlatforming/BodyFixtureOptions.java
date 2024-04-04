package dev.lyze.projectTrianglePlatforming;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import lombok.EqualsAndHashCode;
import lombok.var;

@EqualsAndHashCode
public class BodyFixtureOptions {
    public BodyFixtureOptions(MapProperties mapProperties) {
        type = BodyDef.BodyType.valueOf(mapProperties.get("ptpType", "StaticBody", String.class));
        angle = mapProperties.get("ptpAngle", 0f, Float.class);
        linearVelocity.x = mapProperties.get("ptpLinearVelocityX", 0f, Float.class);
        linearVelocity.y = mapProperties.get("ptpLinearVelocityY", 0f, Float.class);
        angularVelocity = mapProperties.get("ptpAngularVelocity", 0f, Float.class);
        linearDamping = mapProperties.get("ptpLinearDamping", 0f, Float.class);
        angularDamping = mapProperties.get("ptpAngularDamping", 0f, Float.class);
        allowSleep = mapProperties.get("ptpAllowSleep", true, Boolean.class);
        awake = mapProperties.get("ptpAwake", true, Boolean.class);
        fixedRotation = mapProperties.get("ptpFixedRotation", false, Boolean.class);
        bullet = mapProperties.get("ptpBullet", false, Boolean.class);
        active = mapProperties.get("ptpActive", true, Boolean.class);
        gravityScale = mapProperties.get("ptpGravityScale", 1f, Float.class);
        friction = mapProperties.get("ptpFriction", 0.2f, Float.class);
        restitution = mapProperties.get("ptpRestitution", 0f, Float.class);
        density = mapProperties.get("ptpDensity", 0f, Float.class);
        isSensor = mapProperties.get("ptpIsSensor", false, Boolean.class);
    }

    public BodyDef toBodyDef() {
        var bodyDef = new BodyDef();

        bodyDef.type = type;
        bodyDef.angle = angle;
        bodyDef.linearVelocity.set(linearVelocity);
        bodyDef.angularVelocity = angularVelocity;
        bodyDef.linearDamping = linearDamping;
        bodyDef.angularDamping = angularDamping;
        bodyDef.allowSleep = allowSleep;
        bodyDef.awake = awake;
        bodyDef.fixedRotation = fixedRotation;
        bodyDef.bullet = bullet;
        bodyDef.active = active;
        bodyDef.gravityScale = gravityScale;

        return bodyDef;
    }
    
    public FixtureDef toFixtureDef() {
        var fixtureDef = new FixtureDef();

        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        fixtureDef.density = density;
        fixtureDef.isSensor = isSensor;

        return fixtureDef;
    }

    /**
     * The body type: static, kinematic, or dynamic. Note: if a dynamic body would have zero mass, the mass is set to one.
     **/
    public BodyDef.BodyType type = BodyDef.BodyType.StaticBody;

    /**
     * The world angle of the body in radians.
     **/
    public float angle = 0;

    /**
     * The linear velocity of the body's origin in world co-ordinates.
     **/
    public final Vector2 linearVelocity = new Vector2();

    /**
     * The angular velocity of the body.
     **/
    public float angularVelocity = 0;

    /**
     * Linear damping is use to reduce the linear velocity. The damping parameter can be larger than 1.0f but the damping effect
     * becomes sensitive to the time step when the damping parameter is large.
     **/
    public float linearDamping = 0;

    /**
     * Angular damping is use to reduce the angular velocity. The damping parameter can be larger than 1.0f but the damping effect
     * becomes sensitive to the time step when the damping parameter is large.
     **/
    public float angularDamping = 0;

    /**
     * Set this flag to false if this body should never fall asleep. Note that this increases CPU usage.
     **/
    public boolean allowSleep = true;

    /**
     * Is this body initially awake or sleeping?
     **/
    public boolean awake = true;

    /**
     * Should this body be prevented from rotating? Useful for characters.
     **/
    public boolean fixedRotation = false;

    /**
     * Is this a fast moving body that should be prevented from tunneling through other moving bodies? Note that all bodies are
     * prevented from tunneling through kinematic and static bodies. This setting is only considered on dynamic bodies.
     *
     * @warning You should use this flag sparingly since it increases processing time.
     **/
    public boolean bullet = false;

    /**
     * Does this body start out active?
     **/
    public boolean active = true;

    /**
     * Scale the gravity applied to this body.
     **/
    public float gravityScale = 1;

    /** The friction coefficient, usually in the range [0,1]. **/
    public float friction = 0.2f;

    /** The restitution (elasticity) usually in the range [0,1]. **/
    public float restitution = 0;

    /** The density, usually in kg/m^2. **/
    public float density = 0;

    /** A sensor shape collects contact information but never generates a collision response. */
    public boolean isSensor = false;
}
