package com.fxj.unifiednativeadplugin;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import java.util.HashMap;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.platform.PlatformView;

import static com.fxj.unifiednativeadplugin.UnifiedNativeadPlugin.PLUGIN_PREFIX;

public abstract class UnifiedNativeAdPlatformView implements PlatformView, MethodChannel
        .MethodCallHandler {
    private static final String TAG = UnifiedNativeAdPlatformView.class.getSimpleName();
    protected Activity mActivity;
    protected Context mContext;
    protected String[] mTestDevices;
    protected MethodChannel mChannel;
    protected AdLoader mAdLoader;
    protected UnifiedNativeAd mNativeAd;
    protected AdStatus mAdStatus;
    protected int mViewId;
    protected View mAdView;

    public UnifiedNativeAdPlatformView(Activity activity, Context context, BinaryMessenger
            messenger, int id, String viewType) {
        mActivity = activity;
        mContext = context;
        mTestDevices = addTestDevices();
        mViewId = id;
        mAdView = createUnifiedNativeAdView(context, messenger, id, viewType);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "createView id: " + mViewId);
        }
        String channelName = PLUGIN_PREFIX + "_" + viewType + "_" + id;
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "register channel =>" + channelName);
        }
        mChannel = new MethodChannel(messenger, channelName);
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
                if (mAdView instanceof UnifiedNativeAdView)
                    ((UnifiedNativeAdView) mAdView).destroy();
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
                                    if (!onAdEvent("onUnifiedNativeAdLoaded")) {
                                        Log.w(TAG, "adview disposed, so skip show ad. id: " +
                                                mViewId);
                                        return;
                                    }
                                    if (!(mAdView instanceof UnifiedNativeAdView)) {
                                        throw new RuntimeException(
                                                "createUnifiedNativeAdView must return " +
                                                        "UnifiedNativeAdView");
                                    }
                                    mNativeAd = unifiedNativeAd;
                                    populateAd(mNativeAd, (UnifiedNativeAdView) mAdView);
                                    ((UnifiedNativeAdView) mAdView).setNativeAd(mNativeAd);
                                }
                            }).withAdListener(new AdListener() {
                                @Override
                                public void onAdLoaded() {
                                    onAdEvent("onAdLoaded");
                                }

                                @Override
                                public void onAdFailedToLoad(int errorCode) {
                                    HashMap<String, Object> parameters = new HashMap<>();
                                    parameters.put("errorCode", errorCode);
                                    onAdEvent("onAdFailedToLoad", parameters);
                                }

                                @Override
                                public void onAdClicked() {
                                    onAdEvent("onAdClicked");
                                }

                                @Override
                                public void onAdOpened() {
                                    onAdEvent("onAdOpened");
                                }

                                @Override
                                public void onAdImpression() {
                                    onAdEvent("onAdImpression");
                                }

                                @Override
                                public void onAdClosed() {
                                    onAdEvent("onAdClosed");
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

    private boolean onAdEvent(String event) {
        return onAdEvent(event, null);
    }

    private boolean onAdEvent(String event, HashMap<String, Object> parameters) {
        if (mChannel != null) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", mViewId);
            if (parameters != null) {
                map.putAll(parameters);
            }
            if (BuildConfig.DEBUG) {
                Log.d(TAG, event + ", id: " + mViewId + ", parameters: " + map);
            }
            mChannel.invokeMethod(event, map);
            return true;
        }
        return false;
    }

    public abstract View createUnifiedNativeAdView(Context context, BinaryMessenger
            messenger, int id, String viewType);

    public abstract void populateAd(UnifiedNativeAd ad, UnifiedNativeAdView adView);
}
