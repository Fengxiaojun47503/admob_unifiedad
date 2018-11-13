package com.fxj.unifiednativeadpluginexample;

import android.os.Bundle;

import com.fxj.unifiednativeadplugin.UnifiedNativeadPlugin;
import com.google.android.gms.ads.MobileAds;

import io.flutter.app.FlutterActivity;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {
    private static final String ADMOB_TEST_APP_ID = "ca-app-pub-3940256099942544~3347511713";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MobileAds.initialize(this, ADMOB_TEST_APP_ID);
        super.onCreate(savedInstanceState);
        GeneratedPluginRegistrant.registerWith(this);
        UnifiedNativeadPlugin.setUnifiedNativeAdImplement(
                this,
                new String[]{"native1"},
                new MyUnifiedNativeAdViewImplement());
    }
}
