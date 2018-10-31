package com.fxj.unifiednativeadplugin;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

public class UnifiedNativeadPlugin  {

  public static String PLUGIN_PREFIX = "unified_nativead_plugin";
  private static Registrar sRegistrar;

  public static void registerWith(Registrar registrar) {

//    final MethodChannel channel = new MethodChannel(registrar.messenger(), "unified_nativead_plugin");
//    channel.setMethodCallHandler(new UnifiedNativeadPlugin());
    sRegistrar = registrar;
//    registrar.platformViewRegistry().registerViewFactory(
//                    "com.fxj.platformview/unifiednativeadview", new UnifiedNativeAdFactory(registrar
//                            .messenger()));
  }

  public static void setsUnifiedNativeAdImplement(String viewType, MyUnifiedNativeAdView
          implement)  {
    sRegistrar.platformViewRegistry().registerViewFactory( viewType,
            new UnifiedNativeAdFactory(viewType, sRegistrar.messenger(), implement));
  }

}
