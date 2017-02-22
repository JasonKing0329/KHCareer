package com.king.mytennis.view_v_7_0.view;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.king.lib.tool.ui.RippleFactory;
import com.king.mytennis.glory.GloryModuleActivity;
import com.king.mytennis.match.MatchManageActivity;
import com.king.mytennis.model.Configuration;
import com.king.mytennis.model.FileIO;
import com.king.mytennis.model.ImageFactory;
import com.king.mytennis.score.ScoreActivity;
import com.king.mytennis.service.Application;
import com.king.mytennis.view.ChooseBkDialog;
import com.king.mytennis.view.CustomDialog;
import com.king.mytennis.view.H2hMainActivity;
import com.king.mytennis.view.R;
import com.king.mytennis.view.RankActivity;
import com.king.mytennis.view.settings.SettingActivity;
import com.king.mytennis.view_v_7_0.V7MainActivity;

import static android.R.attr.start;

/**
 * @author JingYang
 * @version create time：2016-3-8 下午3:38:05
 *
 */
public class MoreFolderManager extends AbstractFolderManager {

	private ImageView moreCoverImageView, settingImageView;
	private TextView moreTextView;

	private LinearLayout gloryButton;
	private TextView scoreButton, matchButton, bkButton;

	@SuppressLint("InflateParams")
	@Override
	protected void setupViews() {
		ViewGroup moreCoverGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.view7_folder_more_cover, null);
		ViewGroup moreDetailGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.view7_folder_more_detail, null);

		moreCoverImageView = (ImageView) moreCoverGroup.findViewById(R.id.folder_image_cover);
		moreTextView = (TextView) moreCoverGroup.findViewById(R.id.folder_text_cover);
		settingImageView = (ImageView) moreCoverGroup.findViewById(R.id.folder_more_setting);
		gloryButton = (LinearLayout) moreDetailGroup.findViewById(R.id.view7_more_glory);
		matchButton = (TextView) moreDetailGroup.findViewById(R.id.view7_more_match);
		scoreButton = (TextView) moreDetailGroup.findViewById(R.id.view7_more_score);
		bkButton = (TextView) moreDetailGroup.findViewById(R.id.view7_more_bk);
		mFoldableLayout.setupViews(moreCoverGroup, moreDetailGroup
				, getContext().getResources().getDimensionPixelSize(R.dimen.folder_item_height));

		moreCoverImageView.setImageResource(R.drawable.view7_folder_cover_more);
//		matchDetailImageView.setImageResource(R.drawable.test_view7_folder_bg1);
		moreTextView.setText(R.string.view7_title_more);

		moreTextView.setOnClickListener(this);
		settingImageView.setOnClickListener(this);
		gloryButton.setOnClickListener(this);
		matchButton.setOnClickListener(this);
		scoreButton.setOnClickListener(this);
		bkButton.setOnClickListener(this);

		if (Application.isLollipop()) {
			moreTextView.setBackground(RippleFactory.getBorderlessRippleBackground(Color.rgb(0xbb, 0xbb, 0xbb)));
			settingImageView.setBackground(RippleFactory.getBorderlessRippleBackground(Color.rgb(0xbb, 0xbb, 0xbb)));
			gloryButton.setBackground(RippleFactory.getRippleBackground(
					Color.rgb(0xf7, 0x44, 0x61),
					mContext.getResources().getColor(R.color.ripple_material_light)));
			matchButton.setBackground(RippleFactory.getRippleBackground(
					Color.rgb(0x57, 0x60, 0x69),
					mContext.getResources().getColor(R.color.ripple_material_light)));
			scoreButton.setBackground(RippleFactory.getRippleBackground(
					Color.rgb(0xad, 0xc3, 0xc0),
					mContext.getResources().getColor(R.color.ripple_material_light)));
			bkButton.setBackground(RippleFactory.getRippleBackground(
					Color.rgb(0xaa, 0xaa, 0xaa),
					mContext.getResources().getColor(R.color.ripple_material_light)));
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		switch (v.getId()) {
			case R.id.folder_more_setting:
				startSettingActivity();
				break;
			case R.id.view7_more_glory:
				startGloryActivity();
				break;
			case R.id.view7_more_match:
				startMatchManageActivity();
				break;
			case R.id.view7_more_bk:
				chooseBackground();
				break;
			case R.id.view7_more_score:
				startScoreActivity();
				break;

			default:
				break;
		}
	}

	private void chooseBackground() {
		ChooseBkDialog bkDialog = new ChooseBkDialog(mContext, new CustomDialog.OnCustomDialogActionListener() {

			@Override
			public boolean onSave(Object object) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> map = (HashMap<String, Object>) object;
				int kind = (Integer) map.get(ChooseBkDialog.BK_KIND_KEY);
				if (kind == ChooseBkDialog.BK_KIND_MAINVIEW) {
					String path = (String) map.get("path");
					Bitmap bitmap = new ImageFactory().getBackground(path);
					notifyBackgroundChanged(bitmap, path);
				}
				return true;
			}

			@Override
			public void onLoadData(HashMap<String, Object> data) {

			}

			@Override
			public boolean onCancel() {
				return false;
			}
		});
		bkDialog.show();
	}

	/**
	 * @param bitmap
	 * @param newDEF_BK
	 */
	private void notifyBackgroundChanged(Bitmap bitmap, String newDEF_BK) {
		((V7MainActivity) mContext).updateSideBackground(bitmap);
		Configuration.getInstance().DEF_BK = newDEF_BK;
		FileIO dao = new FileIO();
		dao.saveConfigInfor(Configuration.getInstance());
	}

	@Deprecated
	private void startH2hActivity() {
		((Activity) getContext()).startActivity(new Intent().setClass(getContext(), H2hMainActivity.class));
	}

	@Deprecated
	private void startRankActivity() {
		((Activity) getContext()).startActivity(new Intent().setClass(getContext(), RankActivity.class));
	}

	private void startGloryActivity() {
		((Activity) getContext()).startActivity(new Intent().setClass(getContext(), GloryModuleActivity.class));
	}

	private void startSettingActivity() {
		((Activity) getContext()).startActivity(new Intent().setClass(getContext(), SettingActivity.class));
	}

	private void startMatchManageActivity() {
		((Activity) getContext()).startActivity(new Intent().setClass(getContext(), MatchManageActivity.class));
	}

	private void startScoreActivity() {
		((Activity) getContext()).startActivity(new Intent().setClass(getContext(), ScoreActivity.class));
	}

}
