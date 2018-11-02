import 'package:flutter/material.dart';

import 'package:unified_nativead_plugin/unified_nativead_view.dart';

void main() => runApp(new MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => new _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      home: new Scaffold(
        appBar: new AppBar(
          title: const Text('UnifiedNativeAd example app'),
        ),
        body: new Center(
          child: UnifiedNativeAdView(
              adUnitId: UnifiedNativeAdView.TEST_AD_ID,
              height: 350.0,
              viewType: "native1",
              debugAd: true,
              placeHolder: Container(
                color: Colors.greenAccent,
              ),
            )
        ),
      ),
    );
  }
}
