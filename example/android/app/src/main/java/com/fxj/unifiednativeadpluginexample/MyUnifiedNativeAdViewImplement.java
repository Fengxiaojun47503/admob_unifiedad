package com.fxj.unifiednativeadpluginexample;

import android.app.Activity;
import android.content.Context;

import com.fxj.unifiednativeadplugin.BaseUnifiedNativeAdViewFactory;
import com.fxj.unifiednativeadplugin.UnifiedNativeAdPlatformView;

import io.flutter.plugin.common.BinaryMessenger;

public class MyUnifiedNativeAdViewImplement extends BaseUnifiedNativeAdViewFactory {
    @Override
    public UnifiedNativeAdPlatformView createUnifiedNativeAdView(
            Activity activity, Context context, BinaryMessenger messenger, int id, String
            viewType) {
        switch (viewType) {
            case "native1":
                return new MyNativeAdView(activity, context, messenger, id, viewType);
        }
        return null;
    }
}
