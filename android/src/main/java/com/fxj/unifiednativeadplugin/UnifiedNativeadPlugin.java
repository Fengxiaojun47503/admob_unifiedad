package com.fxj.unifiednativeadplugin;

import android.app.Activity;

import io.flutter.plugin.common.PluginRegistry.Registrar;

public class UnifiedNativeadPlugin {

    public static String PLUGIN_PREFIX = "unified_nativead_plugin";
    private static Registrar sRegistrar;

    public static void registerWith(Registrar registrar) {

//    final MethodChannel channel = new MethodChannel(registrar.messenger(),
// "unified_nativead_plugin");
//    channel.setMethodCallHandler(new UnifiedNativeadPlugin());
        sRegistrar = registrar;
//    registrar.platformViewRegistry().registerViewFactory(
//                    "com.fxj.platformview/unifiednativeadview", new UnifiedNativeAdFactory
// (registrar
//                            .messenger()));
    }

    public static void setUnifiedNativeAdImplement(
            Activity activity, String[] viewTypes, BaseUnifiedNativeAdViewFactory implement) {
        for (String viewType : viewTypes) {
            sRegistrar.platformViewRegistry().registerViewFactory(viewType,
                    new UnifiedNativeAdFactory(activity, viewType, sRegistrar.messenger(),
                            implement));
        }
    }

}
