package com.king.khcareer.home.k4;

import com.king.mytennis.view.R;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceAlignmentEnum;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/4/24 15:26
 */
public class BoomMenuHome {

    public static final int HARD = 0;
    public static final int CLAY = 1;
    public static final int GRASS = 2;
    public static final int INHARD = 3;

    private BoomMenuButton bmbMenu;

    public BoomMenuHome(BoomMenuButton bmbMenu) {
        this.bmbMenu = bmbMenu;
    }

    public void init(int seasonType, OnBMClickListener listener) {
        int colors[] = getSeasonTypeColor(seasonType);
//        bmbMenu.setNormalColor(colors[0]);
        // init boom menu
        int radius = bmbMenu.getContext().getResources().getDimensionPixelSize(R.dimen.boom_menu_btn_radius);
        int imageSize = bmbMenu.getContext().getResources().getDimensionPixelSize(R.dimen.boom_menu_image_size);
        bmbMenu.setButtonEnum(ButtonEnum.TextInsideCircle);
        bmbMenu.setButtonPlaceAlignmentEnum(ButtonPlaceAlignmentEnum.BR);
        bmbMenu.setButtonRightMargin(bmbMenu.getContext().getResources().getDimensionPixelSize(R.dimen.home_pop_menu_right));
        bmbMenu.setButtonBottomMargin(bmbMenu.getContext().getResources().getDimensionPixelSize(R.dimen.home_pop_menu_bottom));
        bmbMenu.setButtonVerticalMargin(bmbMenu.getContext().getResources().getDimensionPixelSize(R.dimen.boom_menu_btn_margin_ver));
        bmbMenu.setPiecePlaceEnum(PiecePlaceEnum.DOT_4_1);
        bmbMenu.setButtonPlaceEnum(ButtonPlaceEnum.Vertical);
        bmbMenu.addBuilder(new TextInsideCircleButton.Builder()
                .listener(listener)
                .buttonRadius(radius)
//                .normalColor(colors[0])
                .normalText(bmbMenu.getContext().getString(R.string.menu_save))
                .normalImageRes(R.drawable.ic_save_white_24dp)
        );
        bmbMenu.addBuilder(new TextInsideCircleButton.Builder()
                .listener(listener)
                .buttonRadius(radius)
//                .normalColor(colors[1])
                .normalText(bmbMenu.getContext().getString(R.string.menu_saveas))
                .normalImageRes(R.drawable.ic_inbox_white_24dp)
        );
        bmbMenu.addBuilder(new TextInsideCircleButton.Builder()
                .listener(listener)
                .buttonRadius(radius)
//                .normalColor(colors[2])
                .normalText(bmbMenu.getContext().getString(R.string.exit))
                .normalImageRes(R.drawable.ic_exit_to_app_white_24dp)
        );
        bmbMenu.addBuilder(new TextInsideCircleButton.Builder()
                .listener(listener)
                .buttonRadius(radius)
//                .normalColor(colors[3])
                .normalText(bmbMenu.getContext().getString(R.string.home_go_top))
                .normalImageRes(R.drawable.ic_arrow_upward_white_24dp)
        );

    }

    private int[] getSeasonTypeColor(int seasonType) {
        int[] colors = new int[4];
        switch (seasonType) {
            case HARD:
                colors[0] = bmbMenu.getContext().getResources().getColor(R.color.theme_hard1);
                colors[1] = bmbMenu.getContext().getResources().getColor(R.color.theme_hard2);
                colors[2] = bmbMenu.getContext().getResources().getColor(R.color.theme_hard3);
                colors[3] = bmbMenu.getContext().getResources().getColor(R.color.theme_hard4);
                break;
            case CLAY:
                colors[0] = bmbMenu.getContext().getResources().getColor(R.color.theme_clay1);
                colors[1] = bmbMenu.getContext().getResources().getColor(R.color.theme_clay2);
                colors[2] = bmbMenu.getContext().getResources().getColor(R.color.theme_clay3);
                colors[3] = bmbMenu.getContext().getResources().getColor(R.color.theme_clay4);
                break;
            case GRASS:
                colors[0] = bmbMenu.getContext().getResources().getColor(R.color.theme_grass1);
                colors[1] = bmbMenu.getContext().getResources().getColor(R.color.theme_grass2);
                colors[2] = bmbMenu.getContext().getResources().getColor(R.color.theme_grass3);
                colors[3] = bmbMenu.getContext().getResources().getColor(R.color.theme_grass4);
                break;
            case INHARD:
                colors[0] = bmbMenu.getContext().getResources().getColor(R.color.theme_inhard1);
                colors[1] = bmbMenu.getContext().getResources().getColor(R.color.theme_inhard2);
                colors[2] = bmbMenu.getContext().getResources().getColor(R.color.theme_inhard3);
                colors[3] = bmbMenu.getContext().getResources().getColor(R.color.theme_inhard4);
                break;
        }
        return colors;
    }
}
