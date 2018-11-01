package com.fxj.unifiednativeadplugin;

import android.content.Context;
import android.util.Log;
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

public abstract class UnifiedNativeAdPlatformView implements PlatformView, MethodChannel
        .MethodCallHandler {
    private static final String TAG = UnifiedNativeAdPlatformView.class.getSimpleName();
    private Context mContext;
    private String[] mTestDevices;
    private MethodChannel mChannel;
    private AdLoader mAdLoader;
    private UnifiedNativeAd mNativeAd;
    private AdStatus mAdStatus;
    private int mViewId;
    private UnifiedNativeAdView mAdView;

    public UnifiedNativeAdPlatformView(Context context, BinaryMessenger messenger, int
            id, String viewType) {
        mContext = context;
        mTestDevices = addTestDevices();
        mViewId = id;
        mAdView = createUnifiedNativeAdView(context, messenger, id, viewType);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "createView id: " + mViewId);
        }
        mChannel = new MethodChannel(messenger, PLUGIN_PREFIX + "_" + viewType + "_" + id);
        mChannel.setMethodCallHandler(this);
    }

    /**
     * if you want to load ad as a test devices,
     * you can override this method return test devices
     *
     * @return
     */
    protected String[] addTestDevices() {
        return null;
    }

    @Override
    public View getView() {
        return mAdView;
    }

    @Override
    public void dispose() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "dispose view id: " + mViewId);
        }
        if (mAdView != null) {
            try {
                mAdView.destroy();
            } catch (Exception e) {
            }
        }
        mChannel = null;
        mAdView = null;
        mAdLoader = null;
        mContext = null;
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
                                    if(mChannel == null){
                                        Log.w(TAG, "adview disposed, so skip show ad.");
                                        return;
                                    }
                                    mNativeAd = unifiedNativeAd;
                                    mChannel.invokeMethod("onAdLoaded", null);
                                    if (mAdStatus == AdStatus.PENDING) {

                                    }
                                    populateAd(mNativeAd, mAdView);
                                    mAdView.setNativeAd(mNativeAd);
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
                    if (BuildConfig.DEBUG && mTestDevices != null) {
                        for (String testDevice : mTestDevices) {
                            adRequest.addTestDevice(testDevice);
                        }
                    }
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "start to load ad, view id: " + mViewId);
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

    public abstract UnifiedNativeAdView createUnifiedNativeAdView(Context context, BinaryMessenger
            messenger, int id, String viewType);

    public abstract void populateAd(UnifiedNativeAd ad, UnifiedNativeAdView adView);
}
