package com.nomasmatchapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.nomasmatchapp.databinding.ActivityMainBinding
import com.nomasmatchapp.fragments.FragmentClasificacion
import com.nomasmatchapp.fragments.FragmentGemini
import com.nomasmatchapp.fragments.FragmentGoleadores
import com.nomasmatchapp.fragments.FragmentHome
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MainActivity : AppCompatActivity() {

    private var isInitialSpinnerSelection = true
    private lateinit var listDataHeader: MutableList<String>
    private lateinit var listDataChild: HashMap<String, CategoryURLs>
    private lateinit var binding: ActivityMainBinding
    private lateinit var categorySpinner: android.widget.Spinner

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. Llama a installSplashScreen() ANTES de super.onCreate()    val splashScreen = installSplashScreen()
        installSplashScreen()
        // 2. Llama a super.onCreate()
        super.onCreate(savedInstanceState)

        // 3. Infla el layout y establece el contenido UNA SOLA VEZ usando ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareListData()
        loadSelectedUrl()



        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<FragmentHome>(R.id.nav_host_fragment_content_main)
            }
        }
        binding.root.post {
            setupSpinnerViews()
        }
        val bottomNavView = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_navigation)
        bottomNavView.setOnNavigationItemSelectedListener  {
            when (it.itemId) {
                R.id.nav_home -> replaceFragment(FragmentHome())
                R.id.nav_clasificacion -> replaceFragment(FragmentClasificacion())
                R.id.nav_goleadores -> replaceFragment(FragmentGoleadores())
                R.id.nav_portal -> replaceFragment(FragmentGemini())
            }
            true
        }
    }

    // He creado un pequeño helper para no repetir el código de transacción
    private fun replaceFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.nav_host_fragment_content_main, fragment)
        }
    }

    private fun setupSpinnerViews() {
        categorySpinner = findViewById(R.id.category_spinner)


        // Se usa el constructor de 4 parámetros. Este se encarga de todo y no permite
        // el bloque extra con la sobreescritura de métodos.
        val spinnerAdapter = object : ArrayAdapter<String>(
            this@MainActivity,
            R.layout.custom_spinner_item,
            R.id.spinner_text,
            listDataHeader
        ) {
            // Este método personaliza la vista CERRADA del spinner (la barra principal)
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                // Ponemos el fondo azul deseado
                view.setBackgroundColor(android.graphics.Color.parseColor("#152e60"))
                // Nos aseguramos de que el texto sea blanco
                (view.findViewById(R.id.spinner_text) as TextView).setTextColor(android.graphics.Color.WHITE)
                return view
            }

            // Este método personaliza cada fila del DESPLEGABLE
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                // 1. Obtenemos la referencia al TextView dentro de la fila.
                val textView = view.findViewById<TextView>(R.id.spinner_text)
                // 2. Le cambiamos el color del texto al azul deseado.
                textView.setTextColor(android.graphics.Color.parseColor("#152e60"))
                return view
            }
        }


        categorySpinner.adapter = spinnerAdapter

        // El resto de tu lógica ya es correcto.
        // Asigna el listener PRIMERO
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Si es la primera vez que se dispara (al llamar a setSelection),
                // ignóralo y cambia la bandera.
                if (isInitialSpinnerSelection) {
                    isInitialSpinnerSelection = false
                    return
                }

                // A partir de aquí, solo se ejecutará con clics reales del usuario.
                val groupName = listDataHeader[position]
                val newCategory = listDataChild[groupName]

                if (newCategory != null && URLManager.currentCategory != newCategory) {
                    URLManager.currentCategory = newCategory
                    saveSelectedCategory(newCategory)
                    reloadCurrentFragment()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Establece la selección DESPUÉS. Esto disparará el listener una vez.
        val loadedCategoryName = listDataChild.entries.find { it.value == URLManager.currentCategory }?.key
        if (loadedCategoryName != null) {
            val initialPosition = listDataHeader.indexOf(loadedCategoryName)
            if (initialPosition >= 0) {
                categorySpinner.setSelection(initialPosition)
            }
        }
    }


    private fun prepareListData() {
        listDataHeader = ArrayList()
        listDataChild = HashMap()
        listDataHeader.add("Benjamin A / C")
        listDataHeader.add("Benjamin B")

        listDataChild[listDataHeader[0]] = CategoryURLs(
            calendarioUrl = "https://www.rffm.es/competicion/calendario?temporada=21&tipojuego=2&competicion=24037796&grupo=24037872",
            clasificacionUrl = "https://www.rffm.es/competicion/clasificaciones?temporada=21&tipojuego=2&competicion=24037796&grupo=24037872",
            goleadoresUrl = "https://www.rffm.es/competicion/goleadores?temporada=21&competicion=24037796&grupo=24037872&tipojuego=2"
        )
        listDataChild[listDataHeader[1]] = CategoryURLs(
            calendarioUrl = "https://www.rffm.es/competicion/calendario?temporada=21&tipojuego=2&competicion=24037796&grupo=24037828",
            clasificacionUrl = "https://www.rffm.es/competicion/clasificaciones?temporada=21&tipojuego=2&competicion=24037796&grupo=24037828",
            goleadoresUrl = "https://www.rffm.es/competicion/goleadores?temporada=21&competicion=24037796&grupo=24037828&tipojuego=2"
        )
    }

    private fun saveSelectedCategory(category: CategoryURLs) {
        val sharedPref = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE) ?: return
        val jsonString = Json.encodeToString(category)
        sharedPref.edit {
            putString("SELECTED_CATEGORY", jsonString)
        }
    }

    private fun loadSelectedUrl() {
        val sharedPref = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val jsonString = sharedPref.getString("SELECTED_CATEGORY", null)

        if (jsonString != null) {
            // Si hay algo guardado, lo cargamos
            URLManager.currentCategory = Json.decodeFromString<CategoryURLs>(jsonString)
        } else {
            // SI NO HAY NADA GUARDADO (primer arranque):
            // Asignamos la primera categoría de la lista como valor por defecto.
            // Esto requiere que prepareListData() se haya llamado antes.
            if (::listDataChild.isInitialized && listDataChild.isNotEmpty()) {
                URLManager.currentCategory = listDataChild.values.first()
            }
        }
    }
    private fun reloadCurrentFragment() {
        // 1. Obtén la BottomNavigationView usando findViewById, el método seguro.
        val bottomNavView = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_navigation)

        // 2. Obtén el ID del ítem actualmente seleccionado desde esta referencia segura.
        val currentSelectedItemId = bottomNavView.selectedItemId

        // 3. Decide qué fragmento corresponde a ese ID y crea una NUEVA instancia.
        val fragmentToReload = when (currentSelectedItemId) {
            R.id.nav_home -> FragmentHome()
            R.id.nav_clasificacion -> FragmentClasificacion()
            R.id.nav_goleadores -> FragmentGoleadores()
            R.id.nav_portal -> FragmentGemini()
            else -> null // Si no se encuentra, no hacemos nada.
        }

        // 3. Si se encontró un fragmento para recargar, lo reemplazamos en el contenedor.
        if (fragmentToReload != null) {
            replaceFragment(fragmentToReload)
        }
    }
}

