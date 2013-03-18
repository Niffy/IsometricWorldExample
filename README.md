Isometric World Example
===================
# Requirements!
 * Andengine (my Niffy branch) [https://github.com/Niffy/AndEngine/tree/niffy](https://github.com/Niffy/AndEngine/tree/niffy "AndEngine Isometric Branch")
 * TMX extension (Isometric branch created by me and in my github repo) [https://github.com/Niffy/AndEngineTMXTiledMapExtension/tree/isometric](https://github.com/Niffy/AndEngineTMXTiledMapExtension/tree/isometric "AndEngineTMXTiledMapExtension Isometric Branch")

### How to build
 * So clone this repo
 * Right click in Eclipse on folder > Android Tools > Fix Project Properties
 * Fix any imports (such as point to your andengine location)
 
### Wiki

Visit the wiki to get an understand of how to use isometric features. 
 
### What the example does
 * 1 Isometric map
 * One isometric character
 * A few isometric cubes
 * Simple path manager
 * Simple tile manager
 * Touch Manager
 * Using fragments
 
### How to use
Press the bulid object menu to get a dialog fragment up, select an item on the list and click done. You can come out of the dialog by pressing cancel. **Note** Andengine is still running in the background, so the dialog is not modal. You can see in logcat the FPS logger.

Place object on the map, when placed press done on the bottom of the screen. You can press cancel to stop the process. When done is clicked the location is then blocked in the tile manager.

Repeat till you get some objects on the screen.

To place a human press place human then click a tile on the map.

To move the human, click the human then click the destination tile.
**Note** Placing objects when a human is moving will not block the human, there is no scope for this in this example. Also the path management cannot cope if you try to access a location blocked off, again no scope in the path management example to solve this.


### BUGS
There can be some draw problems related to the depth sorting, when placing objects close together they can incorrectly draw.  I think this is most likely down to the algorithim, its not perfect, if you wanted perfect isometric you'd have to go the full 3D route.  But if you don't place items too close then this shouldn't be a problem.

![IsometricWorldExample01](https://github.com/Niffy/IsometricWorldExample/wiki/images/bugs/IsometricWorldExample01.png)
![IsometricWorldExample02](https://github.com/Niffy/IsometricWorldExample/wiki/images/bugs/IsometricWorldExample02.png)
![IsometricWorldExample03](https://github.com/Niffy/IsometricWorldExample/wiki/images/bugs/IsometricWorldExample03.png)

Placing some other items as well can sometimes fix this.  You may also notice in this situation they will flicker if a human is moving.