package dev.lyze.projectTrianglePlatforming;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.World;
import dev.lyze.projectTrianglePlatforming.utils.MapUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.var;

import java.util.ArrayList;

@AllArgsConstructor
public class TiledTileLayerToBox2d {

    private final TiledTileLayerToBox2dOptions options;

    public void parseAllLayers(TiledMap map, World world) {
        for (var layer : map.getLayers()) {
            if (layer instanceof TiledMapTileLayer)
                parseLayer(((TiledMapTileLayer) layer), world);
        }
    }

    public void parseLayer(TiledMapTileLayer layer, World world) {
        var bodyType = layer.getProperties().get("ptpBodyType", "StaticBody", String.class);

        var bees = new ArrayList<Bee>();
        for (var y = layer.getHeight() - 1; y >= 0; y--) {
            Bee bee = null;
            Bee previousBee = null;
            for (var x = 0; x < layer.getWidth(); x++) {
                var cell = layer.getCell(x, y);
                var isTile = cell != null;

                if (isTile && cell.getTile().getProperties().get("ptpIgnore", false, Boolean.class))
                    continue;

                var finalX = x;
                var foundBee = bees.stream().filter(b -> !b.closed && b.startPointX == finalX).findAny();
                if (foundBee.isPresent()) {
                    if (bee == null)
                        previousBee = foundBee.get();
                    else
                        foundBee.get().closed = true;
                }

                if (previousBee != null && !isTile) {
                    if (previousBee.endPointX == x - 1) {
                        previousBee.height++;
                    } else {
                        previousBee.closed = true;
                        if (x > 0 && layer.getCell(x - 1, y) != null) {
                            bees.add(new Bee(previousBee.startPointX, y, x - 1));
                        }
                    }
                    previousBee = null;
                }

                if (previousBee != null && isTile && previousBee.endPointX == x - 1) {
                    previousBee.closed = true;
                    bee = new Bee(previousBee.startPointX, y);
                    previousBee = null;
                }

                if (bee == null && isTile && previousBee == null) {
                    bee = new Bee(x, y);
                }

                if (bee != null && !isTile) {
                    bee.setEndPointX(x - 1);
                    bees.add(bee);
                    bee = null;
                }
            }

            if (bee != null) {
                bee.setEndPointX(layer.getWidth() - 1);
                Bee finalBee = bee;
                var previousUnclosedBee = bees.stream().filter(b -> !b.closed && b.startPointX == finalBee.startPointX && b.endPointX == finalBee.endPointX).findAny();
                if (previousUnclosedBee.isPresent()) {
                    previousUnclosedBee.get().height++;
                } else {
                    bees.add(bee);
                }
            }

            if (previousBee != null) {
                if (previousBee.endPointX == layer.getWidth() - 1) {
                    previousBee.height++;
                } else {
                    previousBee.closed = true;
                    bees.add(new Bee(previousBee.startPointX, y, layer.getWidth() - 1));
                }
            }
        }

        // last bee can be false if it ends at x = layer.getWidth() - 1
        drawBees(layer, world, bees, new BodyFixtureOptions(layer.getProperties()));
    }

    private void drawBees(TiledMapTileLayer layer, World world, ArrayList<Bee> bees, BodyFixtureOptions options) {
        for (var bee : bees) {
            MapUtils.extractRectangle(world,
                    bee.startPointX * layer.getTileWidth() * this.options.getScale(),
                    (bee.startPointY - bee.height + 1) * layer.getTileHeight() * this.options.getScale(),
                    (bee.endPointX - bee.startPointX + 1) * layer.getTileWidth() * this.options.getScale(),
                    bee.getHeight() * layer.getTileHeight() * this.options.getScale(), options);
        }
    }

    @Data
    private static class Bee {
        private int startPointX, startPointY;
        private int endPointX;
        private int height = 1;

        private boolean closed;

        public Bee(int startPointX, int startPointY) {
            this.startPointX = startPointX;
            this.startPointY = startPointY;
        }

        public Bee(int startPointX, int startPointY, int endPointX) {
            this.startPointX = startPointX;
            this.startPointY = startPointY;

            this.endPointX = endPointX;
        }
    }
}