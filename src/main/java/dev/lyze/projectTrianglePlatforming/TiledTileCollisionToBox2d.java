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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import dev.lyze.clipper2.Clipper;
import dev.lyze.clipper2.core.FillRule;
import dev.lyze.clipper2.core.PathD;
import dev.lyze.clipper2.core.PathsD;
import dev.lyze.projectTrianglePlatforming.utils.ArrayUtils;
import dev.lyze.projectTrianglePlatforming.utils.MapUtils;
import dev.lyze.projectTrianglePlatforming.utils.PolygonUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.var;

@AllArgsConstructor
public class TiledTileCollisionToBox2d {
    private final TiledTileCollisionToBox2dOptions options;

    @Getter private final Array<float[]> computedPolygons = new Array<>();


    public void parseAllLayers(TiledMap map, World world) {
        for (MapLayer layer : map.getLayers())
            if (layer instanceof TiledMapTileLayer)
                parseLayer(((TiledMapTileLayer) layer), world);
    }

    public void parseLayer(TiledMapTileLayer layer, World world) {
        var subjects = new ObjectMap<String, PathsD>();

        var bodyType = layer.getProperties().get("ptpBodyType", "StaticBody", String.class);

        for (int x = 0; x < layer.getWidth(); x++)
            for (int y = 0; y < layer.getHeight(); y++)
                parseCell(layer, x, y, subjects, world);

        for (ObjectMap.Entry<String, PathsD> subject : subjects) {
            var result = Clipper.Union(subject.value, FillRule.NonZero);
            for (var path : result) {
                var vertices = new float[path.size() * 2];

                for (int i = 0; i < path.size(); i++) {
                    var point = path.get(i);

                    vertices[i * 2] = (float) point.x;
                    vertices[i * 2 + 1] = (float) point.y;
                }

                computedPolygons.add(vertices);
                MapUtils.extractPolygon(world, vertices, options.getTriangulator(), subject.key.substring(subject.key.lastIndexOf("_") + 1));
            }
        }
    }

    private void parseCell(TiledMapTileLayer layer, int x, int y, ObjectMap<String, PathsD> subjects, World world) {
        var cell = layer.getCell(x, y);
        if (cell == null)
            return;

        if (cell.getTile().getProperties().get("ptpIgnore", false, Boolean.class))
            return;

        for (var obj : cell.getTile().getObjects()) {
            if (obj.getProperties().get("ptpIgnore", false, Boolean.class))
                continue;

            var bodyType = cell.getTile().getProperties().get("ptpMergeType", "default", String.class);

            if (obj instanceof EllipseMapObject) {
                extractCircle(world, x * layer.getTileWidth(), y * layer.getTileHeight(), subjects, ((EllipseMapObject) obj).getEllipse(), getMergeType(cell), bodyType);
            } else if (obj instanceof RectangleMapObject) {
                extractRectangle(world, x * layer.getTileWidth(), y * layer.getTileHeight(), subjects, ((RectangleMapObject) obj).getRectangle(), getMergeType(cell), bodyType);
            } else if (obj instanceof PolylineMapObject) {
                extractPolyline(world, x * layer.getTileWidth(), y * layer.getTileHeight(), subjects, ((PolylineMapObject) obj).getPolyline(), getMergeType(cell), bodyType);
            } else if (obj instanceof PolygonMapObject) {
                extractPolygon(world, x * layer.getTileWidth(), y * layer.getTileHeight(), subjects, ((PolygonMapObject) obj).getPolygon(), getMergeType(cell), bodyType);
            }
        }
    }

    private void extractPolygon(World world, int x, int y, ObjectMap<String, PathsD> subjects, Polygon polygon, String mergeType, String bodyType) {
        var vertices = PolygonUtils.transformVertices(polygon.getTransformedVertices(), options.getScale(), x * options.getScale(), y * options.getScale());

        if (options.isCombineTileCollisions()) {
            var doubleVertices = ArrayUtils.convertToDoubleArray(vertices);
            createOrAdd(subjects, mergeType, Clipper.MakePath(doubleVertices));
        } else {
            if (polygon.getVertices().length > 8 && !options.isTriangulateInsteadOfThrow())
                throw new IllegalArgumentException("Polygon vertices > 8");
            
            MapUtils.extractPolygon(world, vertices, options.getTriangulator(), bodyType);
        }
    }

    private void extractPolyline(World world, int x, int y, ObjectMap<String, PathsD> subjects, Polyline polyline, String mergeType, String bodyType) {
        var vertices = PolygonUtils.transformVertices(polyline.getTransformedVertices(), options.getScale(), x * options.getScale(), y * options.getScale());

        MapUtils.extractPolyline(world, vertices, bodyType);
    }

    private void extractRectangle(World world, int x, int y, ObjectMap<String, PathsD> subjects, Rectangle rectangle, String mergeType, String bodyType) {
        if (options.isCombineTileCollisions()) {
            var vertices = new double[]{
                    x * options.getScale() + rectangle.x * options.getScale(), y * options.getScale() + rectangle.y * options.getScale(),
                    x * options.getScale() + rectangle.x * options.getScale() + rectangle.width * options.getScale(), y * options.getScale() + rectangle.y * options.getScale(),
                    x * options.getScale() + rectangle.x * options.getScale() + rectangle.width * options.getScale(), y * options.getScale() + rectangle.y * options.getScale() + rectangle.height * options.getScale(),
                    x * options.getScale() + rectangle.x * options.getScale(), y * options.getScale() + rectangle.y * options.getScale() + rectangle.height * options.getScale(),
            };

            createOrAdd(subjects, mergeType, Clipper.MakePath(vertices));
        } else {
            MapUtils.extractRectangle(world, x * options.getScale() + rectangle.x * options.getScale(), y * options.getScale() + rectangle.y * options.getScale(), rectangle.width * options.getScale(), rectangle.height * options.getScale(), bodyType);
        }
    }

    private void extractCircle(World world, int x, int y, ObjectMap<String, PathsD> subjects, Ellipse ellipse, String mergeType, String bodyType) {
        if (ellipse.width != ellipse.height) {
            if (options.isThrowOnInvalidObject())
                throw new IllegalArgumentException("Ellipse objects not supported by Box2D");

            return;
        }

        MapUtils.extractCircle(world, x * options.getScale() + ellipse.x * options.getScale() + ellipse.width / 2f * options.getScale(), y * options.getScale() + ellipse.y * options.getScale() + ellipse.height / 2f * options.getScale(), ellipse.width / 2f * options.getScale(), bodyType);
    }

    private void createOrAdd(ObjectMap<String, PathsD> map, String type, PathD path) {
        if (!map.containsKey(type))
            map.put(type, new PathsD());

        map.get(type).add(path);
    }

    private String getMergeType(TiledMapTileLayer.Cell cell) {
        var mergeType = cell.getTile().getProperties().get("ptpMergeType", "default", String.class);
        var bodyType = cell.getTile().getProperties().get("ptpBodyType", "StaticBody", String.class);

        return mergeType + "_" + bodyType;
    }
}
