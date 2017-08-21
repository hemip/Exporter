package com.teraim.exporter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.teraim.exporter.JSONify.JSON_Report;
import com.teraim.strand.Provyta;

public class ProvytaAdapter extends BaseAdapter {

	private LayoutInflater mLayoutInflater;
	private List<Provyta> pyList;
	private Map<CheckBox,String> isCheckedL;
	private Map<String,JSON_Report> jsonL;
	private Context myCtx;


	public ProvytaAdapter(Context context, List<Provyta> pyList,Map<String,JSON_Report> jsonL) {
		mLayoutInflater = LayoutInflater.from(context);
		this.pyList=pyList;
		isCheckedL = new HashMap<CheckBox,String>();
		this.jsonL=jsonL;
		myCtx = context;
	}

	public Context getContext() {
		return myCtx;
	}
	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

        final Provyta py = pyList.get(position);
        Log.d("Strand","GetView called for position "+position+" and pyid "+py.getpyID());


		final JSON_Report json = jsonL.get(py.getpyID());

		if(convertView==null) {
			convertView = mLayoutInflater.inflate(R.layout.pylist_row, null);
			final CheckBox cb = ((CheckBox) convertView.findViewById(R.id.export));
			cb.setChecked(py.isLocked());
			isCheckedL.put(cb, py.getpyID());
			if (cb.isChecked())
				Log.d("pytex ", "isChecked: " + py.getpyID());
		}

		((TextView)convertView.findViewById(R.id.pyName)).setText(py.getpyID());
		((TextView)convertView.findViewById(R.id.markedReady)).setText(py.isLocked()?"Ja":"Nej");
		//((TextView)convertView.findViewById(R.id.markedExported)).setText("_");
		TextView tomma = ((TextView)convertView.findViewById(R.id.tomma));
		tomma.setText(Integer.toString(json.empty.size()));
		tomma.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String out = format(json.empty);
				AlertDialog.Builder builder = new AlertDialog.Builder(ProvytaAdapter.this.getContext());
				builder.setTitle("Variabler som inte angivits")
						.setMessage(out).setPositiveButton("Ok", null)
						.show();
				Log.d("v",json.json.toString());
			}

			private String format(List<String> empty) {
				String out ="";
				int rows = 4;
				int rc=0;
				for(String s:empty) {
					if(rc<rows) {
						rc++;
						out+=s+", ";
					} else {
						rc=0;
						out+=s+"\n";
					}
				}
				return out;
			}

		});

		return convertView;
	}

	public Map<CheckBox,String> getCheckedRows() {
		return isCheckedL;
	}

	@Override
	public int getCount() {
		Log.d("Strand","Getcount called");
		return pyList.size();
	}

	@Override
	public Object getItem(int position) {
		Log.d("Strand","GetItem called");
		return pyList.get(position);
	}

	@Override
	public long getItemId(int position) {
		Log.d("Strand","GetItemId called");
		return position;
	}



}
