package com.fxj.unifiednativeadpluginexample;

import android.content.Context;

import com.fxj.unifiednativeadplugin.BaseUnifiedNativeAdViewFactory;
import com.fxj.unifiednativeadplugin.UnifiedNativeAdPlatformView;

import io.flutter.plugin.common.BinaryMessenger;

public class MyUnifiedNativeAdViewImplement extends BaseUnifiedNativeAdViewFactory {
    @Override
    public UnifiedNativeAdPlatformView createUnifiedNativeAdView(Context context, BinaryMessenger
            messenger, int id, String viewType) {
        switch (viewType){
            case "native1":
                return new MyNativeAdView(context, messenger, id, viewType);
        }
        return null;
    }
}
