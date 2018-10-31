import 'dart:async';

import 'package:flutter/services.dart';

class UnifiedNativeadPlugin {
  static const MethodChannel _channel =
      const MethodChannel('unified_nativead_plugin');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
