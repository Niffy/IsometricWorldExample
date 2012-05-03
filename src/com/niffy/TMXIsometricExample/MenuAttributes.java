/**
 * 
 */
package com.niffy.TMXIsometricExample;

import org.andengine.extension.tmx.util.constants.TMXIsometricConstants;

/**
 * @author Paul Robinson
 *
 */
public class MenuAttributes implements MenuConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	//Drawing method selected, default DRAW_METHOD_ISOMETRIC_CULLING_TILED_SOURCE
	public int SELECTED_DRAW_METHOD = TMXIsometricConstants.DRAW_METHOD_ISOMETRIC_CULLING_TILED_SOURCE;
	public String SELECTION_OBJECT_LAYER = "default";
	public int SELECTION_LAYER = 0;
	
	public int SELECTION_TILE_HIT = TILE_HIT_CENTRE; //Tile centre
	public int SELECTION_COLOUR = COLOUR_YELLOW;
	
	//Selected map to load, default is 0 (in TMXFilesIsometric)
	public int SELECTED_TMX_MAP = 9; 
	public boolean SELECTED_TMX_MAP_ISO_ORTHO = true; //ISO = true

	public boolean TILE_HIT_SOUND_ENABLED = false;
	public boolean TILE_HIT_VIBRATE_ENABLED = false;
	//Are we monitoring tile hits?
	public boolean ENABLE_TILE_HIT = false;
	

	// ===========================================================
	// Constructors
	// ===========================================================

	public MenuAttributes(){
		
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
