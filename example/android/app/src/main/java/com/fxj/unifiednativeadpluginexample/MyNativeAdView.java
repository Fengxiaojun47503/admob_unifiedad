package com.fxj.unifiednativeadpluginexample;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fxj.unifiednativeadplugin.UnifiedNativeAdPlatformView;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import io.flutter.plugin.common.BinaryMessenger;

public class MyNativeAdView extends UnifiedNativeAdPlatformView {
    private static final String TAG = MyNativeAdView.class.getSimpleName();

    public MyNativeAdView(Activity activity, Context context, BinaryMessenger messenger, int
            id, String viewType) {
        super(activity, context, messenger, id, viewType);
    }

    @Override
    protected String[] addTestDevices() {
        return new String[]{"3AB3178BC4608B46DEC46429AC12D365",
                "92B39F77CA52B82E1A77BCA2C8F2B8F0"};
    }

    @Override
    public UnifiedNativeAdView createUnifiedNativeAdView(Context context, BinaryMessenger
            messenger, int id, String viewType) {
        return (UnifiedNativeAdView) LayoutInflater.from(context).inflate(
                R.layout.my_ad_layout, null);
    }

    @Override
    public void populateAd(UnifiedNativeAd ad, UnifiedNativeAdView adView) {
        TextView headlineView = adView.findViewById(R.id.ad_headline);
        headlineView.setText(ad.getHeadline());
        adView.setHeadlineView(headlineView);
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media_view));
    }
}
