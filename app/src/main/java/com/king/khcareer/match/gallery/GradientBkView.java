package com.king.khcareer.match.gallery;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 描述: 实现跟随match item滑动变化渐变背景的view
 * <p/>作者：景阳
 * <p/>创建时间: 2017/3/21 20:35
 */
public class GradientBkView extends View {

    private Paint gradientPaint;
    private int[] currentGradient;

    public GradientBkView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        gradientPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        initGradient();
        setWillNotDraw(false);
    }

    private void initGradient() {
        if (currentGradient == null) {
            gradientPaint.setColor(Color.WHITE);
            gradientPaint.setShader(null);
        }
        else {
            float centerX = getWidth() * 0.5f;
            Shader gradient = new LinearGradient(
                    centerX, 0, centerX, getHeight(),
                    currentGradient, null,
                    Shader.TileMode.MIRROR);
            gradientPaint.setShader(gradient);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, getWidth(), getHeight(), gradientPaint);
        super.onDraw(canvas);
    }

    public void updateGradientValues(int[] values) {
        currentGradient = values;
        initGradient();
        invalidate();
    }

}
