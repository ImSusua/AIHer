# Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * { @retrofit2.http.* <methods>; }
-keep class okhttp3.** { *; }
-keep class okio.** { *; }
-keep interface retrofit2.** { *; }

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapter { *; }
-keep class * implements com.google.gson.TypeAdapterFactory { *; }
-keep class * implements com.google.gson.JsonSerializer { *; }
-keep class * implements com.google.gson.JsonDeserializer { *; }

# Keep model classes
-keep class com.aiher.app.data.model.** { *; }
-keep class com.aiher.app.data.local.** { *; }

# Kotlin Coroutines
-keepclassmembers class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# Room
-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class * { *; }
-keep class androidx.room.** { *; }

# Hilt / Dagger
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.android.internal.lifecycle.HiltViewModelFactory$ViewModelComponentBuilderEntryPoint { *; }
-keep class * extends androidx.lifecycle.ViewModel { *; }
-keep @dagger.hilt.android.HiltAndroidApp class * { *; }
-keep @dagger.hilt.android.AndroidEntryPoint class * { *; }
-keepclassmembers class * { @javax.inject.Inject *; }

# Kotlin Parcelize
-keep class kotlinx.parcelize.** { *; }
-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# Compose
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Coil
-keep class coil.** { *; }
-dontwarn coil.**

# DataStore
-keep class androidx.datastore.** { *; }

# Keep all Hilt generated classes
-keep class **_HiltComponents { *; }
-keep class **_HiltModules { *; }
-keep class **_HiltApp { *; }
-keep class * extends dagger.hilt.android.internal.modules.ApplicationContextModule { *; }

# Keep generic signatures for reflection
-keepattributes Signature, InnerClasses, EnclosingMethod, *Annotation*

# Suppress warnings
-dontwarn org.jetbrains.annotations.**
-dontwarn javax.lang.model.element.**
-dontwarn com.google.errorprone.**
