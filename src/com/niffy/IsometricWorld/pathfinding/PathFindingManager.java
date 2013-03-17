package com.niffy.IsometricWorld.pathfinding;

import org.andengine.extension.tmx.TMXLayer;
import org.andengine.util.algorithm.path.Path;
import org.andengine.util.algorithm.path.astar.tile.AStarPathFinderTileBased;
import org.andengine.util.algorithm.path.astar.tile.ITileAStarHeuristic;
import org.andengine.util.algorithm.path.astar.tile.ITileCostFunction;
import org.andengine.util.algorithm.path.astar.tile.ITilePathFinderMap;
import org.andengine.util.algorithm.path.astar.tile.mod.AStarPathTileModifierSimple;
import org.andengine.util.algorithm.path.astar.tile.pool.AStarPathTilePoolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.niffy.IsometricWorld.MapHandler;
import com.niffy.IsometricWorld.TileManager;
import com.niffy.IsometricWorld.entity.IHumanEntity;

public class PathFindingManager {
	// ===========================================================
	// Constants
	// ===========================================================
	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(PathFindingManager.class);

	// ===========================================================
	// Fields
	// ===========================================================
	private MapHandler mMapManager;
	protected TileManager mTileManager;
	protected AStarPathFinderTileBased<TMXLayer> mTilePathFinder;
	protected ITilePathFinderMap<TMXLayer> mTilePathFinderMap;
	protected ITileAStarHeuristic<TMXLayer> mTileHeuristic;
	protected ITileCostFunction<TMXLayer> mTileCostFunction;

	protected final float[] noOffset = new float[] { 0, 0 };
	protected int[] normalTileDie;
	protected int[] flipedTileDie;

	// ===========================================================
	// Constructors
	// ===========================================================

	public PathFindingManager(final MapHandler pMapManager, final TileManager pTileManager) {
		this.mMapManager = pMapManager;
		this.mTileManager = pTileManager;
		this.normalTileDie = this.mMapManager.getTileDimensions();
		this.flipedTileDie = new int[] { this.normalTileDie[1], this.normalTileDie[0] };
		this.mTilePathFinder = new AStarPathFinderTileBased<TMXLayer>(
				new AStarPathTilePoolManager(1000, 1000, 500, 500));
		this.mTileHeuristic = new ITileAStarHeuristic<TMXLayer>() {

			@Override
			public float getExpectedRestCost(ITilePathFinderMap<TMXLayer> pPathFinderMap, TMXLayer pEntity,
					int pFromRow, int pFromCol, int pToRow, int pToCol) {
				float[] n = pMapManager.getTMXLayer().getTileCentre(pFromCol, pFromRow);
				float[] g = pMapManager.getTMXLayer().getTileCentre(pToCol, pToRow);
				float x = (Math.abs(n[0] - g[0]) + Math.abs(n[1] - g[1]));
				return x;
			}
		};
		this.mTilePathFinderMap = new ITilePathFinderMap<TMXLayer>() {

			@Override
			public boolean isBlocked(int pRow, int pCol, TMXLayer pEntity) {
				if (pCol < 0 || pCol > pMapManager.getTMXLayer().getTileColumns()) {
					// Not in bounds on column
					return true;
				}
				if (pRow < 0 || pRow > pMapManager.getTMXLayer().getTileRows()) {
					// not in bounds on row.
					return true;
				}

				return mTileManager.isBlocked(pRow, pCol);
			}
		};

		this.mTileCostFunction = new ITileCostFunction<TMXLayer>() {

			@Override
			public float getCost(ITilePathFinderMap<TMXLayer> pTilePathFinderMap, int pFromRow, int pFromCol,
					int pToRow, int pToCol, TMXLayer pEntity) {

				return 10;
			}
		};

	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	public void findPath(IHumanEntity pEntity, final float[] pTouchLocation) {
		float[] currentCoordinate = pEntity.getCurrentCoordinateNoOffset();
		final int[] entityCoordinateTile = this.mMapManager.getTileAt(currentCoordinate);
		final int[] entityDestinationTile = this.mMapManager.getTileAt(pTouchLocation);
		if (entityCoordinateTile == null || entityDestinationTile == null) {
			return;
		}

		Path pPath = this.getPathBetweenLocations(entityCoordinateTile, entityDestinationTile);

		if (pPath != null) {
			PathTileModListener modListner = new PathTileModListener(pEntity);
			pPath.switchXY(true);
			AStarPathTileModifierSimple mod = new AStarPathTileModifierSimple(1.2f, pPath, this.flipedTileDie,
					this.mMapManager.getTMXTiledMap().getMapOriginX(), this.mMapManager.getTMXTiledMap()
							.getMapOriginY(), 0, true, pEntity.getOffset(), null, modListner);
			pEntity.getAnimatedSprite().registerEntityModifier(mod);
		}

	}

	private Path getPathBetweenLocations(final int[] pFromRowColumn, final int[] pToRowColumn) {
		Path found = this.mTilePathFinder.findPath(this.mTilePathFinderMap, this.mMapManager.getTMXTiledMap()
				.getTileRows(), this.mMapManager.getTMXTiledMap().getTileColumns(), this.mMapManager.getTMXLayer(),
				pFromRowColumn[0], pFromRowColumn[1], pToRowColumn[0], pToRowColumn[1], this.mTileHeuristic,
				this.mTileCostFunction);
		if (found == null) {
			return null;
		} else {
		}
		return found;
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
