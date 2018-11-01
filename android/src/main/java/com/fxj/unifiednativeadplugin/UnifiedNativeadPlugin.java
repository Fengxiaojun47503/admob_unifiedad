package com.fxj.unifiednativeadplugin;

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

  public static void setUnifiedNativeAdImplement(String viewType, BaseUnifiedNativeAdViewFactory
          implement)  {
    sRegistrar.platformViewRegistry().registerViewFactory( viewType,
            new UnifiedNativeAdFactory(viewType, sRegistrar.messenger(), implement));
  }

}
