package com.fxj.unifiednativeadplugin;

import android.content.Context;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.platform.PlatformView;

public abstract class BaseUnifiedNativeAdViewFactory {
    private static final String TAG = BaseUnifiedNativeAdViewFactory.class.getSimpleName();

    public PlatformView createView(Context context, BinaryMessenger messenger, int id,
                                   String viewType) {
        PlatformView view = createUnifiedNativeAdView(context, messenger, id, viewType);
        if (view == null) {
            throw new RuntimeException("can not create PlatformView for type: " + viewType);
        }
        return view;
    }

    public abstract UnifiedNativeAdPlatformView createUnifiedNativeAdView(Context context,
                                                                          BinaryMessenger
            messenger, int id, String viewType);

}