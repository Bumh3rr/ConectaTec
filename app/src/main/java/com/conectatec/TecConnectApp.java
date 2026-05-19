package com.conectatec;

import android.app.Application;
import com.conectatec.ui.common.ThemePreferenceManager;
import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class TecConnectApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ThemePreferenceManager.applyTheme(this);
    }
}