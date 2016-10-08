package com.king.lib.tool.ui;

import java.util.Arrays;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.view.View;

/**
 * @author JingYang
 * @version create time：2016-1-16 下午1:37:44
 *
 * for RippleDrawable(colorStateList, content, mask)
 * colorStateList 包含了波纹应有的颜色
 * content 则是正常状态下view的背景颜色
 * mask 是限定ripple效果的形状（是在view区域内的）
 * 只有当content和mask同时为null的情况，该RippleDrawable才是borderless效果的
 * 如果设置了content，即正常状态下的背景色，那么波纹区域是限定在view区域内的
 * mask可以定义为任何形状，波纹将会在view区域内，显示在mask定义的区域内
 *
 * 所以，如果某个view要应用borderless效果，只能不设置正常状态下的背景色（即content=null）
 * 同时mask也置为null
 */
public class RippleFactory {

	/**
	 * This method is for view which has already set RippleDrawable
	 * and for example, the RippleDrawable is like this:
	 * new RippleDrawable(colorStateList, content, mask)
	 * if content and mask are both null, then should call this method to change ripple color
	 *
	 * @param view
	 *            view has already set ripple drawable
	 * @param bgColor
	 *            useless
	 * @param tintColor
	 *            the color of ripple
	 * @throws RippleParseException
	 */
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public static void setRippleColor(View view/* , int bgColor */,
									  int tintColor) throws RippleParseException {
		try {
			RippleDrawable ripple = (RippleDrawable) view.getBackground();
			ripple.setColor(ColorStateList.valueOf(tintColor));
			// no effect
			// ColorDrawable rippleBackground = (ColorDrawable) ripple
			// .getDrawable(0);
			// rippleBackground.setColor(bgColor);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RippleParseException();
		}
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public static void setRippleColor(View view, int bgColor,
									  int tintColor) throws RippleParseException {
		try {
			RippleDrawable ripple = (RippleDrawable) view.getBackground();
			ripple.setColor(ColorStateList.valueOf(tintColor));
			// no effect
			ColorDrawable rippleBackground = (ColorDrawable) ripple
					.getDrawable(0);
			rippleBackground.setColor(bgColor);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RippleParseException();
		}
	}

	/**
	 * create ripple drawable which include normal background and ripple color
	 * ripple will be limited by padding,inset,conerRadius parameters
	 *
	 * @param context
	 * @param colorButtonNormal
	 * @param colorControlHighlight
	 * @param paddingH
	 * @param paddingV
	 * @param insetH
	 * @param insetV
	 * @param cornerRadius
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private static Drawable createButtonRippleBg(
			int colorButtonNormal, int colorControlHighlight, int paddingH,
			int paddingV, int insetH, int insetV, int cornerRadius) {
		return new RippleDrawable(
				ColorStateList.valueOf(colorControlHighlight),
				createButtonShape(colorButtonNormal, paddingH,
						paddingV, insetH, insetV, cornerRadius), null);
	}

	/**
	 * create ripple drawable which include normal background and ripple color
	 * ripple will fill view's visible region
	 * the effect is same with method getRippleBackground
	 * @param context
	 * @param normalColor
	 * @param rippleColor
	 * @return
	 @TargetApi(Build.VERSION_CODES.LOLLIPOP)
	 public static Drawable createButtonRippleBg(
	 int normalColor, int rippleColor) {
	 return new RippleDrawable(ColorStateList.valueOf(rippleColor),
	 createButtonShape(normalColor, 0, 0, 0, 0, 0), null);
	 }
	 */

	private static Drawable createButtonShape(int color,
											  int paddingH, int paddingV, int insetH, int insetV, int cornerRadius) {

		float[] outerRadii = new float[8];
		Arrays.fill(outerRadii, cornerRadius);

		RoundRectShape r = new RoundRectShape(outerRadii, null, null);

		ShapeDrawable shapeDrawable = new ShapeDrawable(r);
		shapeDrawable.getPaint().setColor(color);
		shapeDrawable.setPadding(paddingH, paddingV, paddingH, paddingV);

		return new InsetDrawable(shapeDrawable, insetH, insetV, insetH, insetV);
	}

	/**
	 * create ripple drawable which include normal background and ripple color
	 * ripple will fill view's visible region except normalColor's alpha is 0
	 *
	 * @param normalColor if its alpha is 0, the ripple effect will be borderless
	 * @param rippleColor
	 * @param clip
	 *            useless, ripple will fill view's visible region no matter it
	 *            was true or false
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public static Drawable getRippleBackground(final int normalColor,
											   final int rippleColor/* , final boolean clip */) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			final ColorStateList rippleStateList = new ColorStateList(
					new int[][] { new int[] { android.R.attr.state_pressed },
							new int[0] },
					new int[] { rippleColor, rippleColor });
			final int alpha = Color.alpha(normalColor);
			final Drawable content = alpha > 0 ? new ColorDrawable(normalColor)
					: null;
			// final Drawable mask = clip ? new ShapeDrawable(new RectShape())
			// : null;
			// return new RippleDrawable(rippleStateList, content, mask);
			return new RippleDrawable(rippleStateList, content, null);
		} else {
			final StateListDrawable backgroundDrawable = new StateListDrawable();
			backgroundDrawable.addState(
					new int[] { android.R.attr.state_pressed },
					new ColorDrawable(rippleColor));
			backgroundDrawable.addState(new int[] {}, new ColorDrawable(
					normalColor));
			return backgroundDrawable;
		}
	}

	/**
	 * create ripple drawable which include normal background and ripple color
	 * ripple will fill view's visible region
	 * @param normalColor
	 * @param rippleColor
	 * @param clip if false and normalColor's alpha is 0, ripple effect will be borderless
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public static Drawable getRippleBackground(final int normalColor,
											   final int rippleColor, final boolean clip) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			final ColorStateList rippleStateList = new ColorStateList(
					new int[][] { new int[] { android.R.attr.state_pressed },
							new int[0] },
					new int[] { rippleColor, rippleColor });
			final int alpha = Color.alpha(normalColor);
			final Drawable content = alpha > 0 ? new ColorDrawable(normalColor)
					: null;
			final Drawable mask = clip ? new ShapeDrawable(new RectShape())
					: null;
			return new RippleDrawable(rippleStateList, content, mask);
//			return new RippleDrawable(rippleStateList, content, null);
		} else {
			final StateListDrawable backgroundDrawable = new StateListDrawable();
			backgroundDrawable.addState(
					new int[] { android.R.attr.state_pressed },
					new ColorDrawable(rippleColor));
			backgroundDrawable.addState(new int[] {}, new ColorDrawable(
					normalColor));
			return backgroundDrawable;
		}
	}

	/**
	 * create borderless effect ripple drawable
	 * borderless ripple couldn't have normal status color
	 * @param color
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public static Drawable getBorderlessRippleBackground(int color) {
		ColorStateList rippleStateList = new ColorStateList(new int[][] {
				new int[] { android.R.attr.state_pressed }, new int[0] },
				new int[] { color, color });
		return new RippleDrawable(rippleStateList, null, new ShapeDrawable(new OvalShape()));
	}
}
