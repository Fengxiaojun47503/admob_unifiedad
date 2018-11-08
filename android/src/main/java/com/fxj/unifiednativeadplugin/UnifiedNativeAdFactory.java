package com.fxj.unifiednativeadplugin;

import android.app.Activity;
import android.content.Context;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

public class UnifiedNativeAdFactory extends PlatformViewFactory {
    private final Activity mActivitty;
    private final BinaryMessenger messenger;
    private BaseUnifiedNativeAdViewFactory mPlatformViewImplement;
    private String viewType;

    public UnifiedNativeAdFactory(Activity activity, String viewType, BinaryMessenger messenger,
                                  BaseUnifiedNativeAdViewFactory nativeAdView) {
        super(StandardMessageCodec.INSTANCE);
        mActivitty = activity;
        this.viewType = viewType;
        this.messenger = messenger;
        mPlatformViewImplement = nativeAdView;
    }

    @Override
    public PlatformView create(Context context, int i, Object o) {
        return mPlatformViewImplement.createView(mActivitty, context, messenger, i, viewType);
    }
}