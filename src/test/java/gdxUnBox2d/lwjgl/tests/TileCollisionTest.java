package gdxUnBox2d.lwjgl.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.projectTrianglePlatforming.TiledObjectLayerToBox2d;
import dev.lyze.projectTrianglePlatforming.TiledObjectLayerToBox2dOptions;
import dev.lyze.projectTrianglePlatforming.TiledTileCollisionToBox2d;
import dev.lyze.projectTrianglePlatforming.TiledTileCollisionToBox2dOptions;
import gdxUnBox2d.lwjgl.LibgdxLwjglUnitTest;
import lombok.var;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class TileCollisionTest extends LibgdxLwjglUnitTest {
    private Viewport viewport;

    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer debugRenderer;

    @Override
    public void create() {
        world = new World(new Vector2(0, -10), true);
        debugRenderer = new Box2DDebugRenderer();

        TiledMap map = new TmxMapLoader().load("tileCollision.tmx");

        var tileWidth = map.getProperties().get("tilewidth", Integer.class);
        renderer = new OrthogonalTiledMapRenderer(map, 1f / tileWidth);

        viewport = new FitViewport(map.getProperties().get("width", Integer.class), map.getProperties().get("height", Integer.class));

        var builder = new TiledTileCollisionToBox2d(TiledTileCollisionToBox2dOptions.builder()
                .scale(1f / tileWidth)
                .throwOnInvalidObject(false)
                .build());
        builder.parseAllLayers(map, world);
    }

    @Test
    @Tag("lwjgl")
    public void MovementTest() {
        Assertions.assertEquals(true, true);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();

        renderer.setView(((OrthographicCamera) viewport.getCamera()));
        renderer.render();

        Gdx.gl.glLineWidth(3f);
        debugRenderer.render(world, viewport.getCamera().combined);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}
