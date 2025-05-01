-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**

-keep class eu.discostacja.service.RadioSessionService { *; }
-keep class androidx.media3.session.MediaSessionService { *; }

-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken

 -keep class eu.discostacja.model.** { *; }