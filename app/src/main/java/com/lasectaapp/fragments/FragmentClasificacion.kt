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
import com.lasectaapp.URLManager
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
                        webView.loadUrl(URLManager.currentCategory.clasificacionUrl)
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
        webView.loadUrl(URLManager.currentCategory.clasificacionUrl)
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
        (function() 
        {

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
            //var classesToRemove = ['jss10', 'jss11', 'rightSidebar', 'tickerHolder', 'filtro-busqueda', 'filterstyle', 'footer'];
            var classesToRemove = ['jss3', 'jss4', 'jss10', 'jss441', 'jss442', 'jss443', 'rightSidebar', 'tickerHolder', 'filtro-busqueda', 'filterstyle'];
            classesToRemove.forEach(function(className) {
                var elements = document.getElementsByClassName(className);
                while(elements.length > 0) {
                    elements[0].parentNode.removeChild(elements[0]);
                }
            });
            
            // Ajustar padding de elementos con clase 'MuiGrid-root.jss16.MuiGrid-item.MuiGrid-grid-xs-12'
            var element = document.querySelector('.MuiGrid-root.jss16.MuiGrid-item.MuiGrid-grid-xs-12');
            if (element) { element.style.padding = '0px'; }
            
            // Ajustar la propiedad top en elementos con clase 'jss2'
            var elements = document.querySelectorAll('.jss2');
            elements.forEach(function(el) {
            el.style.top = '0px';
            });

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
        
            function replaceText(targetText, newText) {
                var elements = document.getElementsByTagName('*');
                for (var i = 0; i < elements.length; i++) {
                    var element = elements[i];
                    for (var j = 0; j < element.childNodes.length; j++) {
                        var node = element.childNodes[j];
                        if (node.nodeType === 3) { 
                            var text = node.nodeValue;
                            var replacedText = text.replace(targetText, newText);
                            if (replacedText !== text) {
                                node.nodeValue = replacedText;
                            }
                        }
                    }
                }
            }
        
            replaceText('Puntos', 'PT');
            replaceText('Jugados', 'J');
            replaceText('Ganados', 'G');
            replaceText('Perdidos', 'P');
            replaceText('Empates', 'E');
            replaceText('Sanción puntos', 'SCP');
            
            replaceText('S.A.D. STELLA MARIS LA GAVIA', 'STELLA MARIS');
            replaceText('A.D. NUEVA CASTILLA', 'NV. CASTILLA');
            replaceText('CDE ACADEMIA FENIX', 'AC. FENIX');
            replaceText('S.A.D. FUNDACION RAYO VALLECANO', 'RAYO VALL.');
            replaceText('C.D. ESCUELA BREOGAN', 'ESC. BREOGAN');
            replaceText('C.D. TAJAMAR', 'TAJAMAR');
            replaceText('GREDOS SAN DIEGO-VALLECAS', 'GSD VALLECAS');
            replaceText('C.D. SPORT VILLA DE VALLECAS', 'SPORT V.VALLECAS');
            
        })();
        """
        webView.evaluateJavascript(jsScript, null)
    }
}