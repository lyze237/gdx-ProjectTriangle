package dev.lyze.projectTrianglePlatforming;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import dev.lyze.projectTrianglePlatforming.utils.PolygonUtils;
import lombok.AllArgsConstructor;
import lombok.var;

@AllArgsConstructor
public class TiledTileCollisionToBox2d {
    private final TiledTileCollisionToBox2dOptions options;

    public void parseAllLayers(TiledMap map, World world) {
        for (MapLayer layer : map.getLayers())
            if (layer instanceof TiledMapTileLayer)
                parseLayer(((TiledMapTileLayer) layer), world);
    }

    private void parseLayer(TiledMapTileLayer layer, World world) {
        for (int x = 0; x < layer.getWidth(); x++)
            for (int y = 0; y < layer.getHeight(); y++)
                parseCell(layer, x, y, world);
    }

    private void parseCell(TiledMapTileLayer layer, int x, int y, World world) {
        var cell = layer.getCell(x, y);
        if (cell == null)
            return;

        for (var obj : layer.getObjects()) {
            if (obj instanceof EllipseMapObject) {
                var ellipse = ((EllipseMapObject) obj).getEllipse();
                if (ellipse.width != ellipse.height) {
                    if (options.isThrowOnInvalidObject())
                        throw new IllegalArgumentException("Ellipse objects not supported by Box2D");

                    return;
                }
                extractCircle(world, x, y, ellipse);
            } else if (obj instanceof RectangleMapObject) {
                extractRectangle(world, x, y, ((RectangleMapObject) obj).getRectangle());
            } else if (obj instanceof PolylineMapObject) {
                extractPolyline(world, x, y, ((PolylineMapObject) obj).getPolyline());
            } else if (obj instanceof PolygonMapObject) {
                var polygon = ((PolygonMapObject) obj).getPolygon();

                if (polygon.getVertices().length > 8 && !options.isTriangulateInsteadOfThrow())
                    throw new IllegalArgumentException("Polygon vertices > 8");

                extractPolygon(world, x, y, polygon);
            }
        }
    }

    private void extractPolygon(World world, int x, int y, Polygon polygon) {
        var vertices = PolygonUtils.transformVertices(polygon.getTransformedVertices(), options.getScale());

        if (options.isCombineTileCollisions()) {

        }
    }

    private void extractPolyline(World world, int x, int y, Polyline polyline) {
    }

    private void extractRectangle(World world, int x, int y, Rectangle rectangle) {
    }

    private void extractCircle(World world, int x, int y, Ellipse ellipse) {

    }
}
