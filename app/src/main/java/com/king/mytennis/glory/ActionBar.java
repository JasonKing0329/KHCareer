package com.king.mytennis.glory;

import java.util.ArrayList;
import java.util.List;

import com.king.mytennis.view.R;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class ActionBar implements OnClickListener, TextWatcher {

	private Context context;
	private ImageView backButton, menuButton, addButton, editButton, titleIcon, indexButton
			, okButton, cancelButton, searchButton, closeButton, deleteButton, saveButton, colorButton;
	private TextView titleView;
	private ActionBarListener actionBarListener;
	private ListPopupWindow menuWindow;
	private List<View> currentButtons;
	private Spinner levelSpinner, courtSpinner;
	private LinearLayout layout;
	private EditText searchEdit;
	private RelativeLayout searchLayout;

	public ActionBar(Context context, ActionBarListener listener) {
		this.context = context;
		actionBarListener = listener;
		Activity view = (Activity) context;
		layout = (LinearLayout) view.findViewById(R.id.actionbar);
		backButton = (ImageView) view.findViewById(R.id.actionbar_back);
		menuButton = (ImageView) view.findViewById(R.id.actionbar_menu);
		addButton = (ImageView) view.findViewById(R.id.actionbar_add);
		editButton = (ImageView) view.findViewById(R.id.actionbar_edit);
		indexButton = (ImageView) view.findViewById(R.id.actionbar_index);
		okButton = (ImageView) view.findViewById(R.id.actionbar_ok);
		saveButton = (ImageView) view.findViewById(R.id.actionbar_save);
		colorButton = (ImageView) view.findViewById(R.id.actionbar_color);
		cancelButton = (ImageView) view.findViewById(R.id.actionbar_cancel);
		levelSpinner = (Spinner) view.findViewById(R.id.actionbar_glory_level);
		courtSpinner = (Spinner) view.findViewById(R.id.actionbar_glory_court);
		searchButton = (ImageView) view.findViewById(R.id.actionbar_search);
		closeButton = (ImageView) view.findViewById(R.id.actionbar_search_close);
		titleIcon = (ImageView) view.findViewById(R.id.actionbar_title_icon);
		searchEdit = (EditText) view.findViewById(R.id.actionbar_search_edittext);
		searchLayout = (RelativeLayout) view.findViewById(R.id.actionbar_search_layout);
		deleteButton = (ImageView) view.findViewById(R.id.actionbar_delete);
		backButton.setOnClickListener(this);
		menuButton.setOnClickListener(this);
		addButton.setOnClickListener(this);
		indexButton.setOnClickListener(this);
		editButton.setOnClickListener(this);
		okButton.setOnClickListener(this);
		saveButton.setOnClickListener(this);
		colorButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		deleteButton.setOnClickListener(this);
		searchButton.setOnClickListener(this);
		closeButton.setOnClickListener(this);
		searchEdit.addTextChangedListener(this);
		titleView = (TextView) view.findViewById(R.id.actionbar_title);

		currentButtons = new ArrayList<View>();

	}

	public interface ActionBarListener {
		public void onBack();
		public void onDelete();
		public void onOk();
		public void onIconClick(View view);
		public void onCancel();
		public ListAdapter createMenu();
		public ListAdapter onPrepareMenu();
		public void onMenuSelected(int index);
		public void onTextChanged(String text, int start, int before, int count);
	}

	public interface ActionSpinnerListener {
		public void onTitleFilterListener(int indexLevel, int indexCourt);
	}

	public void addActionSpinnerListener(final ActionSpinnerListener listener) {

		levelSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int index, long arg3) {
				listener.onTitleFilterListener(index, courtSpinner.getSelectedItemPosition());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		courtSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int index, long arg3) {
				listener.onTitleFilterListener(levelSpinner.getSelectedItemPosition(), index);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	public void clearActionIcon() {
		for (View v:currentButtons) {
			v.setVisibility(View.GONE);
		}
		currentButtons.clear();
	}
	public void addEditIcon() {
		currentButtons.add(editButton);
		editButton.setVisibility(View.VISIBLE);
	}
	public void addMenuIcon() {
		currentButtons.add(menuButton);
		menuButton.setVisibility(View.VISIBLE);
	}
	public void addAddIcon() {
		currentButtons.add(addButton);
		addButton.setVisibility(View.VISIBLE);
	}
	public void addIndexIcon(boolean add) {
		if (add) {
			currentButtons.add(indexButton);
			indexButton.setVisibility(View.VISIBLE);
		}
		else {
			currentButtons.remove(indexButton);
			indexButton.setVisibility(View.GONE);
		}
	}
	public void addDeleteIcon() {
		currentButtons.add(deleteButton);
		deleteButton.setVisibility(View.VISIBLE);
	}
	public void addOkIcon() {
		currentButtons.add(okButton);
		okButton.setVisibility(View.VISIBLE);
	}
	public void addSaveIcon() {
		currentButtons.add(saveButton);
		saveButton.setVisibility(View.VISIBLE);
	}
	public void addColorIcon() {
		currentButtons.add(colorButton);
		colorButton.setVisibility(View.VISIBLE);
	}
	public void addTitleFilter() {
		currentButtons.add(levelSpinner);
		currentButtons.add(courtSpinner);
		levelSpinner.setVisibility(View.VISIBLE);
		levelSpinner.setSelection(0);
		courtSpinner.setVisibility(View.VISIBLE);
		courtSpinner.setSelection(0);
	}

	public void addTitleIcon(int resId) {
		titleIcon.setImageResource(resId);
		currentButtons.add(titleIcon);
		titleIcon.setVisibility(View.VISIBLE);
	}
	public void addSearchIcon() {
		currentButtons.add(searchButton);
		currentButtons.remove(closeButton);
		searchButton.setVisibility(View.VISIBLE);
		searchLayout.setVisibility(View.GONE);
	}
	public void addSearchLayout() {
		currentButtons.add(closeButton);
		currentButtons.remove(searchButton);

		Animation animation = AnimationUtils.loadAnimation(context, R.anim.appear);
		searchLayout.startAnimation(animation);
		searchLayout.setVisibility(View.VISIBLE);

		animation = AnimationUtils.loadAnimation(context, R.anim.disappear);
		searchButton.startAnimation(animation);
		searchButton.setVisibility(View.GONE);
	}
	public void resetSearchLayout() {
		searchEdit.setText("");
		currentButtons.remove(closeButton);
		searchLayout.setVisibility(View.GONE);
		searchButton.setVisibility(View.VISIBLE);
	}

	public void closeSearch() {

		Animation animation = AnimationUtils.loadAnimation(context, R.anim.disappear);
		searchLayout.startAnimation(animation);
		searchLayout.setVisibility(View.GONE);

		animation = AnimationUtils.loadAnimation(context, R.anim.appear);
		searchButton.startAnimation(animation);
		addSearchIcon();
	}

	public void setTitle(String text) {
		titleView.setText(text);
	}
	public String getTitle() {
		return titleView.getText().toString();
	}

	public void actionEditMode() {

		setEditMode(true);
		actionBarListener.onIconClick(editButton);
	}

	@Override
	public void onClick(View view) {
		if (view == backButton) {
			actionBarListener.onBack();
		}
		else if (view == addButton) {
			actionBarListener.onIconClick(addButton);
		}
		else if (view == indexButton) {
			actionBarListener.onIconClick(indexButton);
		}
		else if (view == deleteButton) {
			actionBarListener.onDelete();
		}
		else if (view == editButton) {
			actionEditMode();
		}
		else if (view == menuButton) {
			if (menuWindow == null) {

				createMenu();
			}
			else {
				ListAdapter adapter = actionBarListener.onPrepareMenu();
				if (adapter != null) {
					menuWindow.setAdapter(adapter);
					if (menuWindow.getListView() != null) {
						menuWindow.getListView().invalidate();
					}
				}
			}

			if (menuWindow != null) {
				menuWindow.show();
			}
		}
		else if (view == cancelButton) {
			setEditMode(false);
			actionBarListener.onCancel();
		}
		else if (view == okButton) {
			setEditMode(false);
			actionBarListener.onOk();
		}
		else if (view == saveButton) {
			actionBarListener.onIconClick(saveButton);
		}
		else if (view == colorButton) {
			actionBarListener.onIconClick(colorButton);
		}
		else if (view == searchButton) {
			addSearchLayout();
		}
		else if (view == closeButton) {
			closeSearch();
			actionBarListener.onIconClick(closeButton);
		}
	}

	private void setEditMode(boolean b) {
		if (b) {
			okButton.setVisibility(View.VISIBLE);
			cancelButton.setVisibility(View.VISIBLE);
			for (View v:currentButtons) {
				v.setVisibility(View.GONE);
			}
		}
		else {
			okButton.setVisibility(View.GONE);
			cancelButton.setVisibility(View.GONE);
			for (View v:currentButtons) {
				v.setVisibility(View.VISIBLE);
			}
		}
	}

	private void createMenu() {
		ListAdapter adapter = actionBarListener.createMenu();
		if (adapter != null) {
			menuWindow = new ListPopupWindow(context);
			menuWindow.setAdapter(adapter);
			menuWindow.setAnchorView(menuButton);
			int width = context.getResources().getDimensionPixelSize(R.dimen.actionbar_menu_width);
			int iconWidth = context.getResources().getDimensionPixelSize(R.dimen.actionbar_icon_width);
			int offset = iconWidth - width;//in sliding menu mode, use this to control not show on menu view
			menuWindow.setWidth(width);
			menuWindow.setHorizontalOffset(offset);
			if (adapter.getCount() > 6) {
				menuWindow.setHeight(context.getResources().getDimensionPixelSize(R.dimen.mainview_menu_height));
			}
			menuWindow.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View view, int position,
										long arg3) {
					actionBarListener.onMenuSelected(position);
					menuWindow.dismiss();
				}
			});
		}
	}

	public boolean isHidden() {
		return layout.getVisibility() == View.GONE ? true:false;
	}

	public boolean isShowing() {
		return layout.getVisibility() == View.VISIBLE ? true:false;
	}

	public void hide() {
		layout.setVisibility(View.GONE);
	}

	public void show() {
		layout.setVisibility(View.VISIBLE);
	}
	public boolean dismissMenu() {
		if (menuWindow != null && menuWindow.isShowing()) {
			menuWindow.dismiss();
			return true;
		}
		return false;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
								  int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		actionBarListener.onTextChanged(s.toString(), start, before, count);
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	public void setBackgroundColor(int color) {
		layout.setBackgroundColor(color);
	}
}
