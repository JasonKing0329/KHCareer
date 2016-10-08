package com.king.mytennis.view;

import com.king.mytennis.service.ThemeManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ChangeThemeDialog extends CustomDialog implements OnItemClickListener {

	private String[] themes;
	private int selectedIndex;
	private View lastChosedView;

	public ChangeThemeDialog(Context context,
							 OnCustomDialogActionListener actionListener) {
		super(context, actionListener);
		setTitle(getContext().getResources().getString(R.string.menu_change_theme));
	}

	@Override
	protected View getCustomView() {
		ListView view = new ListView(getContext());
		view.setDivider(null);
		ThemeManager manager = new ThemeManager(getContext());
		themes = manager.getThemes();
		ThemeAdapter adapter = new ThemeAdapter(manager.getThemesDrawables());
		view.setAdapter(adapter);
		view.setOnItemClickListener(this);
		return view;
	}

	@Override
	protected View getCustomToolbar() {

		return null;
	}

	@Override
	public void onClick(View view) {
		if (view == saveButton) {
			new ThemeManager(getContext()).saveTheme(themes[selectedIndex]);
			actionListener.onSave(null);
		}
		super.onClick(view);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		selectedIndex = position;
		if (lastChosedView != null) {
			lastChosedView.setBackground(null);
		}
		lastChosedView = view;
		view.setBackgroundColor(getContext().getResources().getColor(R.color.title_court_clay));
	}

	private class ThemeAdapter extends BaseAdapter {

		private int[] drawables;
		public ThemeAdapter(int[] drawables) {
			this.drawables = drawables;
		}
		@Override
		public int getCount() {
			return drawables.length;
		}

		@Override
		public Object getItem(int position) {
			return drawables[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = LayoutInflater.from(context).inflate(R.layout.adapter_theme, null);
			ImageView v = (ImageView) view.findViewById(R.id.theme_chooser_item);
			v.setImageResource(drawables[position]);
			return view;
		}

	}
	
/*
	private class CustomArrayAdapter<T> extends ArrayAdapter<T>
	{
	    public CustomArrayAdapter(Context ctx, T [] objects)
	    {
	        super(ctx, android.R.layout.simple_dropdown_item_1line, objects);
	    }

	    //其它构造函数

	    @Override
	    public View getDropDownView(int position, View convertView, ViewGroup parent)
	    {
	        View view = super.getView(position, convertView, parent);

//	            TextView text = (TextView)view.findViewById(android.R.id.text1);
//	            text.setTextColor(getContext().getResources().getColor(R.color.black));//choose your color :) 

	        return view;

	    }
	}
	*/
}
