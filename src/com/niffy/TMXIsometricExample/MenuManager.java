/**
 * 
 */
package com.niffy.TMXIsometricExample;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;


/**
 * @author Paul Robinson
 *
 */
public class MenuManager implements MenuConstants {
	// ===========================================================
	// Constants
	// ===========================================================
	private TMXIsometricExampleActivity parent = null;
	private MenuDrawer drawer = null;
	private MenuAttributes attributes = null;
	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public MenuManager(final TMXIsometricExampleActivity pParent, final MenuDrawer pDraw, final MenuAttributes pAttributes){
		this.parent = pParent;
		this.drawer = pDraw;
		this.attributes = pAttributes;
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

	public boolean handle(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY){
		final int CLICK = pMenuItem.getID();
		if(CLICK == SELECT_TILE_HIT){
			this.parent.mSceneMasterMenu.setChildSceneModal(this.parent.mSceneTileHitMenu);
			return true;
		}if (CLICK == SELECT_TMX_MAP){
			this.parent.mSceneMasterMenu.setChildSceneModal(this.parent.mSceneSelectMap);
			return true;
		}else if(CLICK == DRAW_OBJECTS){
			this.parent.mSceneMasterMenu.setChildSceneModal(this.parent.mSceneObjectMenu);
			return true;
		}else if(CLICK == DRAW_METHOD){
			this.parent.mSceneMasterMenu.setChildSceneModal(this.parent.mSceneDrawingMethod);
			return true;
		}else if(CLICK == REMOVE_LINES){
			this.parent.removeLines();
			return true;
		}else if(CLICK == RESET_CAMERA){
			this.parent.resetCamera();
			return true;
		}else if(CLICK == BACK_TO_GAME){
			this.parent.mSceneMasterMenu.back();
			return true;
		}else if(CLICK >= TILE_START_RANGE && CLICK <= TILE_END_RANGE) {
			return this.handle_tile_menu(pMenuScene, pMenuItem, pMenuItemLocalX, pMenuItemLocalY);
		}else if(CLICK >= SELECT_TMX_MAP_START_RANGE && CLICK <= SELECT_TMX_MAP_END_RANGE){
			return this.handle_map_selection(pMenuScene, pMenuItem, pMenuItemLocalX, pMenuItemLocalY);
		}else if(CLICK >= DRAW_OBJECTS_START_RANGE && CLICK <= DRAW_OBJECTS_END_RANGE){
			return this.handle_draw_object_selection(pMenuScene, pMenuItem, pMenuItemLocalX, pMenuItemLocalY);
		}else if(CLICK >= COLOUR_START_RANGE && CLICK <= COLOUR_END_RANGE){
			return this.handle_colour_selection(pMenuScene, pMenuItem, pMenuItemLocalX, pMenuItemLocalY);
		}else if(CLICK == MAP_SELECTED_ISO){
			this.attributes.SELECTED_TMX_MAP_ISO_ORTHO = true;
			this.attributes.SELECTED_TMX_MAP = (Integer) pMenuItem.getUserData();
			this.parent.detatchMap();
			this.parent.loadMap(this.attributes.SELECTED_TMX_MAP);
			this.parent.mSceneTMXIsoMenu.back();
			return true;
		}else if(CLICK == MAP_SELECTED_ORTHO){
			this.attributes.SELECTED_TMX_MAP_ISO_ORTHO = false;
			this.attributes.SELECTED_TMX_MAP = (Integer) pMenuItem.getUserData();
			this.parent.detatchMap();
			this.parent.loadMap(this.attributes.SELECTED_TMX_MAP);
			this.parent.mSceneTMXORthoMenu.back();
			return true;
		}else if(CLICK == OBJECT_LAYER_SELECTED){
			this.attributes.SELECTION_OBJECT_LAYER = (String) pMenuItem.getUserData();
			this.parent.attachObjectLayer(this.attributes.SELECTION_OBJECT_LAYER);
			this.parent.mSceneObjectLayerMenu.back();
			return true;
		}else if(CLICK == DRAW_METHOD_SELECTED){
			this.attributes.SELECTED_DRAW_METHOD =  (Integer) pMenuItem.getUserData();
			this.parent.setDrawingMethod(this.attributes.SELECTED_DRAW_METHOD);
			this.parent.mSceneDrawingMethod.back();
			return true;
		}else if(CLICK == DRAW_METHOD_BACK){
			this.parent.mSceneDrawingMethod.back();
			return true;
		}else{
			return false;
		}
	}

	private boolean handle_tile_menu(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY){
		final int CLICK = pMenuItem.getID();
		if(CLICK == TILE_ENABLE_DISABLE){
			if(!this.attributes.ENABLE_TILE_HIT){
				this.attributes.ENABLE_TILE_HIT = true;
			}else{
				this.attributes.ENABLE_TILE_HIT = false;
			}
			this.parent.mSceneTileHitMenu.back();
			this.parent.mSceneTileHitMenu = this.drawer.create_tile_hit_menu();
			return true;
		}else if(CLICK == TILE_ENABLE_OBJECT){
			
			return true;
		}else if(CLICK == TILE_SELECT_COLOUR){
			pMenuScene.setChildSceneModal(this.parent.mSceneColourMenu);
			return true;
		}else if(CLICK == TILE_HIT_CENTRE){
			this.attributes.SELECTION_TILE_HIT = TILE_HIT_CENTRE;
			return true;
		}else if(CLICK == TILE_HIT_POINT){
			this.attributes.SELECTION_TILE_HIT = TILE_HIT_POINT;
			return true;
		}else if(CLICK == TILE_HIT_BACK){
			this.parent.mSceneTileHitMenu.back();
			return true;
		}else if(CLICK ==TILE_HIT_SOUND){
			if(!this.attributes.TILE_HIT_SOUND_ENABLED){
				this.attributes.TILE_HIT_SOUND_ENABLED = true;
			}else{
				this.attributes.TILE_HIT_SOUND_ENABLED = false;
			}			
			this.parent.mSceneTileHitMenu.back();
			this.parent.mSceneTileHitMenu = this.drawer.create_tile_hit_menu();
			return true;
		}else if(CLICK == TILE_HIT_VIBRATE){
			if(!this.attributes.TILE_HIT_VIBRATE_ENABLED){
				this.attributes.TILE_HIT_VIBRATE_ENABLED = true;
			}else{
				this.attributes.TILE_HIT_VIBRATE_ENABLED = false;
			}
			this.parent.mSceneTileHitMenu.back();
			this.parent.mSceneTileHitMenu = this.drawer.create_tile_hit_menu();
			return true;
		}else{
			return false;
		}
	}
	
	private boolean handle_map_selection(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY){
		final int CLICK = pMenuItem.getID();
		if(CLICK == SELECT_TMX_MAP_ISO){
			this.parent.mSceneSelectMap.setChildSceneModal(this.parent.mSceneTMXIsoMenu);
			return true;
		}else if(CLICK == SELECT_TMX_MAP_ORTHO){
			this.parent.mSceneSelectMap.setChildSceneModal(this.parent.mSceneTMXORthoMenu);
			return true;
		}else if(CLICK == SELECT_TMX_MAP_ORTHO_BACK){
			this.parent.mSceneTMXORthoMenu.back();
			return true;
		}else if(CLICK == SELECT_TMX_MAP_ISO_BACK){
			this.parent.mSceneTMXIsoMenu.back();
			return true;
		}else if(CLICK == SELECT_TMX_MAP_BACK){
			this.parent.mSceneSelectMap.back();
			return true;
		}else{
			return false;
		}
	}

	private boolean handle_draw_object_selection(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY){
		final int CLICK = pMenuItem.getID();
		if(CLICK == DRAW_OBJECTS_ALL){
			this.parent.attachAllObjectLayers();
			this.parent.mSceneObjectMenu.back();
			return true;
		}else if(CLICK == DRAW_OBJECTS_LAYER){
			this.parent.mSceneMasterMenu.setChildSceneModal(this.parent.mSceneObjectLayerMenu);
			return true;
		}else if(CLICK == DRAW_OBJECTS_LAYER_BACK){
			this.parent.mSceneObjectLayerMenu.back();
			return true;
		}else if(CLICK == DRAW_OBJECTS_BACK){
			this.parent.mSceneObjectMenu.back();
			return true;
		}else {
			return false;
		}
	}
	
	private boolean handle_colour_selection(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY){
		final int CLICK = pMenuItem.getID();
		if(CLICK == COLOUR_BLUE){
			this.attributes.SELECTION_COLOUR = COLOUR_BLUE;
			this.parent.mSceneColourMenu.back();
			return true;
		} else if(CLICK == COLOUR_RED){
			this.attributes.SELECTION_COLOUR = COLOUR_RED;
			this.parent.mSceneColourMenu.back();
			return true;
		} else if(CLICK == COLOUR_YELLOW){
			this.attributes.SELECTION_COLOUR = COLOUR_YELLOW;
			this.parent.mSceneColourMenu.back();
			return true;
		} else if(CLICK == COLOUR_GREEN){
			this.attributes.SELECTION_COLOUR = COLOUR_GREEN;
			this.parent.mSceneColourMenu.back();
			return true;
		}else if(CLICK == COLOUR_BACK){
			this.parent.mSceneColourMenu.back();
			return true;
		} else {
			return false;
		}
	}
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
