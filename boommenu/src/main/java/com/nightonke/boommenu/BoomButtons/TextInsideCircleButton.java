package com.nightonke.boommenu.BoomButtons;

import android.content.Context;
import android.graphics.PointF;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.R;
import com.nightonke.boommenu.Util;

import java.util.ArrayList;

/**
 * Created by Weiping Huang at 22:44 on 16/11/23
 * For Personal Open Source
 * Contact me at 2584541288@qq.com or nightonke@outlook.com
 * For more projects: https://github.com/Nightonke
 */

@SuppressWarnings("unused")
public class TextInsideCircleButton extends BoomButton {

    private TextInsideCircleButton(Builder builder, Context context) {
        super(context);
        this.context = context;
        this.buttonEnum = ButtonEnum.TextInsideCircle;
        init(builder);
    }

    private void init(Builder builder) {
        LayoutInflater.from(context).inflate(R.layout.bmb_text_inside_circle_button, this, true);
        initAttrs(builder);
        if (isRound) initShadow(buttonRadius + shadowRadius);
        else initShadow(shadowCornerRadius);
        initCircleButton();

        // @author:Jing 不调用父类的initText(button)
        // 很奇怪必须先initText再initImage，否则一开始text会被image遮住，image按压一次抬起才会显示text
//        initText(button);
        initText();

        initImage();

        centerPoint = new PointF(
                buttonRadius + shadowRadius + shadowOffsetX,
                buttonRadius + shadowRadius + shadowOffsetY);
    }

    private void initAttrs(Builder builder) {
        super.initAttrs(builder);
    }

    /**
     * 覆盖父类的方法
     * @author:Jing Yang: make sure the image is center of the button, and it's size is half of button size
     * move image a little upper cause there are texts below
     */
    @Override
    protected void initImage() {
        image = new ImageView(context);
        updateImageRect();
        updateImagePadding();

        LayoutParams params = new LayoutParams(buttonRadius, buttonRadius);
        params.leftMargin = buttonRadius / 2;
        params.topMargin = buttonRadius / 2 - Util.dp2px(5);

        button.addView(image, params);
        lastStateIsNormal = false;
        toNormal();
    }

    /**
     * @author:Jing 不调用父类的initText，保持文字在image下面
     */
    protected void initText() {
        text = new TextView(context);
//        updateTextRect();
//        updateTextPadding();
        text.setGravity(Gravity.CENTER);
        if (typeface != null) text.setTypeface(typeface);
        text.setMaxLines(maxLines);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        text.setGravity(textGravity);
        text.setEllipsize(ellipsize);
        if (ellipsize == TextUtils.TruncateAt.MARQUEE) {
            text.setSingleLine(true);
            text.setMarqueeRepeatLimit(-1);
            text.setHorizontallyScrolling(true);
            text.setFocusable(true);
            text.setFocusableInTouchMode(true);
            text.setFreezesText(true);
            post(new Runnable() {
                @Override
                public void run() {
                    text.setSelected(true);
                }
            });
        }
        LayoutParams params = new LayoutParams(buttonRadius * 2, ViewGroup.LayoutParams.WRAP_CONTENT);
        // imageSize + imageTop + textTop
        params.topMargin = buttonRadius + (buttonRadius / 2 - Util.dp2px(5));
        button.addView(text, params);
    }

    @Override
    public ArrayList<View> goneViews() {
        ArrayList<View> goneViews = new ArrayList<>();
        goneViews.add(image);
        goneViews.add(text);
        return goneViews;
    }

    @Override
    public ArrayList<View> rotateViews() {
        ArrayList<View> rotateViews = new ArrayList<>();
        if (rotateImage) rotateViews.add(image);
        if (rotateText) rotateViews.add(text);
        return rotateViews;
    }

    @Override
    public int trueWidth() {
        return buttonRadius * 2 + shadowRadius * 2 + shadowOffsetX * 2;
    }

    @Override
    public int trueHeight() {
        return buttonRadius * 2 + shadowRadius * 2 + shadowOffsetY * 2;
    }

    @Override
    public int contentWidth() {
        return buttonRadius * 2;
    }

    @Override
    public int contentHeight() {
        return buttonRadius * 2;
    }

    @Override
    public void toHighlighted() {
        if (lastStateIsNormal && ableToHighlight) {
            toHighlightedImage();
            toHighlightedText();
            lastStateIsNormal = false;
        }
    }

    @Override
    public void toNormal() {
        if (!lastStateIsNormal) {
            toNormalImage();
            toNormalText();
            lastStateIsNormal = true;
        }
    }

    @Override
    public void setRotateAnchorPoints() {
        image.setPivotX(buttonRadius - imageRect.left);
        image.setPivotY(buttonRadius - imageRect.top);
        text.setPivotX(buttonRadius - textRect.left);
        text.setPivotY(buttonRadius - textRect.top);
    }

    @Override
    public void setSelfScaleAnchorPoints() {

    }

    public static class Builder extends BoomButtonWithTextBuilder<Builder> {

        /**
         * Whether the text-view should rotate.
         *
         * @param rotateText rotate or not
         * @return the builder
         */
        public Builder rotateText(boolean rotateText) {
            this.rotateText = rotateText;
            return this;
        }

        /**
         * The radius of boom-button, in pixel.
         *
         * @param buttonRadius the button radius
         * @return the builder
         */
        public Builder buttonRadius(int buttonRadius) {
            this.buttonRadius = buttonRadius;
            return this;
        }

        /**
         * Set the corner-radius of button.
         *
         * @param buttonCornerRadius corner-radius of button
         * @return the builder
         */
        public Builder buttonCornerRadius(int buttonCornerRadius) {
            this.buttonCornerRadius = buttonCornerRadius;
            return this;
        }

        /**
         * Whether the button is a circle shape.
         *
         * @param isRound is or not
         * @return the builder
         */
        public Builder isRound(boolean isRound) {
            this.isRound = isRound;
            return this;
        }

        /**
         * Gets button radius.
         *
         * @return the button radius
         */
        public int getButtonRadius() {
            return buttonRadius;
        }

        /**
         * Build text-inside circle button, only use in BMB package.
         *
         * @param context the context
         * @return the simple circle button
         */
        public TextInsideCircleButton build(Context context) {
            TextInsideCircleButton button = new TextInsideCircleButton(this, context);
            weakReferenceButton(button);
            return button;
        }
    }
}
