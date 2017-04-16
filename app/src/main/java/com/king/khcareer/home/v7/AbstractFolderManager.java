package com.king.khcareer.home.v7;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.View;
import android.view.View.OnClickListener;

import com.king.lib.tool.ui.RippleFactory;
import com.king.khcareer.base.KApplication;
import com.king.mytennis.view.R;
import com.king.khcareer.pubview.FoldableLayout;
import com.king.khcareer.pubview.FoldableLayout.FoldListener;

/**
 * @author JingYang
 * @version create time：2016-3-7 下午4:45:11
 *
 */
public abstract class AbstractFolderManager implements FoldListener, OnClickListener, FolderManagerAction {

	protected Context mContext;
	protected FoldableLayout mFoldableLayout;

	@Override
	public void initFolderView(Context context, FoldableLayout layout) {
		mContext = context;
		mFoldableLayout = layout;

		layout.setFoldListener(this);
		layout.setOnClickListener(this);

		setupViews();

		/**
		 * 初始化为折叠状态
		 */
		layout.foldWithoutAnimation();
	}

	/**
	 * 初始化折叠布局的封面和详细布局
	 */
	protected abstract void setupViews();

	protected Context getContext() {
		return mContext;
	}
	
	/*
	protected Drawable getType1Drawable() {
		return getTypButtonBackground(mContext.getResources().getColor(R.color.actionbar_bk_blue));
	}

	protected Drawable getType2Drawable() {
		return getTypButtonBackground(mContext.getResources().getColor(R.color.actionbar_bk_green));
	}

	protected Drawable getType3Drawable() {
		return getTypButtonBackground(mContext.getResources().getColor(R.color.actionbar_bk_orange));
	}

	protected Drawable getType4Drawable() {
		return getTypButtonBackground(mContext.getResources().getColor(R.color.actionbar_bk_purple));
	}
	*/

	protected Drawable getTypButtonBackground(int color) {
		GradientDrawable drawable = new GradientDrawable();
		drawable.setColor(color);
		drawable.setAlpha(0xaa);
		drawable.setShape(GradientDrawable.OVAL);
		return drawable;
	}

	protected Drawable getTypButtonRipple() {
		if (KApplication.isLollipop()) {
			return RippleFactory.getBorderlessRippleBackground(
					mContext.getResources().getColor(R.color.view7_folder_detail_btn_ripple));
		}
		return null;
	}

	@Override
	public void onUnFoldStart(FoldableLayout layout) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			layout.setElevation(5);
		}
	}

	@Override
	public void onUnFoldEnd(FoldableLayout layout) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			layout.setElevation(0);
		}
	}

	@Override
	public void onFoldStart(FoldableLayout layout) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			layout.setElevation(5);
		}
	}

	@Override
	public void onFoldEnd(FoldableLayout layout) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			layout.setElevation(0);
		}
	}

	@Override
	public void onClick(View v) {
		if (v instanceof FoldableLayout) {
			FoldableLayout layout = (FoldableLayout) v;
			if (layout.isFolded()) {
				layout.unfoldWithAnimation();
			} else {
				layout.foldWithAnimation();
			}
		}
	}
}
