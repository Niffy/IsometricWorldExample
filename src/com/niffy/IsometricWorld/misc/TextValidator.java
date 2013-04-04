package com.niffy.IsometricWorld.misc;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

public abstract class TextValidator implements TextWatcher {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private TextView mTextView;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TextValidator(TextView pTextView) {
		this.mTextView = pTextView;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	final public void afterTextChanged(Editable s) {
		String text = this.mTextView.getText().toString();
		validate(this.mTextView, text);
	}

	@Override
	final public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* Don't care */
	}

	@Override
	final public void onTextChanged(CharSequence s, int start, int before, int count) { /* Don't care */
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	public abstract void validate(TextView pTextView, String pText);
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
