package com.niffy.IsometricWorld.touch;

import android.R.integer;
import android.util.SparseArray;

import com.niffy.IsometricWorld.GenericClassCastException;

public enum ETouchStatus {
	DEFAULT(-1);

	/**
	 * The ETouchStatus ID
	 */
	private int mNumber;

	/**
	 * Constructor for ETouchStatus
	 * 
	 * @param pNumber
	 *            {@link integer} of the ETouchStatus ID.
	 */
	private ETouchStatus(int pNumber) {
		this.mNumber = pNumber;
	}

	/**
	 * Get the ID of the current ETouchStatus.
	 * 
	 * @return {@link integer} of the ID of the current ETouchStatus
	 */
	public int getNumber() {
		return this.mNumber;
	}

	/**
	 * This is used to find the ID of each ETouchStatus compared against a
	 * given
	 */
	private static final SparseArray<ETouchStatus> lookup = new SparseArray<ETouchStatus>();
	static {
		for (ETouchStatus h : ETouchStatus.values())
			lookup.put(h.getNumber(), h);
	}

	/**
	 * Finds a ETouchStatus type that is related to an ID, e.g ID 2 =
	 * {@link #BUILD}
	 * 
	 * @param pTouchStatus
	 *            {@link Integer} The ID you wish to know the ETouchStatus
	 *            of.
	 * @return {@link ETouchStatus} The ETouchStatus of the given ID. or
	 *         NULL if the ETouchStatus does not exist.
	 * @throws @link {@link GenericClassCastException} When a given object (ID)
	 *         null;
	 */
	public static ETouchStatus get(int pTouchStatus) throws GenericClassCastException {
		ETouchStatus value = null;
		try {
			value = lookup.get(pTouchStatus);
		} catch (ClassCastException CEE) {
			// Debug.e(CEE);
			throw new GenericClassCastException("ETouchStatus: Given value is incorect object, should of type INT",
					CEE);
		} catch (NullPointerException NPE) {
			// Debug.e(NPE);
			throw new GenericClassCastException("ETouchStatus: Given value is NULL,", NPE);
		}
		return value;
	}
}
