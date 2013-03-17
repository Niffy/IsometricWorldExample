package com.niffy.IsometricWorld;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.niffy.IsometricWorld.entity.CubeTemplate;

public class ObjectListAdapter extends BaseAdapter {
	// ===========================================================
	// Constants
	// ===========================================================
	private final Logger log = LoggerFactory.getLogger(ObjectListAdapter.class);

	// ===========================================================
	// Fields
	// ===========================================================
	private static LayoutInflater inflater = null;
	private ArrayList<CubeTemplate> mData;

	// ===========================================================
	// Constructors
	// ===========================================================

	public ObjectListAdapter(final ArrayList<CubeTemplate> pData, Context pContext) throws Exception {
		this.mData = pData;
		ObjectListAdapter.inflater = (LayoutInflater) pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (ObjectListAdapter.inflater == null) {
			log.error("Could not get the LayoutInflater from context");
			throw new Exception("Could not get the LayoutInflater from context");
		}
	}

	@Override
	public int getCount() {
		return this.mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (ObjectListAdapter.inflater == null) {
			log.error("Cannot inflate list view, inflater is null");
		} else {
			v = inflater.inflate(R.layout.list_row, null);
		}

		if (v != null) {
			CubeTemplate data = this.mData.get(position);
			if (data != null) {
				TextView txLabel = (TextView) v.findViewById(R.id.list_row_text);
				v.setTag(position);
				if (txLabel != null) {
					txLabel.setText(data.getmFileName());
				}
			}
		}
		return v;
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
