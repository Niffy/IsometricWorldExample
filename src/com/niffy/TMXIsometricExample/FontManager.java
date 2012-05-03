/**
 * 
 */
package com.niffy.TMXIsometricExample;

import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;

/**
 * @author Paul Robinson
 *
 */
public class FontManager {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private TMXIsometricExampleActivity parent = null;
	private String mFontFile = "default";
	public Font Font_Master_Menu;
	public Font Font_TMX_Select_type;
	public Font Font_TMX_Select_iso;
	public Font Font_TMX_Select_ortho;
	public Font Font_Object_Menu;
	public Font Font_Draw_Method;
	public Font Font_Tile_Hit;
	public Font Font_Colour;
	public Font Font_HUD;
	
	// ===========================================================
	// Constructors
	// ===========================================================

	public FontManager(final TMXIsometricExampleActivity pParent, final String pFont){
		this.parent = pParent;
		this.mFontFile = pFont;
		this.load();
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
	
	private void load(){
		final ITexture font_master_menu = new BitmapTextureAtlas(this.parent.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		final ITexture font_tmx_select_type_texture = new BitmapTextureAtlas(this.parent.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
		final ITexture font_tmx_select_iso = new BitmapTextureAtlas(this.parent.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
		final ITexture font_tmx_select_ortho = new BitmapTextureAtlas(this.parent.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
		final ITexture font_object_menu = new BitmapTextureAtlas(this.parent.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
		final ITexture font_draw_menu = new BitmapTextureAtlas(this.parent.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
		final ITexture font_tile_hit = new BitmapTextureAtlas(this.parent.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
		final ITexture font_colour = new BitmapTextureAtlas(this.parent.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
		final ITexture font_hud = new BitmapTextureAtlas(this.parent.getTextureManager(), 512, 512, TextureOptions.BILINEAR);
		
		this.Font_Master_Menu = FontFactory.createFromAsset(this.parent.getFontManager(), font_master_menu, this.parent.getAssets(), this.mFontFile, 35, true, android.graphics.Color.WHITE);
		this.Font_Master_Menu.load();
		this.Font_TMX_Select_type = FontFactory.createFromAsset(this.parent.getFontManager(), font_tmx_select_type_texture, this.parent.getAssets(), this.mFontFile, 40, true, android.graphics.Color.WHITE);
		this.Font_TMX_Select_type.load();
		this.Font_TMX_Select_iso = FontFactory.createFromAsset(this.parent.getFontManager(), font_tmx_select_iso, this.parent.getAssets(), this.mFontFile, 30, true, android.graphics.Color.WHITE);
		this.Font_TMX_Select_iso.load();
		this.Font_TMX_Select_ortho = FontFactory.createFromAsset(this.parent.getFontManager(), font_tmx_select_ortho, this.parent.getAssets(), this.mFontFile, 30, true, android.graphics.Color.WHITE);
		this.Font_TMX_Select_ortho.load();
		this.Font_Object_Menu = FontFactory.createFromAsset(this.parent.getFontManager(), font_object_menu, this.parent.getAssets(), this.mFontFile, 30, true, android.graphics.Color.WHITE);
		this.Font_Object_Menu.load();
		this.Font_Draw_Method = FontFactory.createFromAsset(this.parent.getFontManager(), font_draw_menu, this.parent.getAssets(), this.mFontFile, 30, true, android.graphics.Color.WHITE);
		this.Font_Draw_Method.load();
		this.Font_Tile_Hit = FontFactory.createFromAsset(this.parent.getFontManager(), font_tile_hit, this.parent.getAssets(), this.mFontFile, 35, true, android.graphics.Color.WHITE);
		this.Font_Tile_Hit.load();
		this.Font_Colour = FontFactory.createFromAsset(this.parent.getFontManager(), font_colour, this.parent.getAssets(), this.mFontFile, 40, true, android.graphics.Color.WHITE);
		this.Font_Colour.load();
		this.Font_HUD = FontFactory.createFromAsset(this.parent.getFontManager(), font_hud, this.parent.getAssets(), this.mFontFile, 30, true, android.graphics.Color.WHITE);
		this.Font_HUD.load();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
