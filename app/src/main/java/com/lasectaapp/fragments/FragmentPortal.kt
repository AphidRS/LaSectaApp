package com.lasectaapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.lasectaapp.R
import com.lasectaapp.databinding.FragmentPortalBinding

class FragmentPortal : Fragment(R.layout.fragment_portal) {
    private lateinit var binding : FragmentPortalBinding
    private lateinit var webView : WebView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPortalBinding.bind(view)
        activity?.title = "PORTAL DEL FEDERADO"
        setWebView()

        this.activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                @SuppressLint("UseRequireInsteadOfGet")
                override fun handleOnBackPressed() {
                    if (webView.canGoBack()) {
                        // Si el WebView tiene historial, ir hacia atrás en el historial
                        webView.goBack()
                    } else {
                        // Si no puede ir hacia atrás, cerrar la actividad
                        this@FragmentPortal.activity!!.finish()
                    }
                }
            })

        webView.settings.apply {
            loadWithOverviewMode = true
            useWideViewPort = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
        }
    }

    private fun setWebView(){
        webView = binding.webWindow
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Llama al método para inyectar el script de eliminación de contenido
                injectRemoveContentScript()
            }
        }

        webView.loadUrl("https://www.rffm.es/portal-federado")
    }

    private fun injectRemoveContentScript() {
        val jsScript = """
    (function() {
        // Eliminar elementos con la clase 'header-large'
        var elements = document.getElementsByClassName('header-large');
        while(elements.length > 0) {
            elements[0].parentNode.removeChild(elements[0]);
        }

        // Eliminar elementos con las clases 'title-box' y 'ajustado' usando querySelectorAll
        elements = document.querySelectorAll('.title-box.ajustado');
        elements.forEach(function(el) {
            el.parentNode.removeChild(el);
        });

        // Eliminar elementos con la clase 'help-icon'
        elements = document.getElementsByClassName('help-icon');
        while(elements.length > 0) {
            elements[0].parentNode.removeChild(elements[0]);
        }

        // Eliminar el elemento footer
        var footer = document.querySelector('footer');
        if (footer) {
            footer.parentNode.removeChild(footer);
        }
    })();
"""
        webView.evaluateJavascript(jsScript, null)
    }
}