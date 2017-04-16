package com.king.khcareer.common.res;

import java.util.ArrayList;
import java.util.List;

import com.king.lib.colorpicker.ColorPickerSelectionData;
import com.king.mytennis.view.R;

import android.content.Context;

/**
 * @author JingYang
 * @version create time：2016-1-13 下午2:27:43
 *
 */
public class ColorResManager {

	public List<ColorPickerSelectionData> getCardViewYearPage(Context context) {
		List<ColorPickerSelectionData> list = new ArrayList<ColorPickerSelectionData>();
		ColorPickerSelectionData data = new ColorPickerSelectionData();
		data.setKey(ColorRes.CARDVIEW_YEAR_YEAR);
		data.setName("年份");
		data.setColor(JResource.getColor(context
				, ColorRes.CARDVIEW_YEAR_YEAR, R.color.cardview_year_year));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.CARDVIEW_YEAR_COUNT);
		data.setName("数据统计");
		data.setColor(JResource.getColor(context
				, ColorRes.CARDVIEW_YEAR_COUNT, R.color.cardview_year_count));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.CARDVIEW_YEAR_FRAMEBK);
		data.setName("List背景");
		data.setColor(JResource.getColor(context
				, ColorRes.CARDVIEW_YEAR_FRAMEBK, R.color.cardview_year_framebk));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.CARDVIEW_YEAR_ITEM_MONTH);
		data.setName("月份");
		data.setColor(JResource.getColor(context
				, ColorRes.CARDVIEW_YEAR_ITEM_MONTH, R.color.cardview_year_item_month));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.CARDVIEW_YEAR_ITEM_WINNER);
		data.setName("冠军赛事名称");
		data.setColor(JResource.getColor(context
				, ColorRes.CARDVIEW_YEAR_ITEM_WINNER, R.color.cardview_year_item_winner));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.CARDVIEW_YEAR_ITEM_NORMAL);
		data.setName("比赛名称");
		data.setColor(JResource.getColor(context
				, ColorRes.CARDVIEW_YEAR_ITEM_NORMAL, R.color.cardview_year_item_normal));
		list.add(data);
		return list;
	}

	public List<ColorPickerSelectionData> getCardViewMatchPage(Context context) {
		List<ColorPickerSelectionData> list = new ArrayList<ColorPickerSelectionData>();
		ColorPickerSelectionData data = new ColorPickerSelectionData();
		data.setKey(ColorRes.CARDVIEW_MATCH_TITLE);
		data.setName("赛事名称");
		data.setColor(JResource.getColor(context
				, ColorRes.CARDVIEW_MATCH_TITLE, R.color.cardview_match_title));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.CARDVIEW_MATCH_TITLE_LEVEL);
		data.setName("赛事级别");
		data.setColor(JResource.getColor(context
				, ColorRes.CARDVIEW_MATCH_TITLE_LEVEL, R.color.cardview_match_title_level));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.CARDVIEW_MATCH_LIST_BK);
		data.setName("List背景");
		data.setColor(JResource.getColor(context
				, ColorRes.CARDVIEW_MATCH_LIST_BK, R.color.cardview_match_list_bk));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.CARDVIEW_MATCH_LIST_DATE);
		data.setName("时间");
		data.setColor(JResource.getColor(context
				, ColorRes.CARDVIEW_MATCH_LIST_DATE, R.color.cardview_match_list_date));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.CARDVIEW_MATCH_LIST_WINNER);
		data.setName("冠军");
		data.setColor(JResource.getColor(context
				, ColorRes.CARDVIEW_MATCH_LIST_WINNER, R.color.cardview_match_list_winner));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.CARDVIEW_MATCH_LIST_RUNNERUP);
		data.setName("亚军");
		data.setColor(JResource.getColor(context
				, ColorRes.CARDVIEW_MATCH_LIST_RUNNERUP, R.color.cardview_match_list_runnerup));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.CARDVIEW_MATCH_LIST_SF);
		data.setName("半决赛");
		data.setColor(JResource.getColor(context
				, ColorRes.CARDVIEW_MATCH_LIST_SF, R.color.cardview_match_list_sf));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.CARDVIEW_MATCH_LIST_NORMAL);
		data.setName("普通轮次");
		data.setColor(JResource.getColor(context
				, ColorRes.CARDVIEW_MATCH_LIST_NORMAL, R.color.cardview_match_list_normal));
		list.add(data);
		return list;
	}

	public List<ColorPickerSelectionData> getDragSideBar(Context context) {
		List<ColorPickerSelectionData> list = new ArrayList<ColorPickerSelectionData>();
		ColorPickerSelectionData data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DRAGSIDEBAR_LINE_TOP);
		data.setName("顶部栏");
		data.setColor(JResource.getColor(context
				, ColorRes.DRAGSIDEBAR_LINE_TOP, R.color.dragsidebar_sec0_blue));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DRAGSIDEBAR_SEC1);
		data.setName("色块1");
		data.setColor(JResource.getColor(context
				, ColorRes.DRAGSIDEBAR_SEC1, R.color.dragsidebar_sec1_blue));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DRAGSIDEBAR_SEC2);
		data.setName("色块2");
		data.setColor(JResource.getColor(context
				, ColorRes.DRAGSIDEBAR_SEC2, R.color.dragsidebar_sec2_blue));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DRAGSIDEBAR_SEC3);
		data.setName("色块3");
		data.setColor(JResource.getColor(context
				, ColorRes.DRAGSIDEBAR_SEC3, R.color.dragsidebar_sec3_blue));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DRAGSIDEBAR_SEC4);
		data.setName("色块4");
		data.setColor(JResource.getColor(context
				, ColorRes.DRAGSIDEBAR_SEC4, R.color.dragsidebar_sec4_blue));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DRAGSIDEBAR_SEC5);
		data.setName("色块5");
		data.setColor(JResource.getColor(context
				, ColorRes.DRAGSIDEBAR_SEC5, R.color.dragsidebar_sec5_blue));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DRAGSIDEBAR_SEC6);
		data.setName("色块6");
		data.setColor(JResource.getColor(context
				, ColorRes.DRAGSIDEBAR_SEC6, R.color.dragsidebar_sec6_blue));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DRAGSIDEBAR_LINE_BOTTOM);
		data.setName("底部栏");
		data.setColor(JResource.getColor(context
				, ColorRes.DRAGSIDEBAR_LINE_BOTTOM, R.color.dragsidebar_sec7_blue));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DRAGSIDEBAR_RIPPLE_COLOR);
		data.setName("波纹颜色");
		data.setColor(JResource.getColor(context
				, ColorRes.DRAGSIDEBAR_RIPPLE_COLOR, R.color.ripple_material_light));
		list.add(data);
		return list;
	}

	public List<ColorPickerSelectionData> getDetailGalleryMatchPage(
			Context context) {
		List<ColorPickerSelectionData> list = new ArrayList<ColorPickerSelectionData>();
		ColorPickerSelectionData data = new ColorPickerSelectionData();

		data.setKey(ColorRes.DETAIL_GALLERY_BK);
		data.setName("背景");
		data.setColor(JResource.getColor(context
				, ColorRes.DETAIL_GALLERY_BK, R.color.detail_gallery_bk));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DETAIL_GALLERY_BOTTOM_TEXT);
		data.setName("底部文字");
		data.setColor(JResource.getColor(context
				, ColorRes.DETAIL_GALLERY_BOTTOM_TEXT, R.color.detail_gallery_bottom_text));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DETAIL_VIEWPAGE_MATCH);
		data.setName("卡片背景");
		data.setColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_MATCH, R.color.detail_viewpage_match));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DETAIL_VIEWPAGE_MATCH_TITLE);
		data.setName("标题");
		data.setColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_MATCH_TITLE, R.color.detail_viewpage_match_title));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN1);
		data.setName("日期按钮背景");
		data.setColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_MATCH_BTN1, R.color.detail_viewpage_match_btn1));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN1_TEXT);
		data.setName("日期按钮文字");
		data.setColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_MATCH_BTN1_TEXT, R.color.detail_viewpage_match_btn1_text));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN2);
		data.setName("级别按钮背景");
		data.setColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_MATCH_BTN2, R.color.detail_viewpage_match_btn2));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN2_TEXT);
		data.setName("级别按钮文字");
		data.setColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_MATCH_BTN2_TEXT, R.color.detail_viewpage_match_btn2_text));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN3);
		data.setName("场地按钮背景");
		data.setColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_MATCH_BTN3, R.color.detail_viewpage_match_btn3));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN3_TEXT);
		data.setName("场地按钮文字");
		data.setColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_MATCH_BTN3_TEXT, R.color.detail_viewpage_match_btn3_text));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN4);
		data.setName("区域按钮背景");
		data.setColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_MATCH_BTN4, R.color.detail_viewpage_match_btn4));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN4_TEXT);
		data.setName("区域按钮文字");
		data.setColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_MATCH_BTN4_TEXT, R.color.detail_viewpage_match_btn4_text));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN5);
		data.setName("轮次按钮背景");
		data.setColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_MATCH_BTN5, R.color.detail_viewpage_match_btn5));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN5_TEXT);
		data.setName("轮次按钮文字");
		data.setColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_MATCH_BTN5_TEXT, R.color.detail_viewpage_match_btn5_text));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN6);
		data.setName("比分按钮背景");
		data.setColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_MATCH_BTN6, R.color.detail_viewpage_match_btn6));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN6_TEXT);
		data.setName("比分按钮文字");
		data.setColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_MATCH_BTN6_TEXT, R.color.detail_viewpage_match_btn6_text));
		list.add(data);

		return list;
	}

	public List<ColorPickerSelectionData> getDetailGalleryH2hPage(
			Context context) {
		List<ColorPickerSelectionData> list = new ArrayList<ColorPickerSelectionData>();
		ColorPickerSelectionData data = new ColorPickerSelectionData();

		data.setKey(ColorRes.DETAIL_GALLERY_BK);
		data.setName("背景");
		data.setColor(JResource.getColor(context
				, ColorRes.DETAIL_GALLERY_BK, R.color.detail_gallery_bk));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DETAIL_GALLERY_BOTTOM_TEXT);
		data.setName("底部文字");
		data.setColor(JResource.getColor(context
				, ColorRes.DETAIL_GALLERY_BOTTOM_TEXT, R.color.detail_gallery_bottom_text));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DETAIL_VIEWPAGE_H2H);
		data.setName("卡片背景");
		data.setColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_H2H, R.color.detail_viewpage_h2h));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DETAIL_VIEWPAGE_H2H_TITLE);
		data.setName("标题");
		data.setColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_H2H_TITLE, R.color.detail_viewpage_h2h_title));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DETAIL_VIEWPAGE_H2H_BTN);
		data.setName("H2H按钮背景");
		data.setColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_H2H_BTN, R.color.detail_viewpage_h2h_btn));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DETAIL_VIEWPAGE_H2H_ITEM_SEC1);
		data.setName("Item区域1背景");
		data.setColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_H2H_ITEM_SEC1, R.color.detail_viewpage_h2h_item_sec1));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DETAIL_VIEWPAGE_H2H_ITEM_TEXT1);
		data.setName("Item区域1文字");
		data.setColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_H2H_ITEM_TEXT1, R.color.detail_viewpage_h2h_item_text1));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DETAIL_VIEWPAGE_H2H_ITEM_SEC2);
		data.setName("Item区域2背景");
		data.setColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_H2H_ITEM_SEC2, R.color.detail_viewpage_h2h_item_sec2));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DETAIL_VIEWPAGE_H2H_ITEM_TEXT2);
		data.setName("Item区域2文字");
		data.setColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_H2H_ITEM_TEXT2, R.color.detail_viewpage_h2h_item_text2));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DETAIL_VIEWPAGE_H2H_ITEM_SEC3);
		data.setName("Item区域3背景");
		data.setColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_H2H_ITEM_SEC3, R.color.detail_viewpage_h2h_item_sec3));
		list.add(data);

		data = new ColorPickerSelectionData();
		data.setKey(ColorRes.DETAIL_VIEWPAGE_H2H_ITEM_TEXT3);
		data.setName("Item区域3文字");
		data.setColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_H2H_ITEM_TEXT3, R.color.detail_viewpage_h2h_item_text3));
		list.add(data);
		return list;
	}
}
