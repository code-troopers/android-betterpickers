[![Maven Central](https://img.shields.io/maven-central/v/com.doomonafireball.betterpickers/library.svg?style=flat)](https://repo1.maven.org/maven2/com/doomonafireball/betterpickers/library/)
[![API](https://img.shields.io/badge/API-9%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=9)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Android%20Betterpickers-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/118)

[![Built With Cloudbees](https://www.cloudbees.com/sites/default/files/styles/large/public/Button-Built-on-CB-1.png?itok=3Tnkun-C)](https://codetroopers.ci.cloudbees.com/job/betterpickers-master/)

![BetterPickers Feature Graphic][5]

DialogFragments modeled after the AOSP Clock and Calendar apps to improve UX for picking time, date, numbers, and other things.

Try out the sample application on [Google Play][6].

<a href="https://play.google.com/store/apps/details?id=com.doomonafireball.betterpickers.sample">
  <img alt="BetterPickers Samples on Google Play"
         src="http://developer.android.com/images/brand/en_app_rgb_wo_45.png" />
</a>

Including in Your Project
=========================

Android-BetterPickers is presented as an [Android library project][7]. A
standalone JAR is not possible due to the theming capabilities offered by the DialogFragments.

You can include this project by [referencing it as a library project][8] in
Eclipse or ant.  Note that to use this library, both it and your project must be compiled with Android 4.0 (API level 14) or newer, similar to ActionBarSherlock.

If you are a Maven user you can easily include the library by specifying it as
a dependency:

```xml
<dependency>
  <groupId>com.doomonafireball.betterpickers</groupId>
  <artifactId>library</artifactId>
  <version>1.5.5</version>
  <type>aar</type>
</dependency>
```

If you are a Gradle user you can also easily include the library:

```groovy
compile 'com.doomonafireball.betterpickers:library:1.5.5'
```

If you are bringing in the support library you may need to add an exclusion:

```groovy
compile ("com.doomonafireball.betterpickers:library:1.5.5") {
    exclude group: 'com.android.support', module: 'support-v4'
}
```

_You MUST manually add dependency to android-switch-backport_

```
compile 'org.jraf:android-switch-backport:1.4.0@aar'
```
And as it not available on maven central add a new maven repository
```
maven {
    url "http://JRAF.org/static/maven/2"
}
```

There is a standalone Gradle demo [here][14] that may also help.

Usage
=====

*For a working implementation of this project see the `sample/` folder.*

  1. Implement the appropriate Handler callbacks:

  ```java
  public class MyActivity extends Activity implements 
    DatePickerDialogFragment.DatePickerDialogHandler {
  
    @Override
    public void onCreate(Bundle savedInstanceState) {
      // ...
    }
    
    @Override
    public void onDialogDateSet(int year, int monthOfYear, int dayOfMonth) {
      // Do something with your date!
    }
  }
  ```

  2. Use one of the Builder classes to create a PickerDialog with a theme:

  ```java
  DatePickerBuilder dpb = new DatePickerBuilder()
      .setFragmentManager(getSupportFragmentManager())
      .setStyleResId(R.style.BetterPickersDialogFragment);
  dpb.show();
  ```

Theming
=======

*For a demonstration of theming, see the `sample/` folder.*

You can use your own themes if you'd like to change certain attributes.  BetterPickers currently allows for customization of the following attributes:

        bpDialogBackground       :: the drawable (preferably a 9-patch) used as a window background for the DialogFragment
        bpTextColor              :: the color (optionally state list) for all text in the DialogFragment
        bpDeleteIcon             :: the drawable (optionally state list) for the delete button
        bpCheckIcon              :: the drawable (optionally state list) for the check button in the DateDialogPicker
        bpKeyBackground          :: the drawable (optionally state list) for the keyboard buttons
        bpButtonBackground       :: the drawable (optionally state list) for the Set, Cancel, and Delete buttons
        bpDividerColor           :: the color used for the DialogFragment dividers
        bpKeyboardIndicatorColor :: the color used for the ViewPagerIndicator on the DateDialogPicker

  1. Create your own custom theme in `styles.xml`:

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

  2. Instantiate your `DialogFragment` using your custom theme:

  ```java
  DatePickerBuilder dpb = new DatePickerBuilder()
      .setFragmentManager(getSupportFragmentManager())
      .setStyleResId(R.style.MyCustomBetterPickerTheme);
  dpb.show();
  ```

Contribution
============

### Pull requests are welcome!

Feel free to contribute to BetterPickers.

If you've fixed a bug or have a feature you've added, just create a pull request.

If you've found a bug, want a new feature, or have other questions, [file an issue][10]. I'll try to answer as soon as I find the time.

Credits
=======

Thanks to [JakeWharton][11] for his work on [ViewPagerIndicator][9].

Thanks to [OAK][12] and [WillowTree Apps][13] for Maven assistance and possible future improvements.

License
=======

    Copyright 2013 Derek Brameyer

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 [5]: https://raw.github.com/derekbrameyer/android-betterpickers/master/sample/imagery/web_feature_graphic.png
 [6]: https://play.google.com/store/apps/details?id=com.doomonafireball.betterpickers.sample
 [7]: http://developer.android.com/guide/developing/projects/projects-eclipse.html
 [8]: http://developer.android.com/guide/developing/projects/projects-eclipse.html#ReferencingLibraryProject
 [9]: http://viewpagerindicator.com/
 [10]: https://github.com/derekbrameyer/android-betterpickers/issues/new
 [11]: https://plus.google.com/108284392618554783657/posts
 [12]: http://willowtreeapps.github.io/OAK/
 [13]: http://www.willowtreeapps.com/
 [14]: https://github.com/derekbrameyer/android-betterpickers-gradle-sample
