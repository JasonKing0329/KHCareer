package com.king.mytennis.glory;

import java.util.ArrayList;
import java.util.List;

import com.king.mytennis.glory.ActionBar.ActionBarListener;
import com.king.mytennis.model.MySQLHelper;
import com.king.mytennis.service.Application;
import com.king.mytennis.service.LanguageService;
import com.king.mytennis.view.BaseActivity;
import com.king.mytennis.view.R;
import com.king.lib.colorpicker.ColorPicker;
import com.king.lib.colorpicker.ColorPickerSelectionData;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;

public class GloryModuleActivity extends BaseActivity
		implements SPictureChooseListener, ActionBarListener {

	private HorizontalChooser chooser;
	private ImageView lastChosenItem;
	private List<String> gloryTitleList;
	private AchieveParentView achieveParentView;
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		LanguageService.init(this);
		if (Application.isLollipop()) {
			setContentView(R.layout.glory_activity_l);
		}
		else {
			setContentView(R.layout.glory_activity);
		}

		gloryTitleList = new ArrayList<String>();
		for (String name:GloryIndex.FAME_INDEX) {
			gloryTitleList.add(name);
		}
		chooser = new HorizontalChooser(this, gloryTitleList);
		chooser.setOnChooseListener(this);

		achieveParentView = new AchieveParentView(this);

		actionBar = new ActionBar(this, this);
		actionBar.setTitle(GloryIndex.FAME_TITLE);
		actionBar.clearActionIcon();
		bindActionBarForTitleView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.glory_module, menu);
		return true;
	}

	@Override
	public void onChoose(View view, int position) {

		if (lastChosenItem != null) {
			//lastChosenItem.setImageBitmap(null);
			lastChosenItem.setImageResource(R.drawable.spicture_chooser_border_normal);//choose frame as front, image as background
		}
		lastChosenItem = (ImageView) view;
		lastChosenItem.setImageResource(R.drawable.spicture_chooser_border_choose);//choose frame as front, image as background

		actionBar.setTitle(gloryTitleList.get(position));
		actionBar.clearActionIcon();
		switch (position) {
			case GloryIndex.FAME_RANK_INDEX:
				actionBar.addEditIcon();
				actionBar.addMenuIcon();
				break;
			case GloryIndex.FAME_TITLE_INDEX:
				bindActionBarForTitleView();
				break;
			case GloryIndex.FAME_GRANDSLAM_INDEX:
				actionBar.addAddIcon();
				actionBar.addEditIcon();
				actionBar.addMenuIcon();
				break;
			case GloryIndex.FAME_PRIZE_INDEX:
				break;
			case GloryIndex.FAME_RUNNERUP_INDEX:
				bindActionBarForTitleView();
				break;

			default:
				break;
		}
		achieveParentView.setView(position);
	}

	private void bindActionBarForTitleView() {
		actionBar.addTitleFilter();
		actionBar.addColorIcon();
		actionBar.addActionSpinnerListener(new ActionBar.ActionSpinnerListener() {

			@Override
			public void onTitleFilterListener(int indexLevel, int indexCourt) {
				achieveParentView.filtTitle(indexLevel, indexCourt);
			}

		});
	}

	@Override
	protected void onDestroy() {
		MySQLHelper.closeHelper();
		super.onDestroy();
	}

	@Override
	public void onLongTouch(View view, int position) {
		onChoose(view, position);
	}

	@Override
	public void onBack() {
		finish();
	}

	@Override
	public void onOk() {
		if (actionBar.getTitle().equals(GloryIndex.FAME_RANK)) {
			achieveParentView.saveRank();
		}
		else if (actionBar.getTitle().equals(GloryIndex.FAME_GRANDSLAM)) {
			achieveParentView.saveGS();
		}
	}

	@Override
	public void onCancel() {
		if (actionBar.getTitle().equals(GloryIndex.FAME_RANK)) {
			achieveParentView.cancelRank();
		}
		else if (actionBar.getTitle().equals(GloryIndex.FAME_GRANDSLAM)) {
			achieveParentView.cancelEditGS();
		}
	}

	@Override
	public ListAdapter createMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListAdapter onPrepareMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onMenuSelected(int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(String text, int start, int before, int count) {

	}

	@Override
	public void onDelete() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onIconClick(View view) {
		if (view.getId() == R.id.actionbar_add) {
			if (actionBar.getTitle().equals(GloryIndex.FAME_GRANDSLAM)) {
				achieveParentView.addGs();
				actionBar.actionEditMode();
			}

		}
		else if (view.getId() == R.id.actionbar_edit) {
			if (actionBar.getTitle().equals(GloryIndex.FAME_RANK)) {
				achieveParentView.enableEditRank();
			}
			else if (actionBar.getTitle().equals(GloryIndex.FAME_GRANDSLAM)) {
				achieveParentView.enableEditGS();
			}
		}
		else if (view.getId() == R.id.actionbar_color) {
			new ColorPicker(this, new ColorPicker.OnColorPickerListener() {

				@Override
				public void onColorChanged(String key, int newColor) {
					actionBar.setBackgroundColor(newColor);
				}

				@Override
				public void onColorSelected(int color) {
					actionBar.setBackgroundColor(color);
				}

				@Override
				public void onColorSelected(List<ColorPickerSelectionData> list) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onColorCancleSelect() {
					// TODO Auto-generated method stub

				}

				@Override
				public void onApplyDefaultColors() {
					// TODO Auto-generated method stub

				}
			}).show();
		}
	}

}
