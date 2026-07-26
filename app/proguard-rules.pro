# Add project specific ProGuard rules here.
-keepattributes Signature
-keepattributes *Annotation*

# Retrofit
-keepattributes RuntimeVisibleAnnotations
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

# Gson
-keep class com.aiher.app.data.model.** { *; }

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**