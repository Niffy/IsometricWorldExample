/**
 * 
 */
package com.niffy.TMXIsometricExample;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.extension.tmx.TMXObjectGroup;
import org.andengine.extension.tmx.util.constants.TMXIsometricConstants;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.opengl.GLES20;

/**
 * @author Paul Robinson
 *
 */
public class MenuDrawer implements MenuConstants {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private TMXIsometricExampleActivity parent = null;
	private MenuAttributes attributes = null;
	private VertexBufferObjectManager vboManager = null;
	private FontManager mFontManager = null;
	private Background mMenuBackground = null;
	org.andengine.util.color.Color selected = new org.andengine.util.color.Color(1f, 0f, 0f);
	org.andengine.util.color.Color unselected = new org.andengine.util.color.Color(0f, 0f, 0f);

	// ===========================================================
	// Constructors
	// ===========================================================

	public MenuDrawer(final TMXIsometricExampleActivity pParent, final Background pBackground,
			final FontManager pFontManager, final MenuAttributes pAttributes){
		this.parent = pParent;
		this.vboManager = this.parent.getVertexBufferObjectManager();
		this.mMenuBackground = pBackground;
		this.mFontManager = pFontManager;
		this.attributes = pAttributes;
	}


	// ===========================================================
	// Getter & Setter
	// ===========================================================

