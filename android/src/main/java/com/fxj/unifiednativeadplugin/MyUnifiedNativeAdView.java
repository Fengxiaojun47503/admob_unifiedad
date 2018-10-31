package com.fxj.unifiednativeadplugin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.platform.PlatformView;

import static com.fxj.unifiednativeadplugin.UnifiedNativeadPlugin.PLUGIN_PREFIX;

public abstract class MyUnifiedNativeAdView implements PlatformView, MethodChannel
        .MethodCallHandler {
    private static final String TAG = MyUnifiedNativeAdView.class.getSimpleName();
    private Context mContext;
    private String[] mTestDevices;
    private UnifiedNativeAdView mNativeAdView;
    private AdLoader mAdLoader;
    private UnifiedNativeAd mNativeAd;
    private AdStatus mAdStatus;
    private MethodChannel mChannel;
    private int mViewId;

    public MyUnifiedNativeAdView(Context context){
        mContext = context;
        mTestDevices = addTestDevices();
    }

    protected String[] addTestDevices(){
        return null;
    }

    @Override
    public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {
        switch (methodCall.method) {
            case "loadAd":
                if (mAdLoader == null) {
                    AdLoader.Builder builder = new AdLoader.Builder(mContext, (String)
                            methodCall.argument("adUnitId"))
                            .forUnifiedNativeAd(new UnifiedNativeAd
                                    .OnUnifiedNativeAdLoadedListener() {

                                @Override
                                public void onUnifiedNativeAdLoaded(UnifiedNativeAd
                                                                            unifiedNativeAd) {
                                    if (BuildConfig.DEBUG) {
                                        Log.d(TAG, "onUnifiedNativeAdLoaded, id: " + mViewId);
                                    }
                                    mNativeAd = unifiedNativeAd;
                                    mChannel.invokeMethod("onAdLoaded", null);
                                    if (mAdStatus == AdStatus.PENDING) {

                                    }
                                    populateAd(mNativeAd, getView());
                                    getView().setNativeAd(mNativeAd);
                                }
                            }).withAdListener(new AdListener() {
                                @Override
                                public void onAdLoaded() {
                                    if (BuildConfig.DEBUG) {
                                        Log.d(TAG, "onAdLoaded, id: " + mViewId);
                                    }
                                }

                                @Override
                                public void onAdFailedToLoad(int errorCode) {
                                    if (BuildConfig.DEBUG) {
                                        Log.d(TAG, "onAdFailedToLoad, id: " + mViewId);
                                    }
                                }

                                @Override
                                public void onAdClicked() {
                                    if (BuildConfig.DEBUG) {
                                        Log.d(TAG, "onAdClicked, id: " + mViewId);
                                    }
                                }

                                @Override
                                public void onAdOpened() {
                                    if (BuildConfig.DEBUG) {
                                        Log.d(TAG, "onAdOpened, id: " + mViewId);
                                    }
                                }

                                @Override
                                public void onAdImpression() {
                                    if (BuildConfig.DEBUG) {
                                        Log.d(TAG, "onAdImpression, id: " + mViewId);
                                    }
                                }

                                @Override
                                public void onAdClosed() {
                                    if (BuildConfig.DEBUG) {
                                        Log.d(TAG, "onAdClosed, id: " + mViewId);
                                    }
                                }

                            })
                            .withNativeAdOptions(new NativeAdOptions.Builder()
                                    // Methods in the NativeAdOptions.Builder class can be
                                    // used here to specify individual options settings.
                                    .build());
                    mAdLoader = builder.build();
                    AdRequest.Builder adRequest = new AdRequest.Builder();
                    if (BuildConfig.DEBUG && mTestDevices !=null) {
                            for(String testDevice: mTestDevices){
                                adRequest.addTestDevice(testDevice);
                            }
                    }
                    mAdLoader.loadAd(adRequest.build());
                }
                break;
            case "showAd":

                break;
            default:
                result.notImplemented();
        }
    }

    public PlatformView createView(Context context, BinaryMessenger messenger, int id,
                                   String viewType) {
        mViewId = id;
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "createView id: " + mViewId);
        }
        mChannel = new MethodChannel(messenger, PLUGIN_PREFIX + "_" + viewType + "_" + id);
        mChannel.setMethodCallHandler(this);
        return createUnifiedNativeAdView(context, messenger, id, viewType);
    }

    @Override
    public UnifiedNativeAdView getView() {
        return getUnifiedNativeAdView();
    }

    abstract public PlatformView createUnifiedNativeAdView(Context context, BinaryMessenger
            messenger, int id, String viewType);

    abstract public UnifiedNativeAdView getUnifiedNativeAdView();
    abstract public void populateAd(UnifiedNativeAd ad, UnifiedNativeAdView adView);
}