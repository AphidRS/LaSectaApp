package com.lasectaapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.lasectaapp.R
import com.lasectaapp.databinding.FragmentHomeBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class FragmentHome : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var currentRoundValue: String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        activity?.title = "CALENDARIO"
        progressBar = view.findViewById<View>(R.id.progressBar) as ProgressBar
        setWebView()

        this.activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                @SuppressLint("UseRequireInsteadOfGet")
                override fun handleOnBackPressed() {
                    if (webView.canGoBack()) {
                        // Si el WebView tiene historial, ir hacia atrás en el historial
                        progressBar.visibility = ProgressBar.VISIBLE
                        webView.loadUrl("https://www.rffm.es/competicion/calendario?temporada=20&tipojuego=2&competicion=21433999&grupo=22203383")
                    } else {
                        // Si no puede ir hacia atrás, cerrar la actividad
                        this@FragmentHome.activity!!.finish()
                    }
                }
            })

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Llama al método para inyectar el script de eliminación de contenido
                //getJornada()
                //injectRemoveContentScript(currentRoundValue)
                injectRemoveContentScript("16")
                progressBar.visibility = ProgressBar.GONE
            }
        }

    }

    override fun onResume() {
        super.onResume()
        // Forzar orientación de la actividad a landscape cuando el fragmento está visible
        //activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    override fun onPause() {
        super.onPause()
        // Cambiar la orientación a portrait antes de salir
        //activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onDestroyView() {
        super.onDestroyView()
        webView.destroy() // Limpiar el WebView si es necesario para liberar recursos
    }

    private fun setWebView() {
        webView = binding.webWindow
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        progressBar.visibility = ProgressBar.VISIBLE
        webView.loadUrl("https://www.rffm.es/competicion/calendario?temporada=20&tipojuego=2&competicion=21433999&grupo=22203383")
        webView.settings.apply {
            loadWithOverviewMode = true
            useWideViewPort = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
        }
    }

    private fun getJornada(){
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://www.rffm.es/competicion/calendario?temporada=20&tipojuego=2&competicion=21433999&grupo=22203383")
            .build()
        /*
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.use { responseBody ->
                    // Convert the response body to a string
                    //val body = responseBody.string()

                    // Parse the string as JSON
                    //val gson = Gson()
                    //val jObject = gson.fromJson(body, JsonObject::class.java)

                    // Access specific JSON properties if needed
                    //currentRoundValue = jObject.getAsJsonObject("props")?.getAsJsonObject("pageProps")?.get("currentRound")?.asString.toString()
                }
            }
        })*/
    }

    private fun injectRemoveContentScript(currentRoundValue: String) {
        val jsScript = """
    (function() {
       
        var currentRoundValue = "$currentRoundValue";
        
        // Eliminar elementos con clase 'jss3'
        var elements = document.getElementsByClassName('jss3');
        while (elements.length > 0) {
            elements[0].parentNode.removeChild(elements[0]);
        }
        
        // Eliminar elementos con clase 'MuiGrid-grid-md-4'
        elements = document.getElementsByClassName('MuiGrid-grid-md-4');
        while (elements.length > 0) {
            elements[0].parentNode.removeChild(elements[0]);
        }
        
        // Eliminar elementos con múltiples clases 'MuiBox-root' y 'jss14'
        elements = document.querySelectorAll('.MuiBox-root.jss14');
        elements.forEach(function(el) {
            el.parentNode.removeChild(el);
        });
        
        // Eliminar elementos con múltiples clases 'jss10' y 'jss13' y ajustar altura
        elements = document.querySelectorAll('.jss10.jss13');
        elements.forEach(function(el) {
            el.style.height = '0';
            el.parentNode.removeChild(el);
        });
        
        // Ajustar padding de elementos con clase 'MuiGrid-root.jss16.MuiGrid-item.MuiGrid-grid-xs-12'
        var element = document.querySelector('.MuiGrid-root.jss16.MuiGrid-item.MuiGrid-grid-xs-12');
        if (element) { element.style.padding = '0px';}
        
        // Ajustar padding de elementos con clase 'MuiContainer-root'
        elements = document.getElementsByClassName('MuiContainer-root');
        for (var i = 0; i < elements.length; i++) {
            elements[i].style.padding = '10px';
        }
        
        // Ajustar la propiedad top en elementos con clase 'jss2'
        elements = document.getElementsByClassName('jss2');
        for (var i = 0; i < elements.length; i++) {
            elements[i].style.top = '0px';
        }
        
        // Eliminar el footer si existe
        var footer = document.querySelector('footer');
        if (footer) {
            footer.parentNode.removeChild(footer);
        }
        
        var spans = document.getElementsByTagName('span');
        for (var i = 0; i < spans.length; i++) {
            if (spans[i].innerText.includes('JORNADA ' + currentRoundValue)) {
                spans[i].scrollIntoView({ behavior: 'smooth', block: 'center' });
            }
        }

         
    })();
    """
        webView.evaluateJavascript(jsScript, null)
    }
}

