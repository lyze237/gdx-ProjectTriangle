package dev.lyze.projectTrianglePlatforming;

import clipper2.Clipper;
import clipper2.core.FillRule;
import clipper2.core.PathD;
import clipper2.core.PathsD;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.World;
import dev.lyze.projectTrianglePlatforming.utils.ArrayUtils;
import dev.lyze.projectTrianglePlatforming.utils.MapUtils;
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

    public void parseLayer(TiledMapTileLayer layer, World world) {
        var subjects = new PathsD();

        for (int x = 0; x < layer.getWidth(); x++)
            for (int y = 0; y < layer.getHeight(); y++)
                parseCell(layer, x, y, subjects, world);

        var result = Clipper.Union(subjects, FillRule.NonZero);
        for (PathD path : result) {
            var vertices = new float[path.size() * 2];

            for (int i = 0; i < path.size(); i++) {
                var point = path.get(i);

                vertices[i * 2] = (float) point.x;
                vertices[i * 2 + 1] = (float) point.y;
            }

            MapUtils.extractPolygon(world, vertices, options.getTriangulator());
        }
    }

    private void parseCell(TiledMapTileLayer layer, int x, int y, PathsD subjects, World world) {
        var cell = layer.getCell(x, y);
        if (cell == null)
            return;

        for (var obj : cell.getTile().getObjects()) {
            if (obj instanceof EllipseMapObject) {
                extractCircle(world, x * layer.getTileWidth(), y * layer.getTileHeight(), subjects, ((EllipseMapObject) obj).getEllipse());
            } else if (obj instanceof RectangleMapObject) {
                extractRectangle(world, x * layer.getTileWidth(), y * layer.getTileHeight(), subjects, ((RectangleMapObject) obj).getRectangle());
            } else if (obj instanceof PolylineMapObject) {
                extractPolyline(world, x * layer.getTileWidth(), y * layer.getTileHeight(), subjects, ((PolylineMapObject) obj).getPolyline());
            } else if (obj instanceof PolygonMapObject) {
                extractPolygon(world, x * layer.getTileWidth(), y * layer.getTileHeight(), subjects, ((PolygonMapObject) obj).getPolygon());
            }
        }
    }

    private void extractPolygon(World world, int x, int y, PathsD subjects, Polygon polygon) {
        var vertices = PolygonUtils.transformVertices(polygon.getTransformedVertices(), options.getScale(), x * options.getScale(), y * options.getScale());

        if (options.isCombineTileCollisions()) {
            var doubleVertices = ArrayUtils.convertToDoubleArray(vertices);
            subjects.add(Clipper.MakePath(doubleVertices));
        } else {
            if (polygon.getVertices().length > 8 && !options.isTriangulateInsteadOfThrow())
                throw new IllegalArgumentException("Polygon vertices > 8");
            
            MapUtils.extractPolygon(world, vertices, options.getTriangulator());
        }
    }

    private void extractPolyline(World world, int x, int y, PathsD subjects, Polyline polyline) {
        var vertices = PolygonUtils.transformVertices(polyline.getTransformedVertices(), options.getScale(), x * options.getScale(), y * options.getScale());

        MapUtils.extractPolyline(world, vertices);
    }

    private void extractRectangle(World world, int x, int y, PathsD subjects, Rectangle rectangle) {
        if (options.isCombineTileCollisions()) {
            var vertices = new double[]{
                    x * options.getScale() + rectangle.x * options.getScale(), y * options.getScale() + rectangle.y * options.getScale(),
                    x * options.getScale() + rectangle.x * options.getScale() + rectangle.width * options.getScale(), y * options.getScale() + rectangle.y * options.getScale(),
                    x * options.getScale() + rectangle.x * options.getScale() + rectangle.width * options.getScale(), y * options.getScale() + rectangle.y * options.getScale() + rectangle.height * options.getScale(),
                    x * options.getScale() + rectangle.x * options.getScale(), y * options.getScale() + rectangle.y * options.getScale() + rectangle.height * options.getScale(),
            };

            subjects.add(Clipper.MakePath(vertices));
        } else {
            MapUtils.extractRectangle(world, x * options.getScale() + rectangle.x * options.getScale(), y * options.getScale() + rectangle.y * options.getScale(), rectangle.width * options.getScale(), rectangle.height * options.getScale());
        }
    }

    private void extractCircle(World world, int x, int y, PathsD subjects, Ellipse ellipse) {
        if (ellipse.width != ellipse.height) {
            if (options.isThrowOnInvalidObject())
                throw new IllegalArgumentException("Ellipse objects not supported by Box2D");

            return;
        }

        MapUtils.extractCircle(world, x * options.getScale() + ellipse.x * options.getScale() + ellipse.width / 2f * options.getScale(), y * options.getScale() + ellipse.y * options.getScale() + ellipse.height / 2f * options.getScale(), ellipse.width / 2f * options.getScale());
    }
}
