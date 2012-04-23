TMXIsometricExample
===================
# Requirements!
 * Andengine
 * TMX extension (Isometric branch created by me and in my github repo 

### How to build
 * Just like the Andengine Examples (hopefully)
 * So clone this repo
 * Right click in Eclipse on folder > Android Tools > Fix Project Properties
 
### How to use it.
The default map is the isometric map which comes with Tiled (0.8.0)

You can change the map and do a few things by pressing your MENU key.

There are some sample isometric maps I've created with offsets and 1 Orthographic
map.

You can see the tile row and column touched in the HUD, along with the FPS and
X Y touch locations.

You can draw a cross on the tile if you enable "Tile touch hits" (access via 
menu), you can change the colour, what layer to perform the touch on(doesn't 
have any effect really, since all layers are the same).  You can draw the touch
hit in the Tile Centre or where the touch occured (HUD still display the Row and
Column. You can remove the tile hits by going Menu>Remove lines drawn.  You 
can also disable the touch hits

### BUGS
Read through the Isometric branch code (mostly TMXLayer.java) and the code in 
this example