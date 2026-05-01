package com.conectatec.ui.dashboard;

import android.content.Context;
import android.content.res.Configuration;

import androidx.viewpager2.widget.ViewPager2;

public class DashboardUtils {

    public static boolean esTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static int columnasKpi(Context context) {
        int dpWidth = (int) (context.getResources().getDisplayMetrics().widthPixels
                / context.getResources().getDisplayMetrics().density);
        if (dpWidth >= 600) return 4;
        return 2;
    }

    public static void aplicarConfiguracionTablet(ViewPager2 vp, Context context) {
        if (esTablet(context)) {
            vp.setOffscreenPageLimit(2);
        }
    }
}
