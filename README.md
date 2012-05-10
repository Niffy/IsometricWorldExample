TMXIsometricExample
===================
# Requirements!
 * Andengine
 * TMX extension (Isometric branch created by me and in my github repo) [https://github.com/Niffy/AndEngineTMXTiledMapExtension/tree/isometric](https://github.com/Niffy/AndEngineTMXTiledMapExtension/tree/isometric "AndEngineTMXTiledMapExtension Isometric Branch")

### How to build
 * Just like the Andengine Examples (hopefully)
 * So clone this repo
 * Right click in Eclipse on folder > Android Tools > Fix Project Properties
 
### How to use it/features
The default map is 5x5Object, which has object data.

You can change the map and do a few things by pressing your MENU key.

There are some sample isometric maps I've created with offsets and 2 Orthographic maps.

You can see the tile row and column touched in the HUD, along with the FPS and X Y touch locations.

You can draw a cross on the tile if you enable "Tile touch hits" (access via 
menu), you can change the colour. You can draw the touch
hit in the Tile Centre or where the touch occured (HUD still display the Row and Column. You can remove the tile hits by going Menu>Remove lines drawn.  You can also disable the touch hits. 

Currently 2 maps have object data, 5x5Object and Large_isometricBlocks_wObject (Run this large map and see if you can get above 1FPS!).  To draw the tile objects press MENU > Select Objects layers to draw.  You can draw all(even if they have no tile objects, as it can detect this) or select a layer yourself to draw (If you draw layer 5 then 4, layer 4 will be on top, currently no sorting takes place in this example).

When a Tile object layer is added to the scene, it stores which tiles are blocked (the tile the object occupys), then then stops you from drawing on that tile along with playing a sound or vibrating (if you enable via the touch hit options menu)

Polygon and Polylines are parsed and stored in the original Tiled pixel space format, they can be accessed via the TMXObject. These can be converted using a ConvertIsometricPixelToScene object in the Isometric branch. See the maphandling class on how to draw these these objects. (Polygon and Polyline are drawn and processed the same way)  This should make it possible for you to calculate collisions using physics or your own implementation of checking against the TMXObjects.

###Update
- Polygon and Polylines can now be drawn
- When remove lines is clicked, Tile objects and poly objects are removed from the scene and clears any blocked tile.

###Future plans
- Draw tile objects on orthographic maps
- Implement tileset offsets for orthographic maps
- Get the map to return TMXLayer and TMXLayerObjectTiles sorted so they are in the correct order to be drawn.


### BUGS
- If an isometric map origin changes, the touch functions do not reflect this change, same for things like drawing polyobjects.