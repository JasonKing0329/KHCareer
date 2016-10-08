package com.king.mytennis.service;

import java.util.Random;

import com.king.mytennis.view.R;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

public class UIProvider {
	
	/**
	 * for Android L RippleDrawable
	 * @param pressedColor resource id
	 * @return
	 */
	public static ColorStateList createColorStateListForRipple(int pressedColor) {
        int[] colors = new int[] {pressedColor};
        int[][] states = new int[1][];
        states[0] = new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled };
        ColorStateList colorList = new ColorStateList(states, colors);  
        return colorList;  
	}
	
	public static ColorStateList createColorStateList(int normal, int pressed, int focused, int unable) {  
        int[] colors = new int[] { pressed, focused, normal, focused, unable, normal };  
        int[][] states = new int[6][];  
        states[0] = new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled };  
        states[1] = new int[] { android.R.attr.state_enabled, android.R.attr.state_focused };  
        states[2] = new int[] { android.R.attr.state_enabled };  
        states[3] = new int[] { android.R.attr.state_focused };  
        states[4] = new int[] { android.R.attr.state_window_focused };  
        states[5] = new int[] {};  
        ColorStateList colorList = new ColorStateList(states, colors);  
        return colorList;  
    }  
  
    public static StateListDrawable newSelector(Context context, int idNormal, int idPressed, int idFocused,  
            int idUnable) {  
        StateListDrawable bg = new StateListDrawable();  
        Drawable normal = idNormal == -1 ? null : context.getResources().getDrawable(idNormal);  
        Drawable pressed = idPressed == -1 ? null : context.getResources().getDrawable(idPressed);  
        Drawable focused = idFocused == -1 ? null : context.getResources().getDrawable(idFocused);  
        Drawable unable = idUnable == -1 ? null : context.getResources().getDrawable(idUnable);  
        // View.PRESSED_ENABLED_STATE_SET  
        bg.addState(new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled }, pressed);  
        // View.ENABLED_FOCUSED_STATE_SET  
        bg.addState(new int[] { android.R.attr.state_enabled, android.R.attr.state_focused }, focused);  
        // View.ENABLED_STATE_SET  
        bg.addState(new int[] { android.R.attr.state_enabled }, normal);  
        // View.FOCUSED_STATE_SET  
        bg.addState(new int[] { android.R.attr.state_focused }, focused);  
        // View.WINDOW_FOCUSED_STATE_SET  
        bg.addState(new int[] { android.R.attr.state_window_focused }, unable);  
        // View.EMPTY_STATE_SET  
        bg.addState(new int[] {}, normal);  
        return bg;  
    }
    
    public static int[] getDragSideBarColors () {
    	int[][] colors = new int[6][];
    	
    	Random random = new Random();
    	int index = Math.abs(random.nextInt()) % 6;
    	
    	colors[0] = new int[] {
    		R.color.dragsidebar_sec0_blue,
    		R.color.dragsidebar_sec1_blue,
    		R.color.dragsidebar_sec2_blue,
    		R.color.dragsidebar_sec3_blue,
    		R.color.dragsidebar_sec4_blue,
    		R.color.dragsidebar_sec5_blue,
    		R.color.dragsidebar_sec6_blue,
    		R.color.dragsidebar_sec7_blue
    	};

    	colors[1] = new int[] {
    		R.color.dragsidebar_sec0_red,
    		R.color.dragsidebar_sec1_red,
    		R.color.dragsidebar_sec2_red,
    		R.color.dragsidebar_sec3_red,
    		R.color.dragsidebar_sec4_red,
    		R.color.dragsidebar_sec5_red,
    		R.color.dragsidebar_sec6_red,
    		R.color.dragsidebar_sec7_red
    	};

    	colors[2] = new int[] {
    		R.color.dragsidebar_sec0_brown,
    		R.color.dragsidebar_sec1_brown,
    		R.color.dragsidebar_sec2_brown,
    		R.color.dragsidebar_sec3_brown,
    		R.color.dragsidebar_sec4_brown,
    		R.color.dragsidebar_sec5_brown,
    		R.color.dragsidebar_sec6_brown,
    		R.color.dragsidebar_sec7_brown
    	};

    	colors[3] = new int[] {
    		R.color.dragsidebar_sec0_green,
    		R.color.dragsidebar_sec1_green,
    		R.color.dragsidebar_sec2_green,
    		R.color.dragsidebar_sec3_green,
    		R.color.dragsidebar_sec4_green,
    		R.color.dragsidebar_sec5_green,
    		R.color.dragsidebar_sec6_green,
    		R.color.dragsidebar_sec7_green
    	};

    	colors[4] = new int[] {
    		R.color.dragsidebar_sec0_purple,
    		R.color.dragsidebar_sec1_purple,
    		R.color.dragsidebar_sec2_purple,
    		R.color.dragsidebar_sec3_purple,
    		R.color.dragsidebar_sec4_purple,
    		R.color.dragsidebar_sec5_purple,
    		R.color.dragsidebar_sec6_purple,
    		R.color.dragsidebar_sec7_purple
    	};

    	colors[5] = new int[] {
    		R.color.dragsidebar_sec0_theme1,
    		R.color.dragsidebar_sec1_theme1,
    		R.color.dragsidebar_sec2_theme1,
    		R.color.dragsidebar_sec3_theme1,
    		R.color.dragsidebar_sec4_theme1,
    		R.color.dragsidebar_sec5_theme1,
    		R.color.dragsidebar_sec6_theme1,
    		R.color.dragsidebar_sec7_theme1
    	};
    	
    	return colors[index];
    }
}
