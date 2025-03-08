package com.lasectaapp.fragments

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.lasectaapp.R
import com.lasectaapp.databinding.FragmentClasificacionBinding

class FragmentClasificacion : Fragment(R.layout.fragment_clasificacion) {
    private lateinit var binding: FragmentClasificacionBinding
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentClasificacionBinding.bind(view)
        activity?.title = "CLASIFICACION"
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
                        webView.loadUrl("https://www.rffm.es/competicion/clasificaciones?temporada=20&tipojuego=2&competicion=21433999&grupo=22203383&jornada=2")
                    } else {
                        // Si no puede ir hacia atrás, cerrar la actividad
                        this@FragmentClasificacion.activity!!.finish()
                    }
                }
            })

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Llama al método para inyectar el script de eliminación de contenido
                injectRemoveContentScript()
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
        webView.settings.javaScriptEnabled = true
        progressBar.visibility = ProgressBar.VISIBLE
        webView.loadUrl("https://www.rffm.es/competicion/clasificaciones?temporada=20&tipojuego=2&competicion=21433999&grupo=22203383")
        webView.settings.apply {
            loadWithOverviewMode = true
            useWideViewPort = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
        }
    }

    private fun injectRemoveContentScript() {
        val jsScript = """
        (function() {

            // Eliminar elementos con la clase 'jss3'
            var elements = document.getElementsByClassName('jss3');
            while(elements.length > 0) {
                elements[0].parentNode.removeChild(elements[0]);
            }
            
            // Eliminar elementos con múltiples clases usando querySelectorAll
            var elements = document.querySelectorAll('.MuiContainer-root.jss4.jss441');
            elements.forEach(function(el) {
                el.parentNode.removeChild(el);
            });

            // Eliminar otros elementos por su clase
            var classesToRemove = ['jss10', 'jss11', 'rightSidebar', 'tickerHolder', 'filtro-busqueda', 'filterstyle', 'footer'];
            classesToRemove.forEach(function(className) {
                var elements = document.getElementsByClassName(className);
                while(elements.length > 0) {
                    elements[0].parentNode.removeChild(elements[0]);
                }
            });
            
            // Ajustar padding de elementos con clase 'MuiGrid-root.jss16.MuiGrid-item.MuiGrid-grid-xs-12'
            var element = document.querySelector('.MuiGrid-root.jss16.MuiGrid-item.MuiGrid-grid-xs-12');
            if (element) { element.style.padding = '0px'; }

            // Eliminar el footer
            var footer = document.querySelector('footer');
            if (footer) {
                footer.parentNode.removeChild(footer);
            }

            // Eliminar un elemento con clases 'MuiBox-root' y 'js443'
            var muiBox = document.querySelector('.MuiBox-root.js443');
            if (muiBox) {
                muiBox.parentNode.removeChild(muiBox);
            }
            
            // Seleccionar el elemento por todas sus clases y ajustar el tamaño de fuente
            var elements = document.querySelectorAll('.MuiGrid-root.jss16.MuiGrid-item.MuiGrid-grid-xs-1');
            elements.forEach(function(el) {
                el.style.fontSize = '10px';
            });
            
            // Reemplazar las cabeceras de la clasificación
            var classesToRemove = ['MuiBox-root jss73','MuiBox-root jss74','MuiBox-root jss75','MuiBox-root jss76','MuiBox-root jss77','MuiBox-root jss80'];

            classesToRemove.forEach(function(className) {
                switch (className) {
                    case 'MuiBox-root jss73':
                        document.querySelector('.MuiBox-root.jss73').innerHTML = 'PT';  
                        break;
                    case 'MuiBox-root jss74':
                        document.querySelector('.MuiBox-root.jss74').innerHTML = 'J';  
                        break;
                    case 'MuiBox-root jss75':
                        document.querySelector('.MuiBox-root.jss75').innerHTML = 'G';  
                        break;
                    case 'MuiBox-root jss76':
                        document.querySelector('.MuiBox-root.jss76').innerHTML = 'E';  
                        break;
                    case 'MuiBox-root jss77':
                        document.querySelector('.MuiBox-root.jss77').innerHTML = 'P';  
                        break;
                    case 'MuiBox-root jss80':
                        document.querySelector('.MuiBox-root.jss80').innerHTML = 'Another Text';  
                        break;
                    default:
                        // Default case, if needed
                }
            });
            
        })();"""

        webView.evaluateJavascript(jsScript, null)
    }

}