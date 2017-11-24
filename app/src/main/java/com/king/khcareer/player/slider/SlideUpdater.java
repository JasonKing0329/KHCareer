package com.king.khcareer.player.slider;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.view.View;

import com.king.khcareer.pubview.cardslider.CardSliderLayoutManager;
import com.king.khcareer.pubview.cardslider.DefaultViewUpdater;
import com.king.mytennis.view.R;

public class SlideUpdater extends DefaultViewUpdater {

    public SlideUpdater(CardSliderLayoutManager lm) {
        super(lm);
    }

    @Override
    public void onLayoutManagerInitialized() {
        super.onLayoutManagerInitialized();
    }

    @Override
    protected void onUpdateViewAlpha(@NonNull View view, float alpha) {
        final View alphaView = view.findViewById(R.id.v_alpha);
        final View imageView = view.findViewById(R.id.iv_thumb);

        final boolean isLeftCard = alpha < 1;
        if (isLeftCard) {
            ViewCompat.setAlpha(alphaView, 0.9f - alpha);
            ViewCompat.setAlpha(imageView, 0.3f + alpha);
        } else {
            if (ViewCompat.getAlpha(alphaView) != 0) {
                ViewCompat.setAlpha(alphaView, 0f);
            }

            if (ViewCompat.getAlpha(imageView) != 1) {
                ViewCompat.setAlpha(imageView, 1f);
            }
        }
    }

    @Override
    protected void onUpdateViewZ(@NonNull View view, float z) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ((CardView) view).setCardElevation(Math.max(0, z));
        } else {
            super.onUpdateViewZ(view, z);
        }
    }
}
