# Optimizations:
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification

# Keep:
#  - OkHttp
-dontwarn okio.**
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault
#  - Retrofit
-dontnote retrofit2.Platform
-dontwarn retrofit2.Platform$Java8
-dontwarn javax.annotation.concurrent.GuardedBy
-keepattributes Signature
-keepattributes Exceptions