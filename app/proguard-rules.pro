# =================================================================================
#               REGLAS ESENCIALES DE PROGUARD PARA TU APP
# =================================================================================

# --- REGLAS PARA LIBRERÍAS DE RED (RETROFIT Y OKHTTP) ---
# Mantienen las clases que usa OkHttp internamente
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

# Mantienen las clases que usa Retrofit internamente
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }
-dontwarn retrofit2.**

# --- REGLAS PARA KOTLINX SERIALIZATION ---
# ¡MUY IMPORTANTE! Esto evita que R8 renombre tus clases de modelo (Data Classes)
# y rompa el parseo del JSON.
# Reemplaza 'com.lasectaapp.model' con el nombre real de tu paquete de modelos.
-keepclasseswithmembers class com.lasectaapp.model.** {
    @kotlinx.serialization.Serializable <methods>;
}
# Mantén los serializadores generados por la librería
-keep class **$$serializer { *; }

# --- REGLAS PARA CORRUTINAS DE KOTLIN ---
# Evitan problemas con el código de las corrutinas
-keepclassmembers class kotlinx.coroutines.internal.** {
    <fields>;
    <init>(...);
}
-keepclassmembers class ** extends kotlin.coroutines.jvm.internal.ContinuationImpl {
    <fields>;
    <init>(...);
}

# --- REGLAS PARA COIL (Carga de imágenes) ---
# Evitan que Coil falle al cargar imágenes
-keep class coil.** { *; }
-dontwarn coil.**

# --- REGLAS PARA TU PROPIO CÓDIGO (si es necesario) ---
# Si tienes alguna clase específica que no quieres que se ofusque,
# puedes añadir una regla como la siguiente:
# -keep class com.paquete.MiClaseImportante { *; }
