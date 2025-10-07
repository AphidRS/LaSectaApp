package com.lasectaapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.lasectaapp.R
import com.lasectaapp.URLManager
import com.lasectaapp.databinding.FragmentGoleadoresBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException
import okhttp3.Request

class   FragmentGoleadores : Fragment(R.layout.fragment_goleadores) {
    private lateinit var binding : FragmentGoleadoresBinding
    private lateinit var webView : WebView
    private lateinit var currentRoundValue: String
    private lateinit var progressBar: ProgressBar
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGoleadoresBinding.bind(view)
        activity?.title = "GOLEADORES"
        progressBar = view.findViewById<View>(R.id.progressBar) as ProgressBar
        setWebView()

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                getJornada()
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebView() {
        webView = binding.webWindow
        webView.settings.javaScriptEnabled = true
        progressBar.visibility = ProgressBar.VISIBLE
        webView.loadUrl(URLManager.currentCategory.goleadoresUrl)
        webView.settings.apply {
            loadWithOverviewMode = true
            useWideViewPort = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
        }
    }

    private fun getJornada() {
        val client = OkHttpClient()
        val urlCalendario = URLManager.currentCategory.calendarioUrl
        val request = Request.Builder()
            .url(urlCalendario)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                activity?.runOnUiThread {
                    progressBar.visibility = ProgressBar.GONE
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.use { responseBody ->
                        val body = responseBody.string()
                        val regex = Regex("\"currentRound\":\"(\\d+)\"")
                        val matchResult = regex.find(body)
                        activity?.runOnUiThread {
                            currentRoundValue = matchResult?.groupValues?.get(1) ?: "1"
                            // CAMBIO 5: Llama al método correcto con el parámetro.
                            injectRemoveContentScript(currentRoundValue)
                            progressBar.visibility = ProgressBar.GONE
                        }
                    }
                } else {
                    activity?.runOnUiThread {
                        progressBar.visibility = ProgressBar.GONE
                    }
                }
            }
        })
    }

    private fun injectRemoveContentScript(currentRoundValue: String) {
        val jsScript = """
    (function() {           
        var classesToRemove = ['jss3', 'jss4', 'jss10', 'jss441', 'jss442', 'jss443', 'rightSidebar', 'tickerHolder', 'filtro-busqueda', 'filterstyle'];
        classesToRemove.forEach(function(className) {
            var elements = document.getElementsByClassName(className);
            while (elements.length > 0) {
                if (elements[0].parentNode) {
                    elements[0].parentNode.removeChild(elements[0]);
                }
            }
        });
        
        // Ajustar padding de elementos con clase 'MuiGrid-root.jss16.MuiGrid-item.MuiGrid-grid-xs-12'
        var element = document.querySelector('.MuiGrid-root.jss16.MuiGrid-item.MuiGrid-grid-xs-12');
        if (element) { element.style.padding = '0px'; }
                 
        // Ajustar font-size de elementos con clase 'MuiGrid-root.jss16.MuiGrid-item.MuiGrid-grid-xs-3'
        var element = document.querySelector('.MuiGrid-root.jss16.MuiGrid-item.MuiGrid-grid-xs-3');
        if (element) { element.style.fontSize = '8px'; }
        
        // Eliminar el elemento <footer> directamente si existe
        var footer = document.querySelector('footer');
        if (footer && footer.parentNode) {
            footer.parentNode.removeChild(footer);
        }
        
        // Ajustar la propiedad top en elementos con clase 'jss2'
        var elements = document.querySelectorAll('.jss2');
        elements.forEach(function(el) {
            el.style.top = '0px';
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