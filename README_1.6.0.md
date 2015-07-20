Version 1.6.0
=============

If you don't want appcompat-v7 you can use last release of library before this change : 1.6.0
Release 1.6.0 was build with _support-v4_ and _android-switch-backport_

### Maven

Including this release in your pom.xml

```xml
<dependency>
  <groupId>com.doomonafireball.betterpickers</groupId>
  <artifactId>library</artifactId>
  <version>1.6.0</version>
  <type>aar</type>
</dependency>
```

### Gradle

Including this release in your build.gradle

```groovy
compile 'com.doomonafireball.betterpickers:library:1.6.0'
```

If you are bringing in the support library you may need to add an exclusion:

```groovy
compile ("com.doomonafireball.betterpickers:library:1.6.0") {
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


