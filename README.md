# gdx-ProjectTrianglePlatforming

A Tiled Layer Objects / Tile Collisions to Box2D Parser for libGDX

---

[![License](https://img.shields.io/github/license/lyze237/gdx-projecttriangleplatforming)](https://github.com/lyze237/gdx-projecttriangleplatforming/blob/master/LICENSE)
[![Jitpack](https://jitpack.io/v/lyze237/gdx-projecttriangleplatforming.svg)](https://jitpack.io/#lyze237/gdx-projecttriangleplatforming)
[![Donate](https://img.shields.io/badge/Donate-%3C3-red)](https://coffee.lyze.dev)

# What's this?

The library is a simple tool to generate box2d bodies based on various tiled objects and properties.

This includes:
1. Objects in a Layer

This takes all objects on a layer and converts them appropriately into Box2D shapes.

The supported map objects are:
* Rectangle
* Circle (=> Ellipse with width == height)
* Polygon
* Polyline (=> Unclosed polygon)
 
| Tiled                                         | Box2D                                         |
|-----------------------------------------------|-----------------------------------------------| 
| ![Objects in Tiled](images/tiled_objects.png) | ![Objects in Box2D](images/box2d_objects.png) |

2.Tile collisions

This converts tileset collisions into Box2D shapes and combines them when possible into bigger polygons.

![Example screenshot of the tile collision menu](images/tiled_OPBwVk9rJU.png)

The supported map objects are:
* Rectangle
* Circle (=> Ellipse with width == height)
* Polygon
* Polyline (=> Unclosed polygon)

| Tiled                                                      | Box2D                                                      |
|------------------------------------------------------------|------------------------------------------------------------| 
| ![Tile collision in Tiled](images/tiled_tileCollision.png) | ![Tile collision in Box2D](images/box2d_tileCollision.png) |

3.Tiles in a layer

This just converts every tile in the layer into rectangles.

The algorithm tries to create rectangles as wide as possible, and then if possible also extends them down.

This reduces ghosting and is in my opinion the better way of doing it.

| Tiled                                                       | Box2D                                                       |
|-------------------------------------------------------------|-------------------------------------------------------------| 
| ![Tiles in layer collision in Tiled](images/tiled_tile.png) | ![Tiles in layer collision in Box2D](images/box2d_tile.png) |

---

Additionally, there are a couple important features implemented:
* Scaling
* Triangulation of polygons when needed
* Merging of tiles and tile collisions to reduce ghosting issues



