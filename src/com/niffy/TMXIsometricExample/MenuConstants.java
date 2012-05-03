
package com.niffy.TMXIsometricExample;

/**
 * 
 * List of constants for building menus
 * @author Paul Robinson
 *
 */
public interface MenuConstants {

	public static final int SELECT_TILE_HIT = 0;
	public static final int SELECT_TMX_MAP = 1;
	public static final int DRAW_OBJECTS = 2;
	public static final int DRAW_METHOD = 5;
	public static final int REMOVE_LINES = 6;
	public static final int RESET_CAMERA = 7;
	public static final int BACK_TO_GAME = 8;
	
	//TMX Map selection
	public static final int SELECT_TMX_MAP_START_RANGE = 20;
	public static final int SELECT_TMX_MAP_ISO = 21;
	public static final int SELECT_TMX_MAP_ISO_BACK = 22;
	public static final int SELECT_TMX_MAP_ORTHO = 23;
	public static final int SELECT_TMX_MAP_ORTHO_BACK = 24;
	public static final int SELECT_TMX_MAP_BACK = 25;
	public static final int SELECT_TMX_MAP_END_RANGE = 29;
	
	//Objects
	public static final int DRAW_OBJECTS_START_RANGE = 40;
	public static final int DRAW_OBJECTS_ALL = 40;
	public static final int DRAW_OBJECTS_LAYER = 41;
	public static final int DRAW_OBJECTS_LAYER_BACK = 48;
	public static final int DRAW_OBJECTS_BACK = 49;
	public static final int DRAW_OBJECTS_END_RANGE = 49;
	
	//What tile hits options there is
	public static final int TILE_START_RANGE = 50;
	public static final int TILE_ENABLE_DISABLE = 51;  
	public static final int TILE_ENABLE_OBJECT = 52;
	public static final int TILE_SELECT_COLOUR = 53;
	public static final int TILE_HIT_CENTRE = 54;
	public static final int TILE_HIT_POINT = 55;
	public static final int TILE_HIT_BACK = 56;
	public static final int TILE_HIT_SOUND = 57;
	public static final int TILE_HIT_VIBRATE = 58;
	public static final int TILE_END_RANGE = 59;
	
	//colours to use for hit points
	public static final int COLOUR_START_RANGE = 60;
	public static final int COLOUR_BLUE = 60;
	public static final int COLOUR_GREEN = 61;
	public static final int COLOUR_YELLOW = 62;
	public static final int COLOUR_RED = 63;
	public static final int COLOUR_BACK = 64;
	public static final int COLOUR_END_RANGE = 64;
	
	//When a map has been selected this helps manage menu and selection.
	public static final int MAP_SELECTED_ISO = 200;
	public static final int MAP_SELECTED_ORTHO = 201;
	//A layer has been selected, so on the touch options get the layer.
	public static final int LAYER_SELECTION_ENABLED = 300;
	
	public static final int OBJECT_LAYER_SELECTED = 400;
	
	//When a drawing method has been selected, this helps manage menu and selection
	public static final int DRAW_METHOD_SELECTED = 500;
	public static final int DRAW_METHOD_BACK = 501;
	
}

