
Changelog
=========
**2.2.1**
* Remove annoying warning when compiling (#208)
* Fix italian translation (#207)

**2.2.0**
* Fix Number Picker Double issues (#206)
* Add customizable methods on DatePicker and RadialTimePicker (#198)


**2.1.2**
* Fix Landscape height issue on small dpi devices (#203)
* Add min or max value for datepicker (#200 #202)
* Fix Leap Years calculation (#201)

**2.1.0**
* Set all dialogs buttons to material requirements
* Rename CalendarDatePickerDialog to CalendarDatePickerDialogFragment
* Rename RadialTimePickerDialog to RadialTimePickerDialogFragment
* Rename RecurrencePickerDialog to RecurrencePickerDialogFragment
* Rename TimeZonePickerDialog to TimeZonePickerDialogFragment
* update support-appcompat-v7
* Add year as optionnal in DatePicker (#187)

**2.0.3**
* add setTimeInSeconds and setTimeInMilliseconds to HmsPickerBuilder (#185)
* Fix DatePicker button size issues

**2.0.2**
* Fix NullPointerException in HMSPicker (#190)

**2.0.1**
* add spanish translations (#184)
* add onDismissListener on DatePickerDialog in the same way that for RadialTimePickerDialog (#189)

**2.0.0**
* remove switch-backport import
* add support-appcompat import (which break integration/compatibility with project using actionBarSherlock)
* change package name from _com.doomonafireball.betterpickers_ to _com.codetroopers.betterpickers_ (change of maintainer for project)

**1.5.5**
* updated dependency to android-switch-backport:1.4.0 (fix for `1.5.4` problems)

**1.5.4**
* remove transitive dependency to org.jraf:android-switch-backport
* add setTime() in HMSPicker
* add some translations

**1.5.3**
* allow proper use of appcompat 21
* added a few translations
* first release made from the new support team (Code-Troopers)

**1.5.2**
* Bugfix for Gradle resource processing Switch backport attrs.

**1.5.1**
* Package assets in library since we are now producing an .aar

**1.5.0**
* Package as an .aar
* Add RecurrencePicker (from AOSP calendar app)
* Add TimeZonePicker (from AOSP calendar app)
* Resolve bold typeface errors on KitKat
* Add dismiss listener for RadialTimePicker
* Add option to set "Done" text for RadialTimePicker
* Add Italian translations

**1.4.2**
* Fix the ordering of months in the DatePicker
* Add header to DatePicker keyboards for better UX

**1.4.1**
* Fix pom.xml file in Maven Central due to an improper Gradle commit of 1.4.0 (Gradle is still a work-in-progress)

**1.4.0**
* Addition of ExpirationPicker for e.g. credit card expiration date
* Addition of CalendarPicker from AOSP (see stock Calendar app) <a href="https://raw.github.com/derekbrameyer/android-betterpickers/master/sample/imagery/phone_render_calendar_date.png">[screenshot here]</a>
* Addition of RadialTimePicker from AOSP (see stock Calendar app) <a href="https://raw.github.com/derekbrameyer/android-betterpickers/master/sample/imagery/phone_render_radial_time_picker.png">[screenshot here]</a>
* Improved localization of months in DatePicker
* Removed dependency on ViewPagerIndicator
* Fix for Android 4.3 font rendering
* Fix for year field of DatePicker when using e.g. year/month/day format

**1.3.1**
* Improve UX for min/max for NumberPicker
* Added ListView demos to the sample app

**1.3.0**
* Add Catalan, Spanish
* Enable min/max for NumberPicker
* Added Javadoc comments
* Added universal handlers
* Added picker references
* UX tweak for DatePicker indicator

**1.2.0**
* Removed some unused fonts from sample/ project
* Refactor to Builder pattern
* Fix for Gingerbread font in DatePicker
* Added optional label text to NumberPicker
* DatePicker reorders text and keyboards based on locale
* NumberPicker scrolls horizontally to allow for more numbers

**1.1.0**
* Added HMS (hours:minutes:seconds) PickerFragment
* Allow for PickerFragments within target fragments
* Allow for February 29th input
