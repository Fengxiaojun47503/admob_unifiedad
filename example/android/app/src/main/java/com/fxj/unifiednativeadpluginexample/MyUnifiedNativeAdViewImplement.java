package com.fxj.unifiednativeadpluginexample;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fxj.unifiednativeadplugin.MyUnifiedNativeAdView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.platform.PlatformView;

public class MyUnifiedNativeAdViewImplement extends MyUnifiedNativeAdView {
    private UnifiedNativeAdView mAdView;

    MyUnifiedNativeAdViewImplement(Context context){
        super(context);
    }

    @Override
    protected String[] addTestDevices() {
        return new String[]{"3AB3178BC4608B46DEC46429AC12D365",
                "92B39F77CA52B82E1A77BCA2C8F2B8F0"};
    }

    @Override
    public PlatformView createUnifiedNativeAdView(Context context, BinaryMessenger messenger,
                                                  int id, String viewType) {
            mAdView = (UnifiedNativeAdView)LayoutInflater.from(context).inflate(R.layout
                    .my_ad_layout, null);
        return this;
    }

    @Override
    public UnifiedNativeAdView getUnifiedNativeAdView() {
        return mAdView;
    }

    @Override
    public void populateAd(UnifiedNativeAd ad, UnifiedNativeAdView adView) {
        Log.d("fxj", "populateAd ....");
        TextView tv = adView.findViewById(R.id.my_tv);
        tv.setText(ad.getHeadline());
        Log.d("fxj", "getHeadline =>"+ad.getHeadline());
        adView.setHeadlineView(tv);
    }

    @Override
    public void dispose() {
        mAdView.destroy();
    }
}
