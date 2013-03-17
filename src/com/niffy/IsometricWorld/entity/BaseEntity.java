package com.niffy.IsometricWorld.entity;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * An entity which humans and such should extend, could even possible be used in future for cube cars!
 * @author Paul Robinson
 * @since 10 Nov 2012 14:22:30
 */
public class BaseEntity {
	// ===========================================================
	// Constants
	// ===========================================================
	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(BaseEntity.class);

	// ===========================================================
	// Fields
	// ===========================================================
	protected float mX;
	protected float mY;
	protected UUID mUUID;
	/**
	 * [0] = X offset
	 * [1] = Y Offset
	 */
	protected float[] mOffset = new float[] {0,0};
	/**
	 * Is the entity executing a path?
	 */
	protected boolean mExecutingPath = false;
	// ===========================================================
	// Constructors
	// ===========================================================

	public BaseEntity() {

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

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
