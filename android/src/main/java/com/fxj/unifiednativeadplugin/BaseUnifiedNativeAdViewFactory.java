package com.fxj.unifiednativeadplugin;

import android.app.Activity;
import android.content.Context;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.platform.PlatformView;

public abstract class BaseUnifiedNativeAdViewFactory {
    private static final String TAG = BaseUnifiedNativeAdViewFactory.class.getSimpleName();

    public PlatformView createView(Activity activity, Context context, BinaryMessenger messenger,
                                   int id, String viewType) {
        PlatformView view = createUnifiedNativeAdView(activity, context, messenger, id, viewType);
        if (view == null) {
            throw new RuntimeException("can not create PlatformView for type: " + viewType);
        }
        return view;
    }

    public abstract UnifiedNativeAdPlatformView createUnifiedNativeAdView(
            Activity activity, Context context, BinaryMessenger messenger, int id, String viewType);

}