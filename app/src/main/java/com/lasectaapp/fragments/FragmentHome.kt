package com.lasectaapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.lasectaapp.R
import com.lasectaapp.databinding.FragmentHomeBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import com.lasectaapp.URLManager


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
                        // CAMBIO: Usar la URL dinámica también aquí si es necesario, o mantener la principal.
                        webView.loadUrl(URLManager.currentCategory.calendarioUrl)                    } else {
                        // Si no puede ir hacia atrás, cerrar la actividad
                        this@FragmentHome.activity!!.finish()
                    }
                }
            })

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Llama al metodo para inyectar el script de eliminación de contenido
                getJornada()
                //injectRemoveContentScript(currentRoundValue)
                progressBar.visibility = ProgressBar.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // ... (código sin cambios)
    }

    override fun onPause() {
        super.onPause()
        // ... (código sin cambios)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        webView.destroy()
    }

    private fun setWebView() {
        webView = binding.webWindow
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        progressBar.visibility = ProgressBar.VISIBLE
        val finalUrl = URLManager.currentCategory.calendarioUrl
        webView.loadUrl(finalUrl)
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
        val finalUrl = URLManager.currentCategory.calendarioUrl
        val request = Request.Builder()
            .url(finalUrl)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Es buena práctica manejar el error, por ejemplo, mostrando un Toast.
                activity?.runOnUiThread {
                    progressBar.visibility = ProgressBar.GONE
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.use { responseBody ->
                    val body = responseBody.string()
                    val regex = Regex("\"currentRound\":\"(\\d+)\"")
                    val matchResult = regex.find(body)

                    activity?.runOnUiThread {
                        if (matchResult != null) {
                            currentRoundValue = matchResult.groupValues[1]
                        } else {
                            currentRoundValue = "1"
                        }
                        injectRemoveContentScript(currentRoundValue)
                    }
                }
            }
        })
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
        elements = document.querySelectorAll('.jss10.jss13.jss15');
        elements.forEach(function(el) {
            el.style.height = '0';
            el.parentNode.removeChild(el);
        });
        
        // Eliminar elementos con múltiples clases 'jss10' y 'jss68'
        elements = document.querySelectorAll('.jss10.jss68.jss12');
        elements.forEach(function(el) {
            el.parentNode.removeChild(el);
        });
        
        // Eliminar elementos con múltiples clases 'MuiGrid-root jss16 staticContent MuiGrid-item MuiGrid-grid-xs-12 MuiGrid-grid-md-8
        elements = document.querySelectorAll('.MuiGrid-root.jss16.staticContent.MuiGrid-item.MuiGrid-grid-xs-12.MuiGrid-grid-md-8');
        elements.forEach(function(el) {
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
        
        // Eliminar el filterstyle si existe
        var element = document.querySelector('filterstyle');
        if (element) {
            element.parentNode.removeChild(element);
        }
        
        // Eliminar el footer si existe
        var footer = document.querySelector('footer');
        if (footer) {
            footer.parentNode.removeChild(footer);
        }
        
        var spans = document.getElementsByTagName('span');
        for (var i = 0; i < spans.length; i++) {
        if (spans[i].innerText.trim() === 'JORNADA ' + "$currentRoundValue") {
            spans[i].scrollIntoView({ behavior: 'smooth', block: 'center' });
            break;
        }
    }
         
    })();
    """
        webView.evaluateJavascript(jsScript, null)
    }
}
