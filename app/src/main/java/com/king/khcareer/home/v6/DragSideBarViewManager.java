package com.king.khcareer.home.v6;

import java.util.List;

import com.king.lib.colorpicker.ColorPicker;
import com.king.lib.colorpicker.ColorPicker.OnColorPickerListener;
import com.king.lib.colorpicker.ColorPickerSelectionData;
import com.king.lib.tool.ui.RippleFactory;
import com.king.lib.tool.ui.RippleParseException;
import com.king.khcareer.common.multiuser.MultiUser;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.common.multiuser.MultiUserSelector;
import com.king.khcareer.common.res.AppResProvider;
import com.king.khcareer.common.res.ColorRes;
import com.king.khcareer.common.res.ColorResManager;
import com.king.khcareer.common.res.JResource;
import com.king.khcareer.utils.UIProvider;
import com.king.mytennis.view.R;
import com.king.khcareer.pubview.DragSideBar;
import com.king.khcareer.pubview.DragSideBar.DragSideBarListener;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DragSideBarViewManager implements OnClickListener, DragSideBarListener
		, OnColorPickerListener{

	public interface SideListener {
		public int MENU_SAVE = 0;
		public int MENU_LOAD = 1;
		public int MENU_SAVEAS = 2;
		public int MENU_SAVECONF = 3;
		public int MENU_CHANGEBK = 4;
		public int MENU_CHANGETHEME = 5;
		public int MENU_EXIT = 6;

		public void onMatchCard();
		public void onYearCard();
		public void onPlayerCard();
		public void onChangeUser(MultiUser user);
		public void onInsert();
		public void onGlory();
		public void onSetting();
		public void onRank();
		public void onH2H();
		public void onClassicList();
		public void onMenuItem(int itemId);
	}

	private Context mContext;
	private LinearLayout yearButton, matchButton, playerButton, gloryButton
			, insertButton, settingButton;
	private ImageView menuButton, colorButton;
	private TextView h2hButton, rankButton, listButton;
	private MultiUserSelector multiUserSelector;
	private LinearLayout userLayout;

	private SideListener sideListener;
	private DragSideBar dragSideBar;

	private ListPopupWindow menuWindow;
	private ListAdapter adapter;
	private String[] menuItems;

	private ColorPicker colorPicker;
	private int mRippleColor;
	private int mTopColor, mSec1Color, mSec2Color, mSec3Color, mSec4Color
			, mSec5Color, mSec6Color, mBottomColor;
	private View topLineLayout;

	public DragSideBarViewManager(Context context, DragSideBar dragSideBar) {
		mContext = context;
		this.dragSideBar = dragSideBar;

		Activity view = (Activity) context;
		menuButton = (ImageView) view.findViewById(R.id.dragsidebar_menu);
		colorButton = (ImageView) view.findViewById(R.id.dragsidebar_color);
		yearButton = (LinearLayout) view.findViewById(R.id.cardview_cover_year);
		matchButton = (LinearLayout) view.findViewById(R.id.cardview_cover_match);
		playerButton = (LinearLayout) view.findViewById(R.id.cardview_cover_player);
		gloryButton = (LinearLayout) view.findViewById(R.id.cardview_cover_glory);
		insertButton = (LinearLayout) view.findViewById(R.id.cardview_cover_insert);
		settingButton = (LinearLayout) view.findViewById(R.id.cardview_cover_setting);
		h2hButton = (TextView) view.findViewById(R.id.dragsidebar_h2h);
		rankButton = (TextView) view.findViewById(R.id.dragsidebar_rank);
		listButton = (TextView) view.findViewById(R.id.dragsidebar_list);
		userLayout = (LinearLayout) view.findViewById(R.id.home_menu_user_layout);
		topLineLayout = view.findViewById(R.id.cardview_topline);

		yearButton.setOnClickListener(this);
		matchButton.setOnClickListener(this);
		playerButton.setOnClickListener(this);
		gloryButton.setOnClickListener(this);
		insertButton.setOnClickListener(this);
		settingButton.setOnClickListener(this);
		h2hButton.setOnClickListener(this);
		rankButton.setOnClickListener(this);
		listButton.setOnClickListener(this);
		menuButton.setOnClickListener(this);
		colorButton.setOnClickListener(this);
		colorButton.setBackgroundResource(R.drawable.ripple_white);
		menuButton.setBackgroundResource(R.drawable.ripple_white);
		listButton.setBackgroundResource(R.drawable.ripple_white);

		dragSideBar.setDragSideBarListener(this);
		dragSideBar.setShadowView(view.findViewById(R.id.cardpage_shadow));

		initMultiUserSelector();

//		setDrawableForSec();
		setDrawableForSection();
	}

	private void setDrawableForSection() {
		updateColorValues();
		Drawable ripple = RippleFactory.getRippleBackground(
				mTopColor, mRippleColor);
		userLayout.setBackground(ripple);
		ripple = RippleFactory.getRippleBackground(
				mTopColor, mRippleColor);
		topLineLayout.setBackground(ripple);
		ripple = RippleFactory.getRippleBackground(
				mSec1Color, mRippleColor);
		yearButton.setBackground(ripple);
		ripple = RippleFactory.getRippleBackground(
				mSec2Color, mRippleColor);
		matchButton.setBackground(ripple);
		ripple = RippleFactory.getRippleBackground(
				mSec3Color, mRippleColor);
		playerButton.setBackground(ripple);
		ripple = RippleFactory.getRippleBackground(
				mSec4Color, mRippleColor);
		insertButton.setBackground(ripple);
		ripple = RippleFactory.getRippleBackground(
				mSec5Color, mRippleColor);
		gloryButton.setBackground(ripple);
		ripple = RippleFactory.getRippleBackground(
				mSec6Color, mRippleColor);
		settingButton.setBackground(ripple);
		ripple = RippleFactory.getRippleBackground(
				mBottomColor, mRippleColor);
		h2hButton.setBackground(ripple);
		ripple = RippleFactory.getRippleBackground(
				mBottomColor, mRippleColor);
		rankButton.setBackground(ripple);
	}

	/**
	 * import edit color module, deprecate this style
	 */
	@Deprecated
	private void setDrawableForSec() {
		int[] colorRes = UIProvider.getDragSideBarColors();
		View[] views = new View[]{userLayout, yearButton, matchButton, playerButton,
				insertButton, gloryButton, settingButton};

		int rippleColor = mContext.getResources().getColor(R.color.ripple_material_light);
		for (int i = 0; i < 7; i ++) {
			int color = mContext.getResources().getColor(colorRes[i]);
			Drawable ripple = RippleFactory.getRippleBackground(
					color, rippleColor);
			views[i].setBackground(ripple);
		}

		topLineLayout.setBackgroundColor(mContext.getResources().getColor(colorRes[0]));

		int color = mContext.getResources().getColor(colorRes[7]);
		Drawable ripple = RippleFactory.getRippleBackground(
				color, rippleColor);
		h2hButton.setBackground(ripple);
		ripple = RippleFactory.getRippleBackground(
				color, rippleColor);
		rankButton.setBackground(ripple);
	}

	public void setOnSideListener(SideListener listener) {
		sideListener = listener;
	}

	@Override
	public void onClick(View view) {
		if (view == menuButton) {
			showDropMenu();
		}
		else if (view == colorButton) {
			if (colorPicker == null) {
				colorPicker = new ColorPicker(mContext, this);
				colorPicker.setResourceProvider(new AppResProvider(mContext));
			}
			colorPicker.setSelectionData(new ColorResManager().getDragSideBar(mContext));
			colorPicker.show();
		}
		else {
			if (sideListener != null) {
				if (view == yearButton) {
					sideListener.onYearCard();
				}
				else if (view == matchButton) {
					sideListener.onMatchCard();
				}
				else if (view == playerButton) {
					sideListener.onPlayerCard();
				}
				else if (view == gloryButton) {
					sideListener.onGlory();
				}
				else if (view == h2hButton) {
					sideListener.onH2H();
				}
				else if (view == rankButton) {
					sideListener.onRank();
				}
				else if (view == insertButton) {
					sideListener.onInsert();
				}
				else if (view == settingButton) {
					sideListener.onSetting();
				}
				else if (view == listButton) {
					sideListener.onClassicList();
				}

				dragSideBar.dismiss(true);
			}
		}
	}

	private void showDropMenu() {
		if (menuWindow == null) {
			menuWindow = new ListPopupWindow(mContext);
			menuWindow.setAnchorView(menuButton);
			int width = mContext.getResources().getDimensionPixelSize(R.dimen.actionbar_menu_width);
			int iconWidth = mContext.getResources().getDimensionPixelSize(R.dimen.actionbar_icon_width);
			int offset = iconWidth - width;//in sliding menu mode, use this to control not show on menu view
			menuWindow.setWidth(width);
			menuWindow.setHorizontalOffset(offset);
			menuWindow.setHeight(mContext.getResources().getDimensionPixelSize(R.dimen.mainview_menu_height));
			menuWindow.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View view, int position,
										long arg3) {
					sideListener.onMenuItem(position);
					menuWindow.dismiss();
				}

			});
			menuItems = mContext.getResources().getStringArray(R.array.mainview_menu);
			adapter = new ArrayAdapter<String>(mContext
					, android.R.layout.simple_dropdown_item_1line, menuItems);
			menuWindow.setAdapter(adapter);
		}

		menuWindow.show();
	}

	private void initMultiUserSelector() {
		multiUserSelector = new MultiUserSelector(mContext, new MultiUserSelector.OnUserSelectListener() {

			@Override
			public void onSelect(MultiUser user) {
				if (sideListener != null) {
					sideListener.onChangeUser(user);
				}
			}
		});
		multiUserSelector.setUserLayout(userLayout);
		multiUserSelector.updateUserGuide(userLayout, MultiUserManager.getInstance().getCurrentUser());
	}

	@Override
	public void onCloseDragSideBar() {
//		setDrawableForSec();
	}

	@Override
	public void onColorChanged(String key, int newColor) {
		if (key.equals(ColorRes.DRAGSIDEBAR_LINE_TOP)) {
			mTopColor = newColor;
			updateBkColor(userLayout, newColor, mRippleColor);
			updateBkColor(topLineLayout, newColor, mRippleColor);
		}
		else if (key.equals(ColorRes.DRAGSIDEBAR_SEC1)) {
			mSec1Color = newColor;
			updateBkColor(yearButton, newColor, mRippleColor);
		}
		else if (key.equals(ColorRes.DRAGSIDEBAR_SEC2)) {
			mSec2Color = newColor;
			updateBkColor(matchButton, newColor, mRippleColor);
		}
		else if (key.equals(ColorRes.DRAGSIDEBAR_SEC3)) {
			mSec3Color = newColor;
			updateBkColor(playerButton, newColor, mRippleColor);
		}
		else if (key.equals(ColorRes.DRAGSIDEBAR_SEC4)) {
			mSec4Color = newColor;
			updateBkColor(insertButton, newColor, mRippleColor);
		}
		else if (key.equals(ColorRes.DRAGSIDEBAR_SEC5)) {
			mSec5Color = newColor;
			updateBkColor(gloryButton, newColor, mRippleColor);
		}
		else if (key.equals(ColorRes.DRAGSIDEBAR_SEC6)) {
			mSec6Color = newColor;
			updateBkColor(settingButton, newColor, mRippleColor);
		}
		else if (key.equals(ColorRes.DRAGSIDEBAR_LINE_BOTTOM)) {
			mBottomColor = newColor;
			updateBkColor(h2hButton, newColor, mRippleColor);
			updateBkColor(rankButton, newColor, mRippleColor);
		}
		else if (key.equals(ColorRes.DRAGSIDEBAR_RIPPLE_COLOR)) {
			updateRippleColor(newColor);
		}
	}

	private void updateBkColor(View view, int color, int rippleColor) {
		try {
			RippleFactory.setRippleColor(view, color, rippleColor);
		} catch (RippleParseException e) {
			e.printStackTrace();
		}
	}

	private void updateRippleColor(int color) {
		mRippleColor = color;
		updateBkColor(userLayout, mTopColor, mRippleColor);
		updateBkColor(topLineLayout, mTopColor, mRippleColor);
		updateBkColor(yearButton, mSec1Color, mRippleColor);
		updateBkColor(matchButton, mSec2Color, mRippleColor);
		updateBkColor(playerButton, mSec3Color, mRippleColor);
		updateBkColor(insertButton, mSec4Color, mRippleColor);
		updateBkColor(gloryButton, mSec5Color, mRippleColor);
		updateBkColor(settingButton, mSec6Color, mRippleColor);
		updateBkColor(h2hButton, mBottomColor, mRippleColor);
		updateBkColor(rankButton, mBottomColor, mRippleColor);
	}

	@Override
	public void onColorSelected(int color) {

	}

	@Override
	public void onColorSelected(List<ColorPickerSelectionData> list) {
		for (ColorPickerSelectionData data:list) {
			JResource.updateColor(data.getKey(), data.getColor());
		}
		((ManagerActivity) mContext).onColorEdited();
	}

	@Override
	public void onColorCancleSelect() {
		resetColors();
	}

	@Override
	public void onApplyDefaultColors() {
		JResource.removeColor(ColorRes.DRAGSIDEBAR_RIPPLE_COLOR);
		JResource.removeColor(ColorRes.DRAGSIDEBAR_LINE_TOP);
		JResource.removeColor(ColorRes.DRAGSIDEBAR_SEC1);
		JResource.removeColor(ColorRes.DRAGSIDEBAR_SEC2);
		JResource.removeColor(ColorRes.DRAGSIDEBAR_SEC3);
		JResource.removeColor(ColorRes.DRAGSIDEBAR_SEC4);
		JResource.removeColor(ColorRes.DRAGSIDEBAR_SEC5);
		JResource.removeColor(ColorRes.DRAGSIDEBAR_SEC6);
		JResource.removeColor(ColorRes.DRAGSIDEBAR_LINE_BOTTOM);
		JResource.removeColor(ColorRes.DRAGSIDEBAR_RIPPLE_COLOR);
		((ManagerActivity) mContext).onColorEdited();
		resetColors();
	}

	private void resetColors() {
		updateColorValues();
		updateBkColor(userLayout, mTopColor, mRippleColor);
		updateBkColor(topLineLayout, mTopColor, mRippleColor);
		updateBkColor(yearButton, mSec1Color, mRippleColor);
		updateBkColor(matchButton, mSec2Color, mRippleColor);
		updateBkColor(playerButton, mSec3Color, mRippleColor);
		updateBkColor(insertButton, mSec4Color, mRippleColor);
		updateBkColor(gloryButton, mSec5Color, mRippleColor);
		updateBkColor(settingButton, mSec6Color, mRippleColor);
		updateBkColor(h2hButton, mBottomColor, mRippleColor);
		updateBkColor(rankButton, mBottomColor, mRippleColor);
	}

	private void updateColorValues() {
		mRippleColor = JResource.getColor(mContext
				, ColorRes.DRAGSIDEBAR_RIPPLE_COLOR, R.color.ripple_material_light);
		mTopColor = JResource.getColor(mContext
				, ColorRes.DRAGSIDEBAR_LINE_TOP, R.color.dragsidebar_sec0_blue);
		mSec1Color = JResource.getColor(mContext
				, ColorRes.DRAGSIDEBAR_SEC1, R.color.dragsidebar_sec1_blue);
		mSec2Color = JResource.getColor(mContext
				, ColorRes.DRAGSIDEBAR_SEC2, R.color.dragsidebar_sec2_blue);
		mSec3Color = JResource.getColor(mContext
				, ColorRes.DRAGSIDEBAR_SEC3, R.color.dragsidebar_sec3_blue);
		mSec4Color = JResource.getColor(mContext
				, ColorRes.DRAGSIDEBAR_SEC4, R.color.dragsidebar_sec4_blue);
		mSec5Color = JResource.getColor(mContext
				, ColorRes.DRAGSIDEBAR_SEC5, R.color.dragsidebar_sec5_blue);
		mSec6Color = JResource.getColor(mContext
				, ColorRes.DRAGSIDEBAR_SEC6, R.color.dragsidebar_sec6_blue);
		mBottomColor = JResource.getColor(mContext
				, ColorRes.DRAGSIDEBAR_LINE_BOTTOM, R.color.dragsidebar_sec7_blue);
	}

}
