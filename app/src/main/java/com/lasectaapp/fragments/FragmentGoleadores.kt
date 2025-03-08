package com.lasectaapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.lasectaapp.R
import com.lasectaapp.databinding.FragmentGoleadoresBinding

class   FragmentGoleadores : Fragment(R.layout.fragment_goleadores) {
    private lateinit var binding : FragmentGoleadoresBinding
    private lateinit var webView : WebView
    private lateinit var progressBar: ProgressBar
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGoleadoresBinding.bind(view)
        activity?.title = "GOLEADORES"
        progressBar = view.findViewById<View>(R.id.progressBar) as ProgressBar
        setWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebView(){
        webView = binding.webWindow
        webView.settings.javaScriptEnabled = true
        progressBar.visibility = ProgressBar.VISIBLE
        webView.loadUrl("https://www.rffm.es/competicion/goleadores?temporada=20&tipojuego=2&competicion=21433999&grupo=22203383")
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Llama al método para inyectar el script de eliminación de contenido
                injectRemoveContentScript()
                progressBar.visibility = ProgressBar.GONE
            }
        }
    }

    private fun injectRemoveContentScript() {
        val jsScript = """
    (function() {           
        var classesToRemove = ['jss3', 'jss4', 'jss10', 'jss441', 'jss442', 'jss443', 'rightSidebar', 'tickerHolder'];
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
    })();
    """
        webView.evaluateJavascript(jsScript, null)
    }


}