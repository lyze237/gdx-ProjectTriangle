package gdxUnBox2d.lwjgl.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.projectTrianglePlatforming.*;
import gdxUnBox2d.lwjgl.LibgdxLwjglUnitTest;
import lombok.var;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class ObjectParserTest extends LibgdxLwjglUnitTest {
    private Viewport viewport;

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private ShapeRenderer shapeRenderer;
    private TiledTileCollisionToBox2d secondBuilder;

    @Override
    public void create() {
        world = new World(new Vector2(0, -10), true);
        debugRenderer = new Box2DDebugRenderer();

        map = new TmxMapLoader().load("tile.tmx");

        var tileWidth = map.getProperties().get("tilewidth", Integer.class);
        renderer = new OrthogonalTiledMapRenderer(map, 1f / tileWidth);

        viewport = new FitViewport(map.getProperties().get("width", Integer.class), map.getProperties().get("height", Integer.class));

        shapeRenderer = new ShapeRenderer();

        var builder = new TiledObjectLayerToBox2d(TiledObjectLayerToBox2dOptions.builder()
                .scale(1f / tileWidth)
                .throwOnInvalidObject(false)
                .build());
        //builder.parseAllLayers(map, world);

        secondBuilder = new TiledTileCollisionToBox2d(TiledTileCollisionToBox2dOptions.builder()
                .scale(1f / tileWidth)
                .build());

        //secondBuilder.parseLayer(((TiledMapTileLayer) map.getLayers().get("Tile Layer 2")), world);
        //secondBuilder.parseAllLayers(map, world);

        var thirdBuilder = new TiledTileLayerToBox2d(TiledTileLayerToBox2dOptions.builder()
                .scale(1f / tileWidth)
                .build());

        thirdBuilder.parseAllLayers(map, world);
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

        Gdx.gl.glLineWidth(4f);
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (float[] polygon : secondBuilder.getComputedPolygons()) {
            shapeRenderer.setColor(convertToColor(Arrays.hashCode(polygon)));
            shapeRenderer.polygon(polygon);
        }
        shapeRenderer.end();

        Gdx.gl.glLineWidth(2f);
        debugRenderer.render(world, viewport.getCamera().combined);
    }

    // chat gpt
    private static Color convertToColor(int hashCode) {
        // Generate normalized RGB values from the hash code
        float red = ((hashCode >> 16) & 0xFF) / 255.0f;
        float green = ((hashCode >> 8) & 0xFF) / 255.0f;
        float blue = (hashCode & 0xFF) / 255.0f;

        // Create a Color object from the normalized RGB values
        return new Color(red, green, blue, 1f);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}
