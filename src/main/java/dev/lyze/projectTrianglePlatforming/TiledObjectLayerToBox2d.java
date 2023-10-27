package dev.lyze.projectTrianglePlatforming;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import dev.lyze.projectTrianglePlatforming.utils.MapUtils;
import dev.lyze.projectTrianglePlatforming.utils.PolygonUtils;
import lombok.AllArgsConstructor;
import lombok.var;

@AllArgsConstructor
public class TiledObjectLayerToBox2d {

    private final TiledObjectLayerToBox2dOptions options;

    public void parseAllLayers(TiledMap map, World world) {
        for (MapLayer layer : map.getLayers()) {
            parseLayer(layer, world);
        }
    }

    public void parseLayer(MapLayer layer, World world) {
        for (var obj : layer.getObjects()) {
            if (obj.getProperties().get("ptpIgnore", false, Boolean.class))
                continue;

            var bodyType = obj.getProperties().get("ptpBodyType", "StaticBody", String.class);

            if (obj instanceof EllipseMapObject) {
                extractCircle(world, ((EllipseMapObject) obj).getEllipse(), bodyType);
            } else if (obj instanceof RectangleMapObject) {
                extractRectangle(world, ((RectangleMapObject) obj).getRectangle(), bodyType);
            } else if (obj instanceof PolylineMapObject) {
                extractPolyline(world, ((PolylineMapObject) obj).getPolyline(), bodyType);
            } else if (obj instanceof PolygonMapObject) {
                extractPolygon(world, ((PolygonMapObject) obj).getPolygon(), bodyType);
            }
        }
    }

    private void extractPolygon(World world, Polygon polygon, String bodyType) {
        if (polygon.getVertices().length > 8 && !options.isTriangulateInsteadOfThrow())
            throw new IllegalArgumentException("Polygon vertices > 8");

        var vertices = PolygonUtils.transformVertices(polygon.getTransformedVertices(), options.getScale());

        MapUtils.extractPolygon(world, vertices, options.getTriangulator(), bodyType);
    }

    private void extractPolyline(World world, Polyline polyline, String dynamic) {
        var vertices = PolygonUtils.transformVertices(polyline.getTransformedVertices(), options.getScale());

        MapUtils.extractPolyline(world, vertices, dynamic);
    }

    private void extractRectangle(World world, Rectangle rectangle, String bodyType) {
        MapUtils.extractRectangle(world, rectangle.x * options.getScale(), rectangle.y * options.getScale(), rectangle.width * options.getScale(), rectangle.height * options.getScale(), bodyType);
    }

    private void extractCircle(World world, Ellipse ellipse, String bodyType) {
        if (ellipse.width != ellipse.height) {
            if (options.isThrowOnInvalidObject())
                throw new IllegalArgumentException("Ellipse objects not supported by Box2D");

            return;
        }

        MapUtils.extractCircle(world, ellipse.x * options.getScale() + ellipse.width / 2f * options.getScale(), ellipse.y * options.getScale() + ellipse.height / 2f * options.getScale(), ellipse.width / 2f * options.getScale(), bodyType);
    }
}