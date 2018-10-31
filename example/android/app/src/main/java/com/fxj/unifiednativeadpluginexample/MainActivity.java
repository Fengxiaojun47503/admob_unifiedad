package com.fxj.unifiednativeadpluginexample;

import android.os.Bundle;

import com.fxj.unifiednativeadplugin.UnifiedNativeadPlugin;
import com.google.android.gms.ads.MobileAds;

import io.flutter.app.FlutterActivity;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    MobileAds.initialize(this, "ca-app-pub-6459489448442434~5879388858");
    super.onCreate(savedInstanceState);
    GeneratedPluginRegistrant.registerWith(this);
    UnifiedNativeadPlugin.setsUnifiedNativeAdImplement("native1", new
            MyUnifiedNativeAdViewImplement(this));
  }
}
