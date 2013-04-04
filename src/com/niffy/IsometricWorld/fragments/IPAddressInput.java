package com.niffy.IsometricWorld.fragments;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.niffy.IsometricWorld.R;
import com.niffy.IsometricWorld.misc.TextValidator;

public class IPAddressInput extends DialogFragment {
	// ===========================================================
	// Constants
	// ===========================================================
	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(IPAddressInput.class);

	// ===========================================================
	// Fields
	// ===========================================================
	protected View mView;
	protected IDialogInputReturn mParent;
	protected TextView mTextView;
	protected EditText mEditView;
	protected TextView mTextViewButton;
	private String mHostIPString;
	private static final String IP_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	private Pattern mIP_Pattern = Pattern.compile(IP_PATTERN);

	// ===========================================================
	// Constructors
	// ===========================================================
	/**
	 * Create a text dialog to display a message.
	 * 
	 * @param pTitle
	 *            {@link String} of title to use
	 * @param pText
	 *            {@link String} of text to display
	 * @param pHint
	 *            {@link String} of text to display as hint in {@link EditText}
	 *            view
	 */
	public IPAddressInput(IDialogInputReturn pParent) {
		this.mParent = pParent;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().setCanceledOnTouchOutside(true);
		this.mView = inflater.inflate(R.layout.fragment_dialog_input, container);
		this.mTextView = (TextView) this.mView.findViewById(R.id.fragment_dialog_input_textview);
		this.mEditView = (EditText) this.mView.findViewById(R.id.fragment_dialog_input_input);
		this.mTextViewButton = (TextView) this.mView.findViewById(R.id.fragment_dialog_input_ok);
		this.mTextViewButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mParent.returnInput(mHostIPString);
				getDialog().dismiss();
			}
		});

		this.mTextViewButton.addTextChangedListener(new TextValidator(this.mEditView) {

			@Override
			public void validate(TextView textView, String pText) {
				Matcher matcher = mIP_Pattern.matcher(pText);
				boolean matches = matcher.matches();
				if (matches) {
					mHostIPString = pText;
				} else {
					mHostIPString = null;
				}
			}
		});
		return mView;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	public interface IDialogInputReturn {
		public void returnInput(final String pInput);
	}
}
