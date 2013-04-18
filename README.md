![BetterPickers Feature Graphic][5]

DialogFragments modeled after the 4.2 Alarm Clock TimePicker to improve UX for picking time, date, and numbers.

Try out the sample application on [Google Play][6].


Including in Your Project
=========================

Android-BetterPickers is presented as an [Android library project][7]. A
standalone JAR is not possible due to the theming capabilities offered by the
indicator widgets.

You can include this project by [referencing it as a library project][8] in
Eclipse or ant.

If you are a Maven user you can easily include the library by specifying it as
a dependency:

    <dependency>
      <groupId>com.doomonafireball.betterpickers</groupId>
      <artifactId>library</artifactId>
      <version>1.0.0</version>
      <type>apklib</type>
    </dependency>

This project depends on Jake Wharton's `ViewPagerIndicator` library. Details for
including this library is available [here][9].

Usage
=====

*For a working implementation of this project see the `sample/` folder.*

  1. Implement the appropriate Handler callbacks:

        public class MyActivity extends Activity implements DatePickerDialogFragment.DatePickerDialogHandler {
        
          @Override
          public void onCreate(Bundle savedInstanceState) {
            // ...
          }
          
          // ...
          
          @Override
          public void onDialogDateSet(int year, int monthOfYear, int dayOfMonth) {
            // Do something with your date!
          }
        }  

  2. Use BetterPickerUtils to create a PickerDialog with a theme:

        BetterPickerUtils.showDateEditDialog(getSupportFragmentManager(), R.style.BetterPickersDialogFragment);

Theming
=======

Work in progress.

 [1]: https://raw.github.com/derekbrameyer/android-betterpickers/master/sample/imagery/screenshot_time.png
 [2]: https://raw.github.com/derekbrameyer/android-betterpickers/master/sample/imagery/screenshot_number.png
 [3]: https://raw.github.com/derekbrameyer/android-betterpickers/master/sample/imagery/screenshot_number_negative_decimal.png
 [4]: https://raw.github.com/derekbrameyer/android-betterpickers/master/sample/imagery/screenshot_date.png
 [5]: https://raw.github.com/derekbrameyer/android-betterpickers/master/sample/imagery/web_feature_graphic.png
 [6]: https://play.google.com/store/apps/details?id=com.doomonafireball.betterpickers.sample
 [7]: http://developer.android.com/guide/developing/projects/projects-eclipse.html
 [8]: http://developer.android.com/guide/developing/projects/projects-eclipse.html#ReferencingLibraryProject
 [9]: http://viewpagerindicator.com/
