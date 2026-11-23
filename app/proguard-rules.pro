# App code: kept whole (small, and Room/Hilt/serialization/@Generable rely on reflection-free codegen anyway).
-keep class com.example.callguardian.** { *; }

# kotlinx.serialization
-keepattributes *Annotation*, InnerClasses, Signature, EnclosingMethod
-dontnote kotlinx.serialization.**
-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }

# networknt json-schema-validator + Jackson (reflection-heavy)
-keep class com.networknt.schema.** { *; }
-keep class com.fasterxml.jackson.** { *; }
-dontwarn com.fasterxml.jackson.**
-dontwarn com.networknt.**
-dontwarn org.slf4j.**
-dontwarn com.ethlo.time.**
-dontwarn org.jcodings.**
-dontwarn org.joni.**

# Moshi reflective adapters for lookup DTOs
-keep class com.squareup.moshi.** { *; }
-dontwarn com.squareup.moshi.**
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
