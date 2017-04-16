package com.king.khcareer.pubview;

import java.util.List;

import android.R.anim;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class ArcButtonView implements OnClickListener {

	private ImageButton buttonDelete;
	private Context context;
	private LayoutParams initParams;
	private boolean isExpand;
	private Animation animationTranslate, animationRotate, animationScale;

	private List<ImageButton> buttons;
	private OnClickListener buttonListener;
	private float radius;

	/**
	 *
	 * @param context
	 * @param radius 圆弧半径
	 * @param buttonWidth 每一个button的大小
	 * @param buttons 不能为空。第0个代表streach button, 后面的按照从弧形下方到上方顺序排列
	 * @param btlistener 每个button的非UI事件监听
	 */
	public ArcButtonView(Context context, float radius, int buttonWidth
			, List<ImageButton> buttons, OnClickListener btlistener) {
		this.context = context;
		this.buttons = buttons;
		this.radius = radius;
		this.buttonListener = btlistener;

		initParams = new LayoutParams(buttonWidth, buttonWidth);
		initParams.setMargins(0, 0, 0, 0);
		initParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		buttonDelete = buttons.get(0);


		//buttonDelete.startAnimation(animRotate(-45.0f, 0.5f, 0.45f));
		buttonDelete.setOnClickListener(this);
		for (int i = 1; i < buttons.size(); i ++) {
			buttons.get(i).setOnClickListener(this);
			buttons.get(i).setVisibility(View.GONE);
		}
	}

	protected Animation setAnimScale(float toX, float toY)
	{
		// TODO Auto-generated method stub
		animationScale = new ScaleAnimation(1f, toX, 1f, toY, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.45f);
		animationScale.setInterpolator(context, anim.accelerate_decelerate_interpolator);
		animationScale.setDuration(500);
		animationScale.setFillAfter(false);
		return animationScale;

	}

	protected Animation animRotate(float toDegrees, float pivotXValue, float pivotYValue)
	{
		// TODO Auto-generated method stub
		animationRotate = new RotateAnimation(0, toDegrees, Animation.RELATIVE_TO_SELF, pivotXValue, Animation.RELATIVE_TO_SELF, pivotYValue);
		animationRotate.setAnimationListener(new AnimationListener()
		{

			@Override
			public void onAnimationStart(Animation animation)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation)
			{
				// TODO Auto-generated method stub
				animationRotate.setFillAfter(true);
			}
		});
		return animationRotate;
	}

	/**
	 *
	 * @param toX 动画结束的点离当前View X坐标上的差值
	 * @param toY 动画结束的点离当前View Y坐标上的差值
	 * @param button 执行动画的ImageButton
	 * @param durationMillis 动画时间
	 * @return
	 */
	protected Animation animTranslate(final float toX, final float toY,
									  final ImageButton button, long durationMillis)
	{
		//移动的动画效果
		/*
		 * TranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta)
		 *
		 * float fromXDelta:这个参数表示动画开始的点离当前View X坐标上的差值；
	     *
	　　       * float toXDelta, 这个参数表示动画结束的点离当前View X坐标上的差值；
	     *
	　　       * float fromYDelta, 这个参数表示动画开始的点离当前View Y坐标上的差值；
	     *
	　　       * float toYDelta)这个参数表示动画结束的点离当前View Y坐标上的差值；
		 */
		animationTranslate = new TranslateAnimation(0, toX, 0, toY);
		animationTranslate.setAnimationListener(new AnimationListener()
		{

			@Override
			public void onAnimationStart(Animation animation)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation)
			{
				// TODO Auto-generated method stub

				LayoutParams params = new LayoutParams(initParams.width, initParams.height);
				params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				params.setMargins((int)toX, 0, 0, -(int)toY);
				button.setLayoutParams(params);
				button.clearAnimation();

			}
		});
		animationTranslate.setDuration(durationMillis);
		return animationTranslate;
	}

	/**
	 * 基于圆坐标，等分弧的x,y坐标关系，x = r * cos(theta), y = r * sin(theta)
	 */
	@Override
	public void onClick(View v) {
		if (v == buttonDelete) {
			if(!isExpand)
			{
				isExpand = true;
				buttonDelete.startAnimation(animRotate(45.0f, 0.5f, 0.45f));

				double pi = Math.PI;
				double spaceTheta = pi/36;
				double theta = (pi/2 - 2*spaceTheta)/(buttons.size() - 2);
				float x = 0;
				float y = 0;
				int duration = 260;
				for (int i = 1; i < buttons.size(); i ++) {
					x = radius * (float)Math.cos(spaceTheta + (i - 1) * theta);
					y = -radius * (float)Math.sin(spaceTheta + (i - 1) * theta);
					buttons.get(i).startAnimation(animTranslate(x, y, buttons.get(i), duration));
					buttons.get(i).setVisibility(View.VISIBLE);
					duration -= 30;
				}
			}
			else
			{
				isExpand = false;
				buttonDelete.startAnimation(animRotate(0.0f, 0.5f, 0.45f));
				int duration = 260;
				for (int i = buttons.size() - 1; i > 0; i --) {
					buttons.get(i).startAnimation(animTranslate(0, 0, buttons.get(i), duration));
					buttons.get(i).setVisibility(View.GONE);
					duration -= 30;
				}

			}
		}
		else {
			v.startAnimation(setAnimScale(2.5f, 2.5f));
			buttonListener.onClick(v);
		}
	}

	public boolean isExpand() {
		return isExpand;
	}
}
