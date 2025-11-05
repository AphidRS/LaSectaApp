package com.lasectaapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.load

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long = 3000 // 3 segundos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // El sistema ya ha mostrado un fondo azul gracias a nuestro tema.
        // Ahora, simplemente ponemos nuestro contenido encima.
        setContentView(R.layout.activity_splash)

        // Carga la animación GIF
        val splashImageView: ImageView = findViewById(R.id.splash_image_view)
        val imageLoader = ImageLoader.Builder(this)
            .components {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()

        // Asegúrate de que tu GIF se llama 'soccer_ball' o el nombre correcto
        splashImageView.load(R.drawable.baseline_sports_soccer_24, imageLoader)

        // Navega a MainActivity después del tiempo definido
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_TIME_OUT)
    }
}
