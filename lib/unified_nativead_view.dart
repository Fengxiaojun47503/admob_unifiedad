import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

const _kIsDebug = !bool.fromEnvironment("dart.vm.product");
enum MobileAdEvent {
  loading,
  onUnifiedNativeAdLoaded,
  loaded,
  failedToLoad,
  clicked,
  impression,
  opened,
  leftApplication,
  closed,
}

class UnifiedNativeAdView extends StatefulWidget {
  static const TEST_AD_ID = "ca-app-pub-3940256099942544/2247696110";
  static const Map<String, MobileAdEvent> _methodToMobileAdEvent =
      <String, MobileAdEvent>{
    'onUnifiedNativeAdLoaded': MobileAdEvent.onUnifiedNativeAdLoaded,
    'onAdLoaded': MobileAdEvent.loaded,
    'onAdFailedToLoad': MobileAdEvent.failedToLoad,
    'onAdClicked': MobileAdEvent.clicked,
    'onAdImpression': MobileAdEvent.impression,
    'onAdOpened': MobileAdEvent.opened,
    'onAdLeftApplication': MobileAdEvent.leftApplication,
    'onAdClosed': MobileAdEvent.closed,
  };

  UnifiedNativeAdView({
    @required this.adUnitId,
    this.width: double.infinity,
    this.height: double.infinity,
    @required this.viewType,
    this.debugAd: false,
    this.margin,
    this.padding,
    this.placeHolder,
  });
  final String adUnitId;
  final double width, height;
  final String viewType;
  final bool debugAd;
  final EdgeInsetsGeometry margin;
  final EdgeInsetsGeometry padding;
  final Widget placeHolder;

  @override
  State<StatefulWidget> createState() {
    return _UnifiedNativeAdViewState();
  }
}

class _UnifiedNativeAdViewState extends State<UnifiedNativeAdView> {
  MethodChannel _channel;
  bool _created = false;
  String adEvent = "loading";
  bool _adEventOccur = false;
  bool _isAdShowing = false;
  int _id;

  @override
  void initState() {
    super.initState();
  }

  @override
  void dispose() {
    super.dispose();
    debugPrint('dispose PlatformView $_id');
  }

  @override
  Widget build(BuildContext context) {
    if (defaultTargetPlatform != TargetPlatform.android) {
      return Container(
        width: 0.0,
        height: 0.0,
      );
    }
    bool shouldShowNow = _created && _adEventOccur;
    debugPrint(
        'build platform view ... _created? $_created,  _isAdShowing=> $_isAdShowing');
    return Offstage(
      offstage: !shouldShowNow && widget.placeHolder == null,
      child: Container(
          width: widget.width,
          height: widget.height,
          margin: widget.margin,
          padding: widget.padding,
          child: Stack(
            children: <Widget>[
              AndroidView(
                viewType: widget.viewType,
                onPlatformViewCreated: _onPlatformViewCreated,
              ),
              Offstage(
                offstage: shouldShowNow || widget.placeHolder == null,
                child: widget.placeHolder,
              ),
              Offstage(
                  offstage: !widget.debugAd || _isAdShowing,
                  child: Container(
                    color: Colors.white.withAlpha(50),
                    child: Text(adEvent),
                  )),
            ],
          )),
    );
  }

  void _onPlatformViewCreated(int id) {
    if (_kIsDebug) {
      debugPrint('_onPlatformViewCreated id: $id');
    }
    setState(() {
      _id = id;
      _created = true;
    });
    _channel = MethodChannel('unified_nativead_plugin_${widget.viewType}_$id');
    _channel.setMethodCallHandler(_handleMessages);
    _channel
        .invokeMethod("loadAd", <String, dynamic>{"adUnitId": widget.adUnitId});
  }

  Future<Null> _handleMessages(MethodCall call) async {
    setState(() {
      _adEventOccur = true;
      adEvent = call.method;
      if (_kIsDebug) {
        debugPrint('hadle message $adEvent, view id: $_id');
      }
      if (UnifiedNativeAdView._methodToMobileAdEvent[adEvent] ==
          MobileAdEvent.impression) {
        _isAdShowing = true;
      } else if (UnifiedNativeAdView._methodToMobileAdEvent[adEvent] ==
          MobileAdEvent.failedToLoad) {
        adEvent = '$adEvent errorCode: ${call.arguments['errorCode']}';
      }
    });
  }
}
