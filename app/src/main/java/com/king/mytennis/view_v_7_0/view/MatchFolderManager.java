package com.king.mytennis.view_v_7_0.view;

import com.king.lib.tool.ui.RippleFactory;
import com.king.mytennis.service.Application;
import com.king.mytennis.update_v_6_0.ManagerActivity;
import com.king.mytennis.view.R;
import com.king.mytennis.view.settings.SettingProperty;
import com.king.mytennis.view_v_7_0.ClassicActivity;
import com.king.mytennis.view_v_7_0.swipecard.SwipeCardActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author JingYang
 * @version create time：2016-3-8 下午3:16:20
 *
 */
public class MatchFolderManager extends AbstractFolderManager implements OnLongClickListener {

	private final String VALUE_QUICKENTER_CLASSIC = "match_value_classic";
	private final String VALUE_QUICKENTER_CARD_HOR = "match_value_cardhor";
	private final String VALUE_QUICKENTER_CARD_VER = "match_value_cardver";

	private ImageView matchCoverImageView, matchDetailImageView;
	private TextView matchTextView;

	private TextView mCardHorBtn, mCardVerBtn, mClassicBtn;
	private TextView quickEnterView;
	private View mDefaultBtn;

	@Override
	protected void setupViews() {
		ViewGroup matchCoverGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.list_item_cover, null);
		ViewGroup matchDetailGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.list_item_detail, null);

		matchCoverImageView = (ImageView) matchCoverGroup.findViewById(R.id.folder_image_cover);
		matchTextView = (TextView) matchCoverGroup.findViewById(R.id.folder_text_cover);
		matchDetailImageView = (ImageView) matchDetailGroup.findViewById(R.id.folder_image_detail);
		mFoldableLayout.setupViews(matchCoverGroup, matchDetailGroup
				, getContext().getResources().getDimensionPixelSize(R.dimen.folder_item_height));

		quickEnterView = (TextView) matchCoverGroup.findViewById(R.id.folder_quick_enter);
		quickEnterView.setOnClickListener(this);

		matchCoverImageView.setImageResource(R.drawable.view7_folder_cover_match);
		matchDetailImageView.setImageResource(R.drawable.view7_folder_detail_match);
		matchTextView.setText(R.string.view7_title_match);

		matchTextView.setOnClickListener(this);

		mCardHorBtn = (TextView) matchDetailGroup.findViewById(R.id.folder_detail_card_hor);
		mCardVerBtn = (TextView) matchDetailGroup.findViewById(R.id.folder_detail_card_ver);
		mClassicBtn = (TextView) matchDetailGroup.findViewById(R.id.folder_detail_timeline);
		mCardHorBtn.setOnClickListener(this);
		mCardVerBtn.setOnClickListener(this);
		mClassicBtn.setOnClickListener(this);
		mClassicBtn.setOnLongClickListener(this);
		mCardVerBtn.setOnLongClickListener(this);
		mCardHorBtn.setOnLongClickListener(this);

		String defStr = getContext().getString(R.string.view7_folder_default);
		String mode = SettingProperty.getQuickEnterMatchMode(mContext, VALUE_QUICKENTER_CLASSIC);
		if (mode.equals(VALUE_QUICKENTER_CARD_VER)) {
			mCardVerBtn.setText(getContext().getString(R.string.view7_folder_card_ver)
					+ "\n" + defStr);
			mDefaultBtn = mCardVerBtn;
		}
		else if (mode.equals(VALUE_QUICKENTER_CARD_HOR)) {
			mCardHorBtn.setText(getContext().getString(R.string.view7_folder_card_hor)
					+ "\n" + defStr);
			mDefaultBtn = mCardHorBtn;
		}
		else {
			mClassicBtn.setText(getContext().getString(R.string.view7_folder_classic)
					+ "\n" + defStr);
			mDefaultBtn = mClassicBtn;
		}

		if (Application.isLollipop()) {
			quickEnterView.setBackground(RippleFactory.getRippleBackground(
					mContext.getResources().getColor(R.color.view7_quick_enter_bk)
					, mContext.getResources().getColor(R.color.ripple_material_light)));
			matchTextView.setBackground(RippleFactory.getBorderlessRippleBackground(Color.rgb(0xbb, 0xbb, 0xbb)));
			matchDetailGroup.findViewById(R.id.folder_detail_card_hor_layout)
					.setBackground(getTypButtonBackground(Color.rgb(85, 170, 173)));
			matchDetailGroup.findViewById(R.id.folder_detail_card_ver_layout)
					.setBackground(getTypButtonBackground(Color.rgb(229, 190, 157)));
			matchDetailGroup.findViewById(R.id.folder_detail_timeline_layout)
					.setBackground(getTypButtonBackground(Color.rgb(148, 41, 35)));
			mCardHorBtn.setBackground(getTypButtonRipple());
			mCardVerBtn.setBackground(getTypButtonRipple());
			mClassicBtn.setBackground(getTypButtonRipple());
		}

		matchDetailGroup.findViewById(R.id.folder_detail_classic_layout).setVisibility(View.GONE);
