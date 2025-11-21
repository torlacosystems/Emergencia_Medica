# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep medical data class
-keep class com.emergencia.medica.MedicalData { *; }

# Keep activities
-keep class com.emergencia.medica.MainActivity { *; }
-keep class com.emergencia.medica.LockScreenActivity { *; }

# Keep accessibility service
-keep class com.emergencia.medica.EmergencyAccessibilityService { *; }

# ZXing
-keep class com.google.zxing.** { *; }
-dontwarn com.google.zxing.**

# iText PDF
-keep class com.itextpdf.** { *; }
-dontwarn com.itextpdf.**
-dontwarn org.slf4j.**
-keep class org.slf4j.** { *; }

# Prevent obfuscation of emergency data
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