	private VertexBufferObjectManager getVertexBufferObjectManager() {
		return this.vboManager;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public MenuScene create_master_menu(){
		MenuScene masterMenu = new MenuScene(this.parent.getEngine().getCamera());
		masterMenu.setBackground(this.mMenuBackground);

		TextMenuItem selectDrawText = new TextMenuItem(DRAW_METHOD, this.mFontManager.Font_Master_Menu, "Select Isometric Drawing Method", this.getVertexBufferObjectManager());
		final IMenuItem SelectDrawItem = new ColorMenuItemDecorator(selectDrawText,selected , unselected);
		SelectDrawItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		TextMenuItem selectTMXMAPText = new TextMenuItem(SELECT_TMX_MAP, this.mFontManager.Font_Master_Menu, "Select Map", this.getVertexBufferObjectManager());
		final IMenuItem SelectTMXMapItem = new ColorMenuItemDecorator(selectTMXMAPText,selected , unselected);
		SelectTMXMapItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		TextMenuItem selectTMXDrawObjectsText = new TextMenuItem(DRAW_OBJECTS, this.mFontManager.Font_Master_Menu, "Select Objects layers to draw", this.getVertexBufferObjectManager());
		final IMenuItem SelectTMXDrawObjectsItem = new ColorMenuItemDecorator(selectTMXDrawObjectsText,selected , unselected);
		SelectTMXDrawObjectsItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		TextMenuItem enableTileHitText = new TextMenuItem(SELECT_TILE_HIT, this.mFontManager.Font_Master_Menu, "Tile touch hits options", this.getVertexBufferObjectManager());
		final IMenuItem EnableTileHitItem = new ColorMenuItemDecorator(enableTileHitText,selected , unselected);
		EnableTileHitItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		TextMenuItem RemoveText = new TextMenuItem(REMOVE_LINES, this.mFontManager.Font_Master_Menu, "Remove lines drawn", this.getVertexBufferObjectManager());
		final IMenuItem RemoveItem = new ColorMenuItemDecorator(RemoveText,selected , unselected);
		RemoveItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		TextMenuItem CameraResetText = new TextMenuItem(RESET_CAMERA, this.mFontManager.Font_Master_Menu, "Reset Camera", this.getVertexBufferObjectManager());
		final IMenuItem CameraResetItem = new ColorMenuItemDecorator(CameraResetText,selected , unselected);
		CameraResetItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		TextMenuItem BackText = new TextMenuItem(BACK_TO_GAME, this.mFontManager.Font_Master_Menu, "Back to map", this.getVertexBufferObjectManager());
		final IMenuItem BackItem = new ColorMenuItemDecorator(BackText,selected , unselected);
		BackItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		masterMenu.addMenuItem(SelectDrawItem);
		masterMenu.addMenuItem(SelectTMXMapItem);
		masterMenu.addMenuItem(SelectTMXDrawObjectsItem);
		masterMenu.addMenuItem(EnableTileHitItem);
		masterMenu.addMenuItem(RemoveItem);
		masterMenu.addMenuItem(CameraResetItem);
		masterMenu.addMenuItem(BackItem);
		masterMenu.buildAnimations();
		masterMenu.setBackgroundEnabled(true);
		masterMenu.setOnMenuItemClickListener(this.parent);
		return masterMenu;
	}

	public MenuScene create_map_type_selection(){
		MenuScene menu = new MenuScene(this.parent.getEngine().getCamera());
		menu.setBackground(this.mMenuBackground);

		TextMenuItem selectIsoText = new TextMenuItem(SELECT_TMX_MAP_ISO, this.mFontManager.Font_TMX_Select_type, "Isometric Map", this.getVertexBufferObjectManager());
		final IMenuItem selectIso = new ColorMenuItemDecorator(selectIsoText,selected , unselected);
		selectIso.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		TextMenuItem selectOrthoText = new TextMenuItem(SELECT_TMX_MAP_ORTHO, this.mFontManager.Font_TMX_Select_type, "Orthographic Map", this.getVertexBufferObjectManager());
		final IMenuItem selectOrtho = new ColorMenuItemDecorator(selectOrthoText,selected , unselected);
		selectOrtho.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		TextMenuItem backText = new TextMenuItem(SELECT_TMX_MAP_BACK, this.mFontManager.Font_TMX_Select_type, "Back", this.getVertexBufferObjectManager());
		final IMenuItem backItem = new ColorMenuItemDecorator(backText,selected , unselected);
		backItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		menu.addMenuItem(selectIso);
		menu.addMenuItem(selectOrtho);
		menu.addMenuItem(backItem);
		menu.buildAnimations();
		menu.setBackgroundEnabled(true);
		menu.setOnMenuItemClickListener(this.parent);
		return menu;
	}

	public MenuScene create_TMX_selection_isometric(){
		MenuScene menu = new MenuScene(this.parent.getEngine().getCamera());
		menu.setBackground(this.mMenuBackground);

		for (int i = 0; i < this.parent.TMXFilesIsometric.length; i++){
			TextMenuItem mapText = new TextMenuItem(MAP_SELECTED_ISO, this.mFontManager.Font_TMX_Select_iso, this.parent.TMXFilesIsometric[i], this.getVertexBufferObjectManager());
			final IMenuItem mapItem = new ColorMenuItemDecorator(mapText,selected , unselected);
			mapItem.setUserData(i);
			mapItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
			menu.addMenuItem(mapItem);
		}

		TextMenuItem backText = new TextMenuItem(SELECT_TMX_MAP_ISO_BACK, this.mFontManager.Font_TMX_Select_iso, "Back", this.getVertexBufferObjectManager());
		final IMenuItem backItem = new ColorMenuItemDecorator(backText,selected , unselected);
		backItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		menu.addMenuItem(backItem);
		menu.buildAnimations();
		menu.setBackgroundEnabled(true);
		menu.setOnMenuItemClickListener(this.parent);
		return menu;
	}

	public MenuScene create_TMX_selection_orthographic(){
		MenuScene menu = new MenuScene(this.parent.getEngine().getCamera());
		menu.setBackground(this.mMenuBackground);

		for (int i = 0; i < this.parent.TMXFilesOrthographic.length; i++){
			TextMenuItem mapText = new TextMenuItem(MAP_SELECTED_ORTHO, this.mFontManager.Font_TMX_Select_ortho, this.parent.TMXFilesOrthographic[i], this.getVertexBufferObjectManager());
			final IMenuItem mapItem = new ColorMenuItemDecorator(mapText,selected , unselected);
			mapItem.setUserData(i);
			mapItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
			menu.addMenuItem(mapItem);
		}

		TextMenuItem backText = new TextMenuItem(SELECT_TMX_MAP_ORTHO_BACK, this.mFontManager.Font_TMX_Select_ortho, "Back", this.getVertexBufferObjectManager());
		final IMenuItem backItem = new ColorMenuItemDecorator(backText,selected , unselected);
		backItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		menu.addMenuItem(backItem);
		menu.buildAnimations();
		menu.setBackgroundEnabled(true);
		menu.setOnMenuItemClickListener(this.parent);
		return menu;
	}

	public MenuScene create_object_menu(){
		MenuScene menu = new MenuScene(this.parent.getEngine().getCamera());
		menu.setBackground(this.mMenuBackground);

		TextMenuItem allText = new TextMenuItem(DRAW_OBJECTS_ALL, this.mFontManager.Font_Object_Menu, "All", this.getVertexBufferObjectManager());
		final IMenuItem allItem = new ColorMenuItemDecorator(allText,selected , unselected);
		allItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		TextMenuItem layerText = new TextMenuItem(DRAW_OBJECTS_LAYER, this.mFontManager.Font_Object_Menu, "Select a Layer", this.getVertexBufferObjectManager());
		final IMenuItem layerItem = new ColorMenuItemDecorator(layerText,selected , unselected);
		layerItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		TextMenuItem backText = new TextMenuItem(DRAW_OBJECTS_BACK, this.mFontManager.Font_Object_Menu, "Back", this.getVertexBufferObjectManager());
		final IMenuItem backItem = new ColorMenuItemDecorator(backText,selected , unselected);
		backItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		menu.addMenuItem(allItem);
		menu.addMenuItem(layerItem);
		menu.addMenuItem(backItem);
		menu.buildAnimations();
		menu.setBackgroundEnabled(true);
		menu.setOnMenuItemClickListener(this.parent);
		return menu;
	}

	public MenuScene create_object_layer(){
		MenuScene menu = new MenuScene(this.parent.getEngine().getCamera());
		menu.setBackground(this.mMenuBackground);
		int count = 0;
		for (TMXObjectGroup objG : this.parent.mMap.getTMXObjectGroups()) {
			String strName = String.format("%d - %s", count , objG.getName());
			TextMenuItem mapText = new TextMenuItem(OBJECT_LAYER_SELECTED, this.mFontManager.Font_Object_Menu, strName, this.getVertexBufferObjectManager());
			final IMenuItem mapItem = new ColorMenuItemDecorator(mapText,selected , unselected);
			mapItem.setUserData(objG.getName());
			mapItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
			menu.addMenuItem(mapItem);
			count++;
		}
		
		TextMenuItem backText = new TextMenuItem(DRAW_OBJECTS_LAYER_BACK, this.mFontManager.Font_Object_Menu, "Back", this.getVertexBufferObjectManager());
		final IMenuItem backItem = new ColorMenuItemDecorator(backText,selected , unselected);
		backItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		menu.addMenuItem(backItem);
		menu.buildAnimations();
		menu.setBackgroundEnabled(true);
		menu.setOnMenuItemClickListener(this.parent);
		return menu;
	}

	public MenuScene create_drawing_method_menu(){
		MenuScene menu = new MenuScene(this.parent.getEngine().getCamera());
		menu.setBackground(this.mMenuBackground);

		TextMenuItem selectAllText = new TextMenuItem(DRAW_METHOD_SELECTED, this.mFontManager.Font_Draw_Method, "DRAW_METHOD_ISOMETRIC_ALL", this.getVertexBufferObjectManager());
		final IMenuItem SelectAllItem = new ColorMenuItemDecorator(selectAllText,selected , unselected);
		SelectAllItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		SelectAllItem.setUserData(TMXIsometricConstants.DRAW_METHOD_ISOMETRIC_ALL);

		TextMenuItem selectSlimText = new TextMenuItem(DRAW_METHOD_SELECTED, this.mFontManager.Font_Draw_Method, "DRAW_METHOD_ISOMETRIC_CULLING_SLIM ", this.getVertexBufferObjectManager());
		final IMenuItem SelectSlimItem = new ColorMenuItemDecorator(selectSlimText,selected , unselected);
		SelectSlimItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		SelectSlimItem.setUserData(TMXIsometricConstants.DRAW_METHOD_ISOMETRIC_CULLING_SLIM);

		TextMenuItem selectPaddingText = new TextMenuItem(DRAW_METHOD_SELECTED, this.mFontManager.Font_Draw_Method, "DRAW_METHOD_ISOMETRIC_CULLING_PADDING", this.getVertexBufferObjectManager());
		final IMenuItem SelectPaddingItem = new ColorMenuItemDecorator(selectPaddingText,selected , unselected);
		SelectPaddingItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		SelectPaddingItem.setUserData(TMXIsometricConstants.DRAW_METHOD_ISOMETRIC_CULLING_PADDING);

		TextMenuItem selectTiledText = new TextMenuItem(DRAW_METHOD_SELECTED, this.mFontManager.Font_Draw_Method, "DRAW_METHOD_ISOMETRIC_CULLING_TILED_SOURCE", this.getVertexBufferObjectManager());
		final IMenuItem SelectTiledItem = new ColorMenuItemDecorator(selectTiledText,selected , unselected);
		SelectTiledItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		SelectTiledItem.setUserData(TMXIsometricConstants.DRAW_METHOD_ISOMETRIC_CULLING_TILED_SOURCE);

		TextMenuItem selectBackText = new TextMenuItem(DRAW_METHOD_BACK, this.mFontManager.Font_Draw_Method, "Back", this.getVertexBufferObjectManager());
		final IMenuItem SelectBackItem = new ColorMenuItemDecorator(selectBackText,selected , unselected);
		SelectBackItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		menu.addMenuItem(SelectAllItem);
		menu.addMenuItem(SelectSlimItem);
		menu.addMenuItem(SelectPaddingItem);
		menu.addMenuItem(SelectTiledItem);
		menu.addMenuItem(SelectBackItem);
		menu.buildAnimations();
		menu.setBackgroundEnabled(true);
		menu.setOnMenuItemClickListener(this.parent);
		return menu;
	}

	public MenuScene create_tile_hit_menu(){
		MenuScene menu = new MenuScene(this.parent.getEngine().getCamera());
		menu.setBackground(this.mMenuBackground);

		if(!this.attributes.ENABLE_TILE_HIT){
			TextMenuItem tile_enable_Text = new TextMenuItem(TILE_ENABLE_DISABLE, this.mFontManager.Font_Tile_Hit, "Enable", this.getVertexBufferObjectManager());
			final IMenuItem Tile_Enable_Item = new ColorMenuItemDecorator(tile_enable_Text,selected , unselected);
			Tile_Enable_Item.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
			menu.addMenuItem(Tile_Enable_Item);
		}else{
			TextMenuItem tile_enable_Text = new TextMenuItem(TILE_ENABLE_DISABLE, this.mFontManager.Font_Tile_Hit, "Disable", this.getVertexBufferObjectManager());
			final IMenuItem Tile_Enable_Item = new ColorMenuItemDecorator(tile_enable_Text,selected , unselected);
			Tile_Enable_Item.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
			menu.addMenuItem(Tile_Enable_Item);
		}

		TextMenuItem hit_colour_Text = new TextMenuItem(TILE_SELECT_COLOUR, this.mFontManager.Font_Tile_Hit, "Hit colour", this.getVertexBufferObjectManager());
		final IMenuItem hit_colour_Item = new ColorMenuItemDecorator(hit_colour_Text,selected , unselected);
		hit_colour_Item.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		TextMenuItem tile_centre_Text = new TextMenuItem(TILE_HIT_CENTRE, this.mFontManager.Font_Tile_Hit, "Tile Centre", this.getVertexBufferObjectManager());
		final IMenuItem Tile_CentreP_Item = new ColorMenuItemDecorator(tile_centre_Text,selected , unselected);
		Tile_CentreP_Item.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		TextMenuItem tile_point_Text = new TextMenuItem(TILE_HIT_POINT, this.mFontManager.Font_Tile_Hit, "Tile Point", this.getVertexBufferObjectManager());
		final IMenuItem Tile_point_Item = new ColorMenuItemDecorator(tile_point_Text,selected , unselected);
		Tile_point_Item.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		if(!this.attributes.TILE_HIT_SOUND_ENABLED){
			TextMenuItem tile_enable_Text = new TextMenuItem(TILE_HIT_SOUND, this.mFontManager.Font_Tile_Hit, "Enable Sound", this.getVertexBufferObjectManager());
			final IMenuItem Tile_Enable_Item = new ColorMenuItemDecorator(tile_enable_Text,selected , unselected);
			Tile_Enable_Item.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
			menu.addMenuItem(Tile_Enable_Item);
		}else{
			TextMenuItem tile_enable_Text = new TextMenuItem(TILE_HIT_SOUND, this.mFontManager.Font_Tile_Hit, "Disable Sound", this.getVertexBufferObjectManager());
			final IMenuItem Tile_Enable_Item = new ColorMenuItemDecorator(tile_enable_Text,selected , unselected);
			Tile_Enable_Item.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
			menu.addMenuItem(Tile_Enable_Item);
		}
		
		if(!this.attributes.TILE_HIT_VIBRATE_ENABLED){
			TextMenuItem tile_enable_Text = new TextMenuItem(TILE_HIT_VIBRATE, this.mFontManager.Font_Tile_Hit, "Enable Vibrate", this.getVertexBufferObjectManager());
			final IMenuItem Tile_Enable_Item = new ColorMenuItemDecorator(tile_enable_Text,selected , unselected);
			Tile_Enable_Item.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
			menu.addMenuItem(Tile_Enable_Item);
		}else{
			TextMenuItem tile_enable_Text = new TextMenuItem(TILE_HIT_VIBRATE, this.mFontManager.Font_Tile_Hit, "Disable Vibrate", this.getVertexBufferObjectManager());
			final IMenuItem Tile_Enable_Item = new ColorMenuItemDecorator(tile_enable_Text,selected , unselected);
			Tile_Enable_Item.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
			menu.addMenuItem(Tile_Enable_Item);
		}
		
		TextMenuItem back_Text = new TextMenuItem(TILE_HIT_BACK, this.mFontManager.Font_Tile_Hit, "Back", this.getVertexBufferObjectManager());
		final IMenuItem back_Item = new ColorMenuItemDecorator(back_Text,selected , unselected);
		back_Item.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		menu.addMenuItem(hit_colour_Item);
		menu.addMenuItem(Tile_CentreP_Item);
		menu.addMenuItem(Tile_point_Item);
		menu.addMenuItem(back_Item);
		menu.buildAnimations();
		menu.setBackgroundEnabled(true);
		menu.setOnMenuItemClickListener(this.parent);
		return menu;
	}

	public MenuScene create_colour_selection_menu(){
		MenuScene menu = new MenuScene(this.parent.getEngine().getCamera());
		menu.setBackground(this.mMenuBackground);

		TextMenuItem blue_Text = new TextMenuItem(COLOUR_BLUE, this.mFontManager.Font_Colour, "Blue", this.getVertexBufferObjectManager());
		final IMenuItem blue_Item = new ColorMenuItemDecorator(blue_Text,selected , unselected);
		blue_Item.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		menu.addMenuItem(blue_Item);

		TextMenuItem green_Text = new TextMenuItem(COLOUR_GREEN, this.mFontManager.Font_Colour, "Green", this.getVertexBufferObjectManager());
		final IMenuItem green_Item = new ColorMenuItemDecorator(green_Text,selected , unselected);
		green_Item.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		menu.addMenuItem(green_Item);

		TextMenuItem red_Text = new TextMenuItem(COLOUR_RED, this.mFontManager.Font_Colour, "Red", this.getVertexBufferObjectManager());
		final IMenuItem red_Item = new ColorMenuItemDecorator(red_Text,selected , unselected);
		red_Item.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		menu.addMenuItem(red_Item);

		TextMenuItem yellow_Text = new TextMenuItem(COLOUR_YELLOW, this.mFontManager.Font_Colour, "Yellow", this.getVertexBufferObjectManager());
		final IMenuItem yellow_Item = new ColorMenuItemDecorator(yellow_Text,selected , unselected);
		yellow_Item.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		menu.addMenuItem(yellow_Item);

		TextMenuItem back_Text = new TextMenuItem(COLOUR_BACK, this.mFontManager.Font_Colour, "Back", this.getVertexBufferObjectManager());
		final IMenuItem back_Item = new ColorMenuItemDecorator(back_Text,selected , unselected);
		back_Item.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		menu.addMenuItem(back_Item);

		menu.buildAnimations();
		menu.setBackgroundEnabled(true);
		menu.setOnMenuItemClickListener(this.parent);
		return menu;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