//		.setBackground(getTypButtonBackground(Color.rgb(35, 31, 32)));
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		if (v == mCardHorBtn) {
			startSwipeCardActivity();
		}
		else if (v == mCardVerBtn) {
			startCardVerActivity();
		}
		else if (v == mClassicBtn) {
			startClassicActivity();
		}
		else if (v == quickEnterView) {
			String mode = SettingProperty.getQuickEnterMatchMode(mContext, VALUE_QUICKENTER_CLASSIC);
			if (mode.equals(VALUE_QUICKENTER_CARD_VER)) {
				startCardVerActivity();
			}
			else if (mode.equals(VALUE_QUICKENTER_CARD_HOR)) {
				startSwipeCardActivity();
			}
			else {
				startClassicActivity();
			}
		}
	}

	private void startClassicActivity() {
		((Activity) getContext()).startActivity(new Intent().setClass(getContext(), ClassicActivity.class));
	}

	private void startSwipeCardActivity() {
		Intent intent = new Intent().setClass(getContext(), SwipeCardActivity.class);
		intent.putExtra(SwipeCardActivity.KEY_INIT_MODE, SwipeCardActivity.INIT_MATCH);
		((Activity) getContext()).startActivity(intent);
	}

	private void startCardVerActivity() {
		Intent intent = new Intent().setClass(getContext(), ManagerActivity.class);
		intent.putExtra(ManagerActivity.KEY_INIT_MODE, ManagerActivity.INIT_MATCH);
		intent.putExtra(ManagerActivity.KEY_START_FROM_V7, true);
		((Activity) getContext()).startActivity(intent);
	}

	@Override
	public boolean onLongClick(View v) {
		if (v == mClassicBtn) {
			if (mDefaultBtn != mClassicBtn) {
				mCardVerBtn.setText(getContext().getString(R.string.view7_folder_card_ver));
				mCardHorBtn.setText(getContext().getString(R.string.view7_folder_card_hor));

				String defStr = getContext().getString(R.string.view7_folder_default);
				mClassicBtn.setText(getContext().getString(R.string.view7_folder_classic)
						+ "\n" + defStr);
				SettingProperty.setQuickEnterMatchMode(getContext(), VALUE_QUICKENTER_CLASSIC);
			}
		}
		else if (v == mCardHorBtn) {
			if (mDefaultBtn != mCardHorBtn) {
				mClassicBtn.setText(getContext().getString(R.string.view7_folder_classic));
				mCardVerBtn.setText(getContext().getString(R.string.view7_folder_card_ver));

				String defStr = getContext().getString(R.string.view7_folder_default);
				mCardHorBtn.setText(getContext().getString(R.string.view7_folder_card_hor)
						+ "\n" + defStr);
				SettingProperty.setQuickEnterMatchMode(getContext(), VALUE_QUICKENTER_CARD_HOR);
			}
		}
		else if (v == mCardVerBtn) {
			if (mDefaultBtn != mCardVerBtn) {
				mClassicBtn.setText(getContext().getString(R.string.view7_folder_classic));
				mCardHorBtn.setText(getContext().getString(R.string.view7_folder_card_hor));

				String defStr = getContext().getString(R.string.view7_folder_default);
				mCardVerBtn.setText(getContext().getString(R.string.view7_folder_card_ver)
						+ "\n" + defStr);
				SettingProperty.setQuickEnterMatchMode(getContext(), VALUE_QUICKENTER_CARD_VER);
			}
		}
		mDefaultBtn = v;
		return true;
	}

}
