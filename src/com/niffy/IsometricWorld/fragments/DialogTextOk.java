package com.niffy.IsometricWorld.fragments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.niffy.IsometricWorld.R;

/**
 * A dialog with a title bar, text and an OK button. <br>
 * Very useful for informing user of something.
 * @author Paul Robinson
 *
 */
public class DialogTextOk extends DialogFragment{
	// ===========================================================
	// Constants
	// ===========================================================
	//private final String TAG = "DialogTextOk";
	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(DialogTextOk.class);

	// ===========================================================
	// Fields
	// ===========================================================
	protected View view;
	protected TextView textView;
	protected TextView button;
	protected String mText;
	protected String mTitle;
	// ===========================================================
	// Constructors
	// ===========================================================
	/**
	 * Create a text dialog to display a message.
	 * @param pTitle {@link String} of title to use
	 * @param pText {@link String} of text to show
	 */
	public DialogTextOk(String pTitle, String pText) {
		this.mTitle = pTitle;
		this.mText = pText;
	}
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().setCanceledOnTouchOutside(true);
		this.view = inflater.inflate(R.layout.fragment_dialog_text_ok, container);
		this.textView = (TextView) this.view.findViewById(R.id.fragment_dialog_text_ok_textview);
		this.textView.setText(this.mText);
		this.button = (TextView)this.view.findViewById(R.id.fragment_dialog_text_ok_button_ok);
		this.button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getDialog().dismiss();
			}
		});
		this.getDialog().setTitle(this.mTitle);
		return view;
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
