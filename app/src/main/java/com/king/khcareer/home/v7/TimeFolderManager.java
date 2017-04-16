package com.king.khcareer.home.v7;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.lib.tool.ui.RippleFactory;
import com.king.khcareer.base.KApplication;
import com.king.khcareer.home.v6.ManagerActivity;
import com.king.mytennis.view.R;
import com.king.khcareer.settings.SettingProperty;
import com.king.khcareer.home.classic.ClassicActivity;

/**
 * @author JingYang
 * @version create time：2016-3-7 下午4:41:11
 *
 */
public class TimeFolderManager extends AbstractFolderManager implements OnLongClickListener {

	private final String VALUE_QUICKENTER_CLASSIC = "time_value_classic";
	private final String VALUE_QUICKENTER_CARD_VER = "time_value_cardver";

	private ImageView timeCoverImageView, timeDetailImageView;
	private TextView timeTextView;

	private TextView mClassicBtn, mCardVerBtn/*, mCardHorBtn, mTimelineBtn*/;
	private TextView quickEnterView;
	private View mDefaultBtn;

	@Override
	protected void setupViews() {
		ViewGroup timeCoverGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.list_item_cover, null);
		ViewGroup timeDetailGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.list_item_detail, null);
		timeCoverImageView = (ImageView) timeCoverGroup.findViewById(R.id.folder_image_cover);
		timeTextView = (TextView) timeCoverGroup.findViewById(R.id.folder_text_cover);
		timeDetailImageView = (ImageView) timeDetailGroup.findViewById(R.id.folder_image_detail);
		mFoldableLayout.setupViews(timeCoverGroup, timeDetailGroup
				, getContext().getResources().getDimensionPixelSize(R.dimen.folder_item_height));

		quickEnterView = (TextView) timeCoverGroup.findViewById(R.id.folder_quick_enter);
		quickEnterView.setOnClickListener(this);

		timeCoverImageView.setImageResource(R.drawable.view7_folder_cover_time);
		timeDetailImageView.setImageResource(R.drawable.view7_folder_detail_time);
		timeTextView.setText(R.string.view7_title_timeline);
		timeTextView.setOnClickListener(this);

		mClassicBtn = (TextView) timeDetailGroup.findViewById(R.id.folder_detail_classic);
		mCardVerBtn = (TextView) timeDetailGroup.findViewById(R.id.folder_detail_card_ver);
		mClassicBtn.setOnClickListener(this);
		mCardVerBtn.setOnClickListener(this);
		mClassicBtn.setOnLongClickListener(this);
		mCardVerBtn.setOnLongClickListener(this);
		String mode = SettingProperty.getQuickEnterTimeMode(mContext, VALUE_QUICKENTER_CLASSIC);
		if (mode.equals(VALUE_QUICKENTER_CARD_VER)) {
			String defStr = getContext().getString(R.string.view7_folder_default);
			mCardVerBtn.setText(getContext().getString(R.string.view7_folder_card_ver)
					+ "\n" + defStr);
			mDefaultBtn = mCardVerBtn;
		}
		else {
			String defStr = getContext().getString(R.string.view7_folder_default);
			mClassicBtn.setText(getContext().getString(R.string.view7_folder_classic)
					+ "\n" + defStr);
			mDefaultBtn = mClassicBtn;
		}

		if (KApplication.isLollipop()) {
			quickEnterView.setBackground(RippleFactory.getRippleBackground(
					mContext.getResources().getColor(R.color.view7_quick_enter_bk)
					, mContext.getResources().getColor(R.color.ripple_material_light)));
			timeTextView.setBackground(RippleFactory.getBorderlessRippleBackground(Color.rgb(0xbb, 0xbb, 0xbb)));
			timeDetailGroup.findViewById(R.id.folder_detail_classic_layout)
					.setBackground(getTypButtonBackground(Color.rgb(247, 68, 97)));
			timeDetailGroup.findViewById(R.id.folder_detail_card_ver_layout)
					.setBackground(getTypButtonBackground(Color.rgb(173, 195, 193)));
			mClassicBtn.setBackground(getTypButtonRipple());
			mCardVerBtn.setBackground(getTypButtonRipple());
		}

		//切划卡片和时间线不做
		timeDetailGroup.findViewById(R.id.folder_detail_card_hor_layout).setVisibility(View.GONE);
//			.setBackground(getTypButtonBackground(Color.rgb(87, 96, 105)));
		timeDetailGroup.findViewById(R.id.folder_detail_timeline_layout).setVisibility(View.GONE);
//			.setBackground(getTypButtonBackground(Color.rgb(0, 49, 79)));
//		mCardHorBtn = (TextView) timeDetailGroup.findViewById(R.id.folder_detail_card_hor);
//		mTimelineBtn = (TextView) timeDetailGroup.findViewById(R.id.folder_detail_timeline);
//		mCardHorBtn.setOnClickListener(this);
//		mTimelineBtn.setOnClickListener(this);
//		mCardHorBtn.setBackground(getTypButtonRipple());
//		mTimelineBtn.setBackground(getTypButtonRipple());
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		switch (v.getId()) {
			case R.id.folder_detail_classic:
				startClassicActivity();
				break;
			case R.id.folder_detail_card_hor:

				break;
			case R.id.folder_detail_card_ver:
				startCardVerActivity();
				break;
			case R.id.folder_detail_timeline:

				break;
			case R.id.folder_quick_enter:
				String mode = SettingProperty.getQuickEnterTimeMode(mContext, VALUE_QUICKENTER_CLASSIC);
				if (mode.equals(VALUE_QUICKENTER_CARD_VER)) {
					startCardVerActivity();
				}
				else {
					startClassicActivity();
				}
				break;

			default:
				break;
		}
	}

	private void startClassicActivity() {
		((Activity) getContext()).startActivity(new Intent().setClass(getContext(), ClassicActivity.class));
	}

	private void startCardVerActivity() {
		Intent intent = new Intent().setClass(getContext(), ManagerActivity.class);
		intent.putExtra(ManagerActivity.KEY_START_FROM_V7, true);
		((Activity) getContext()).startActivity(intent);
	}

	@Override
	public boolean onLongClick(View v) {
		switch (v.getId()) {
			case R.id.folder_detail_classic:
				if (mDefaultBtn != mClassicBtn) {
					mCardVerBtn.setText(getContext().getString(R.string.view7_folder_card_ver));

					String defStr = getContext().getString(R.string.view7_folder_default);
					mClassicBtn.setText(getContext().getString(R.string.view7_folder_classic)
							+ "\n" + defStr);
					SettingProperty.setQuickEnterTimeMode(getContext(), VALUE_QUICKENTER_CLASSIC);
				}
				break;
			case R.id.folder_detail_card_ver:
				if (mDefaultBtn != mCardVerBtn) {
					mClassicBtn.setText(getContext().getString(R.string.view7_folder_classic));

					String defStr = getContext().getString(R.string.view7_folder_default);
					mCardVerBtn.setText(getContext().getString(R.string.view7_folder_card_ver)
							+ "\n" + defStr);
					SettingProperty.setQuickEnterTimeMode(getContext(), VALUE_QUICKENTER_CARD_VER);
				}
				break;
		}
		mDefaultBtn = v;
		return true;
	}
}
