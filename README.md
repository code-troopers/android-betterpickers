[![Maven Central](https://img.shields.io/maven-central/v/com.code-troopers.betterpickers/library.svg?style=flat)](https://repo1.maven.org/maven2/com/code-troopers/betterpickers/library/)
[![API](https://img.shields.io/badge/API-9%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=9)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Android%20Betterpickers-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/118)
[![Join the chat at https://gitter.im/derekbrameyer/android-betterpickers](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/derekbrameyer/android-betterpickers?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

[![Built With Cloudbees](https://www.cloudbees.com/sites/default/files/styles/large/public/Button-Built-on-CB-1.png?itok=3Tnkun-C)](https://codetroopers.ci.cloudbees.com/job/betterpickers-master/)


<img src="https://raw.githubusercontent.com/code-troopers/android-betterpickers/master/sample/imagery/screenshot_calendar_date.png" width="120">
<img src="https://raw.githubusercontent.com/code-troopers/android-betterpickers/master/sample/imagery/screenshot_date.png" width="120">
<img src="https://raw.githubusercontent.com/code-troopers/android-betterpickers/master/sample/imagery/screenshot_expiration.png" width="120">
<img src="https://raw.githubusercontent.com/code-troopers/android-betterpickers/master/sample/imagery/screenshot_hms.png" width="120">
<img src="https://raw.githubusercontent.com/code-troopers/android-betterpickers/master/sample/imagery/screenshot_number.png" width="120">
<img src="https://raw.githubusercontent.com/code-troopers/android-betterpickers/master/sample/imagery/screenshot_radial_time.png" width="120">
<img src="https://raw.githubusercontent.com/code-troopers/android-betterpickers/master/sample/imagery/screenshot_recurrence.png" width="120">
<img src="https://raw.githubusercontent.com/code-troopers/android-betterpickers/master/sample/imagery/screenshot_time.png" width="120">
<img src="https://raw.githubusercontent.com/code-troopers/android-betterpickers/master/sample/imagery/screenshot_time_zone.png" width="120">

DialogFragments modeled after the AOSP Clock and Calendar apps to improve UX for picking time, date, numbers, and other things.

Try out the sample application on [Google Play][6].

<a href="https://play.google.com/store/apps/details?id=com.doomonafireball.betterpickers.sample">
  <img alt="BetterPickers Samples on Google Play"
         src="http://developer.android.com/images/brand/en_app_rgb_wo_45.png" />
</a>

Including in Your Project
=========================
### Gradle

```groovy
compile 'com.code-troopers.betterpickers:library:2.2.1'
```

### Maven

```xml
<dependency>
  <groupId>com.code-troopers.betterpickers</groupId>
  <artifactId>library</artifactId>
  <version>2.2.1</version>
  <type>aar</type>
</dependency>
```

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

Actionbarsherlock compatibility
===============================
If you use actionbarsherlock which is not compatible with appcompat-v7 you can use the latest version of the library on the 1.x.x branch.

You can view the readme [here](README_1.6.0.md)

ChangeLog
=========
Change log file is available [here](Changelog.md)

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
 [6]: https://play.google.com/store/apps/details?id=com.doomonafireball.betterpickers.sample
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
