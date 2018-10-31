import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class UnifiedNativeAdView extends StatefulWidget {
  static const TEST_AD_ID = "ca-app-pub-3940256099942544/2247696110";

  UnifiedNativeAdView(
      {@required this.adUnitId,
      this.width:double.infinity,
      this.height:double.infinity,
      @required this.viewType,
      this.debugAd:false,
      });
  final String adUnitId;
  final double width, height;
  final String viewType;
  final bool debugAd;

  @override
  State<StatefulWidget> createState() {
    return _UnifiedNativeAdViewState();
  }
}

class _UnifiedNativeAdViewState extends State<UnifiedNativeAdView> {
  MethodChannel _channel;
  bool _created = false;

  @override
  void initState() {
    super.initState();
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    if (defaultTargetPlatform != TargetPlatform.android) {
      return Container(
        width: 0.0,
        height: 0.0,
      );
    }
    debugPrint('build platform view ... _created? $_created');
    return Offstage(
      offstage: !_created,
      child: Container(
        width: widget.width,
        height: widget.height,
        child: AndroidView(
          viewType: widget.viewType,
          onPlatformViewCreated: _onPlatformViewCreated,
        ),
      ),
    );
  }

  void _onPlatformViewCreated(int id) {
    setState(() {
      _created = true;
    });
    _channel = MethodChannel('unified_nativead_plugin_${widget.viewType}_$id');
    _channel.setMethodCallHandler(_handleMessages);
    _channel
        .invokeMethod("loadAd", <String, dynamic>{"adUnitId": widget.adUnitId});
  }

  Future<Null> _handleMessages(MethodCall call) async {
    switch (call.method) {
      case "onAdLoaded":
        break;
    }
  }
}
