package com.codetroopers.betterpickers.sample;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

public class SampleApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		JodaTimeAndroid.init(this);
	}
}
