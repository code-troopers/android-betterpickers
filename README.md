[![Maven Central](https://img.shields.io/maven-central/v/com.code-troopers.betterpickers/library.svg?style=flat)](https://repo1.maven.org/maven2/com/code-troopers/betterpickers/library/)
[![API](https://img.shields.io/badge/API-9%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=9)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Android%20Betterpickers-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/118)

[![Built With Cloudbees](https://www.cloudbees.com/sites/default/files/styles/large/public/Button-Built-on-CB-1.png?itok=3Tnkun-C)](https://codetroopers.ci.cloudbees.com/job/betterpickers-master/)


<img src="https://raw.githubusercontent.com/code-troopers/android-betterpickers/master/sample/imagery/screenshot_calendar_date.png" width="120">
<img src="https://raw.githubusercontent.com/code-troopers/android-betterpickers/master/sample/imagery/screenshot_radial_time.png" width="120">
<img src="https://raw.githubusercontent.com/code-troopers/android-betterpickers/master/sample/imagery/screenshot_recurrence.png" width="120">
<img src="https://raw.githubusercontent.com/code-troopers/android-betterpickers/master/sample/imagery/screenshot_time_zone.png" width="120">

<img src="https://raw.githubusercontent.com/code-troopers/android-betterpickers/master/sample/imagery/screenshot_date.png" width="120">
<img src="https://raw.githubusercontent.com/code-troopers/android-betterpickers/master/sample/imagery/screenshot_expiration.png" width="120">
<img src="https://raw.githubusercontent.com/code-troopers/android-betterpickers/master/sample/imagery/screenshot_hms.png" width="120">
<img src="https://raw.githubusercontent.com/code-troopers/android-betterpickers/master/sample/imagery/screenshot_number.png" width="120">
<img src="https://raw.githubusercontent.com/code-troopers/android-betterpickers/master/sample/imagery/screenshot_time.png" width="120">


DialogFragments modeled after the AOSP Clock and Calendar apps to improve UX for picking time, date, numbers, and other things.

Try out the sample application on [Google Play][6].

<a href="https://play.google.com/store/apps/details?id=com.codetroopers.betterpickersapp">
  <img alt="BetterPickers Samples on Google Play"
         src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" width="140" />
</a>

Including in Your Project
=========================
### Gradle

```groovy
compile 'com.code-troopers.betterpickers:library:3.1.0'
```

### Maven

```xml
<dependency>
  <groupId>com.code-troopers.betterpickers</groupId>
  <artifactId>library</artifactId>
  <version>3.1.0</version>
  <type>aar</type>
</dependency>
```

Usage
=====

*For a working implementation of this project see the `sample/` folder.*

### Calendar Date Picker

```java
button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                .setOnDateSetListener(SampleCalendarDateBasicUsage.this)
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setPreselectedDate(towDaysAgo.getYear(), towDaysAgo.getMonthOfYear() - 1, towDaysAgo.getDayOfMonth())
                .setDateRange(minDate, null)
                .setDoneText("Yay")
                .setCancelText("Nop")
                .setThemeDark(true);
        cdp.show(getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);
    }
});
```

### Radial Time Picker

```java
button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener(SampleRadialTimeBasicUsage.this)
                .setStartTime(10, 10)
                .setDoneText("Yay")
                .setCancelText("Nop")
                .setThemeDark(true);
        rtpd.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER);
    }
});
```

### Recurrence Picker

```java
button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        FragmentManager fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        Time time = new Time();
        time.setToNow();
        bundle.putLong(RecurrencePickerDialogFragment.BUNDLE_START_TIME_MILLIS, time.toMillis(false));
        bundle.putString(RecurrencePickerDialogFragment.BUNDLE_TIME_ZONE, time.timezone);
        bundle.putString(RecurrencePickerDialogFragment.BUNDLE_RRULE, mRrule);
        bundle.putBoolean(RecurrencePickerDialogFragment.BUNDLE_HIDE_SWITCH_BUTTON, true);

        RecurrencePickerDialogFragment rpd = new RecurrencePickerDialogFragment();
        rpd.setArguments(bundle);
        rpd.setOnRecurrenceSetListener(SampleRecurrenceBasicUsage.this);
        rpd.show(fm, FRAG_TAG_RECUR_PICKER);
    }
});
```

### Timezone Picker

```java
button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        FragmentManager fm = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        Time time = new Time();
        time.setToNow();
        bundle.putLong(TimeZonePickerDialogFragment.BUNDLE_START_TIME_MILLIS, time.toMillis(false));
        bundle.putString(TimeZonePickerDialogFragment.BUNDLE_TIME_ZONE, time.timezone);
        bundle.putString(RecurrencePickerDialogFragment.BUNDLE_RRULE, mRrule);

        TimeZonePickerDialogFragment tzpd = new TimeZonePickerDialogFragment();
        tzpd.setArguments(bundle);
        tzpd.setOnTimeZoneSetListener(SampleTimeZoneBasicUsage.this);
        tzpd.show(fm, FRAG_TAG_TIME_ZONE_PICKER);
    }
});
```

### Date Picker

```java
button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        DatePickerBuilder dpb = new DatePickerBuilder()
                .setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment)
                .setYearOptional(true);
        dpb.show();
    }
});
```

### Expiration Picker

```java
button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        ExpirationPickerBuilder epb = new ExpirationPickerBuilder()
                  .setFragmentManager(getSupportFragmentManager())
                  .setStyleResId(R.style.BetterPickersDialogFragment)
                  .setMinYear(2000);
        epb.show();
    }
});
```

### HMS Picker

```java
button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        HmsPickerBuilder hpb = new HmsPickerBuilder()
                .setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment);
        hpb.show();
    }
});
```

### Number Picker

```java
button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        NumberPickerBuilder npb = new NumberPickerBuilder()
                .setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment)
                .setLabelText("LBS.");
        npb.show();
}
});
```

### Time Picker

```java
button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        TimePickerBuilder tpb = new TimePickerBuilder()
                .setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment);
        tpb.show();
    }
});
```

Theming
=======
*For a demonstration of theming, see the `sample/` folder.*

### Calendar Date Picker / Radial Time Picker

  1. Use attributes that allow you to customize pickers

        bpHeaderBackgroundColor     ::
        bpHeaderUnselectedTextColor ::
        bpHeaderSelectedTextColor   ::
        bpBodyBackgroundColor       ::
        bpBodySelectedTextColor     ::
        bpBodyUnselectedTextColor   ::
        bpButtonsBackgroundColor    ::
        bpButtonsTextColor          ::
        -- Calendar Date Picker
        bpPreHeaderBackgroundColor  ::
        bpDisabledDayTextColor      ::
        -- Radial Time Picker
        bpRadialBackgroundColor     ::
        bpRadialTextColor           ::
        bpRadialPointerColor        ::
        bpAmPmCircleColor           ::

  2. Create your own custom theme in `styles.xml`:

    ```xml
    <style name="MyCustomBetterPickersDialogs" parent="BetterPickersRadialTimePickerDialog.PrimaryColor">
        <item name="bpPreHeaderBackgroundColor">@color/holo_red_dark</item>
        <item name="bpHeaderBackgroundColor">@color/holo_red_light</item>
        <item name="bpHeaderSelectedTextColor">@color/holo_orange_dark</item>
        <item name="bpHeaderUnselectedTextColor">@android:color/white</item>

        <item name="bpBodyBackgroundColor">@color/holo_blue_dark</item>
        <item name="bpBodySelectedTextColor">@color/holo_orange_dark</item>
        <item name="bpBodyUnselectedTextColor">@android:color/white</item>

        <item name="bpRadialBackgroundColor">@color/holo_orange_dark</item>
        <item name="bpRadialTextColor">@color/holo_purple</item>
        <item name="bpRadialPointerColor">@android:color/black</item>

        <item name="bpButtonsBackgroundColor">@color/holo_green_dark</item>
        <item name="bpButtonsTextColor">@color/holo_orange_dark</item>
    </style>
    ```

  3. Instantiate your `DialogFragment` using your custom theme:

  ```java
  RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
         .setOnTimeSetListener(SampleRadialTimeThemeCustom.this)
         .setThemeCustom(R.style.MyCustomBetterPickersDialogs);
  rtpd.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER);
  ```
  4. Result

  <img src="https://raw.githubusercontent.com/code-troopers/android-betterpickers/master/sample/imagery/screenshot_calendar_date_customized.png" width="120">
  <img src="https://raw.githubusercontent.com/code-troopers/android-betterpickers/master/sample/imagery/screenshot_radial_time_customized.png" width="120">

### Date Picker / Expiration Picker / HMS Picker / Number Picker / Time Picker


  1. You can use your own themes if you'd like to change certain attributes.  BetterPickers currently allows for customization of the following attributes:

        bpDialogBackground       :: the drawable (preferably a 9-patch) used as a window background for the DialogFragment
        bpTextColor              :: the color (optionally state list) for all text in the DialogFragment
        bpDeleteIcon             :: the drawable (optionally state list) for the delete button
        bpCheckIcon              :: the drawable (optionally state list) for the check button in the DateDialogPicker
        bpKeyBackground          :: the drawable (optionally state list) for the keyboard buttons
        bpButtonBackground       :: the drawable (optionally state list) for the Set, Cancel, and Delete buttons
        bpDividerColor           :: the color used for the DialogFragment dividers
        bpKeyboardIndicatorColor :: the color used for the ViewPagerIndicator on the DateDialogPicker

  2. Create your own custom theme in `styles.xml`:

  ```xml
  <style name="MyCustomBetterPickerTheme">
      <item name="bpDialogBackground">@drawable/custom_dialog_background</item>
      <item name="bpTextColor">@color/custom_text_color</item>
      <item name="bpDeleteIcon">@drawable/ic_backspace_custom</item>
      <item name="bpCheckIcon">@drawable/ic_check_custom</item>
      <item name="bpKeyBackground">@drawable/key_background_custom</item>
      <item name="bpButtonBackground">@drawable/button_background_custom</item>
      <item name="bpDividerColor">@color/custom_divider_color</item>
      <item name="bpKeyboardIndicatorColor">@color/custom_keyboard_indicator_color</item>
  </style>
  ```

  3. Instantiate your `DialogFragment` using your custom theme:

  ```java
  DatePickerBuilder dpb = new DatePickerBuilder()
      .setFragmentManager(getSupportFragmentManager())
      .setStyleResId(R.style.MyCustomBetterPickerTheme);
  dpb.show();
  ```

Actionbarsherlock compatibility
===============================
If you use actionbarsherlock which is not compatible with appcompat-v7 you can use the latest version of the library on the 1.x.x branch.

You can view the readme [here](README_1.6.0.md)

ChangeLog
=========
Change log file is available [here](CHANGE_LOG.md)

Contribution
============

### Pull requests are welcome!

Feel free to contribute to BetterPickers.

If you've fixed a bug or have a feature you've added, just create a pull request.

If you've found a bug, want a new feature, or have other questions, [file an issue][10]. We will try to answer as soon as possible.


### Applications using BetterPickers

Please send a pull request if you would like to be added here.

Icon | Application
------------ | -------------
<img src="https://lh6.ggpht.com/wG3RSgReZcIcKsqFfsKSibR-j1UYfOekNmtY8x0n0mjWJT84U2V3l2lh3TmxeXfJis0z=w300" width="48" height="48" /> | [Trello]
<img src="https://lh3.ggpht.com/OuJF91ba0PiNItxw_zAqwjarenly_LiaeaPJQOHBrVWPsGzydjUZ1ANp1wVDpU4cLsE=w300" width="48" height="48" /> | [Navig'Tours]
<img src="https://lh3.googleusercontent.com/wgzBdz0-R999n1uo6qKJmIzPCt3ShM2lSM6oHmpz9HQKHhbuU0hGYwYhTJx_2QmAhic=w300" width="48" height="48" /> | [Sleep Well]
<img src="https://lh3.googleusercontent.com/h30IiTNjNayoas5zhbeE38ajB-rsW9Cpz-AOnZnBIuwoOVMMnKZxDQ1RYRLZYLW8Jxc=w300" width="48" height="48" /> | [Dayon Alarm]
<img src="http://imgur.com/ayz4NSY.png" width="48" height="48" /> | [Driving Timer]
<img src="https://lh6.ggpht.com/p_j7PAV7YnvGl1ONtQ3OcqtIBpEeocH0okXpy2Hmaztwz8k9BBPLMvUnup6ptG4HxjE=w300" width="48" height="48" /> | [TVShow Time]




Credits
=======

Thanks to [Derek Brameyer][15] for the initial version.

Thanks to [JakeWharton][11] for his work on [ViewPagerIndicator][9].

Thanks to [OAK][12] and [WillowTree Apps][13] for Maven assistance and possible future improvements.

Thanks to all contributors !

License
=======

    Copyright 2013 Derek Brameyer, Code-Troopers

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 [5]: https://raw.github.com/code-troopers/android-betterpickers/master/sample/imagery/web_feature_graphic.png
 [6]: https://play.google.com/store/apps/details?id=com.codetroopers.betterpickersapp
 [7]: http://developer.android.com/guide/developing/projects/projects-eclipse.html
 [8]: http://developer.android.com/guide/developing/projects/projects-eclipse.html#ReferencingLibraryProject
 [9]: http://viewpagerindicator.com/
 [10]: https://github.com/code-troopers/android-betterpickers/issues/new
 [11]: https://plus.google.com/108284392618554783657/posts
 [12]: http://willowtreeapps.github.io/OAK/
 [13]: http://www.willowtreeapps.com/
 [15]: https://github.com/derekbrameyer/

 [Trello]:https://play.google.com/store/apps/details?id=com.trello
 [Navig'Tours]: https://play.google.com/store/apps/details?id=com.codetroopers.transport.tours
 [Sleep Well]: https://play.google.com/store/apps/details?id=com.processingbox.jevaisbiendormir
 [Dayon Alarm]: https://play.google.com/store/apps/details?id=com.atesfactory.dayon
 [Driving Timer]: https://play.google.com/store/apps/details?id=tk.leoforney.drivingtimer
 [TVShow Time]: https://play.google.com/store/apps/details?id=com.tozelabs.tvshowtime
