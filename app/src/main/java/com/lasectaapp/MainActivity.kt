package com.lasectaapp

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.lasectaapp.databinding.ActivityMainBinding
import com.lasectaapp.fragments.FragmentClasificacion
import com.lasectaapp.fragments.FragmentGoleadores
import com.lasectaapp.fragments.FragmentHome
import com.lasectaapp.fragments.FragmentPortal
import com.google.gson.Gson
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner


class MainActivity : AppCompatActivity() {

    //private lateinit var toggle: ActionBarDrawerToggle
    //private lateinit var drawerLayout: DrawerLayout
    private lateinit var categorySpinner: Spinner
    private lateinit var listDataHeader: MutableList<String>
    private lateinit var listDataChild: HashMap<String, CategoryURLs>
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.yellow)))
        /*drawerLayout = binding.lateralMenu
        val toggle = ActionBarDrawerToggle(
            this, binding.lateralMenu, binding.appBarMain.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        */
        loadSelectedUrl()
        prepareListData()
        categorySpinner = findViewById(R.id.category_spinner)
        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, // Layout para el item seleccionado
            listDataHeader
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = spinnerAdapter
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val groupName = listDataHeader[position]
                val newCategory = listDataChild[groupName]

                if (newCategory != null && URLManager.currentCategory != newCategory) {
                    // Actualizar la categoría en nuestro gestor
                    URLManager.currentCategory = newCategory

                    // Guardar la selección
                    saveSelectedCategory(newCategory)

                    // Recargar el fragment actual
                    reloadCurrentFragment()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No hacer nada si no se selecciona nada
            }
        }

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<FragmentHome>(R.id.app_bar_main)
            }
        }

        binding.bottomNavigation.setOnNavigationItemSelectedListener  {
            when (it.itemId) {
                R.id.nav_home -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<FragmentHome>(R.id.app_bar_main)
                        //drawerLayout.closeDrawers()
                        //if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        //    drawerLayout.closeDrawer(GravityCompat.START)
                        //}
                    }
                }

                R.id.nav_clasificacion -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<FragmentClasificacion>(R.id.app_bar_main)
                        //drawerLayout.closeDrawers()
                        //if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        //    drawerLayout.closeDrawer(GravityCompat.START)
                        //}
                    }
                }

                R.id.nav_goleadores -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<FragmentGoleadores>(R.id.app_bar_main)
                        //drawerLayout.closeDrawers()
                        //if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        //    drawerLayout.closeDrawer(GravityCompat.START)
                        //}
                    }
                }

                R.id.nav_portal -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<FragmentPortal>(R.id.app_bar_main)
                        //drawerLayout.closeDrawers()
                        //if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        //    drawerLayout.closeDrawer(GravityCompat.START)
                        //}
                    }
                }
            }
            true
        }
    }

    private fun prepareListData() {
        listDataHeader = ArrayList()
        listDataChild = HashMap()

        listDataHeader.add("Benjamin A / C")
        listDataHeader.add("Benjamin B")
        //listDataHeader.add("Benjamin C")

        listDataChild[listDataHeader[0]] = CategoryURLs(
            calendarioUrl = "https://www.rffm.es/competicion/calendario?temporada=21&tipojuego=2&competicion=24037796&grupo=24037872",
            clasificacionUrl = "https://www.rffm.es/competicion/clasificaciones?temporada=21&tipojuego=2&competicion=24037796&grupo=24037872",
            goleadoresUrl = "https://www.rffm.es/competicion/goleadores?temporada=21&tipojuego=2&competicion=24037796&grupo=24037872"
        )
        listDataChild[listDataHeader[1]] = CategoryURLs(
            calendarioUrl = "https://www.rffm.es/competicion/calendario?temporada=21&tipojuego=2&competicion=24037796&grupo=24037828",
            clasificacionUrl = "https://www.rffm.es/competicion/clasificaciones?temporada=21&tipojuego=2&competicion=24037796&grupo=24037828",
            goleadoresUrl = "https://www.rffm.es/competicion/goleadores?temporada=21&tipojuego=2&competicion=24037796&grupo=24037828"
        )
/*        listDataChild[listDataHeader[2]] = CategoryURLs(
            calendarioUrl = "https://www.rffm.es/competicion/calendario?idcompeticion=27814&idgrupo=29126&idjornada=1",
            clasificacionUrl = "https://www.rffm.es/competicion/clasificacion?idcompeticion=27814&idgrupo=29126",
            goleadoresUrl = "https://www.rffm.es/competicion/goleadores?idcompeticion=27814&idgrupo=29126"
        )*/
    }

    // Guardar el objeto CategoryURLs completo usando Gson
    private fun saveSelectedCategory(category: CategoryURLs) {
        val sharedPref = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE) ?: return
        val json = Gson().toJson(category)
        with(sharedPref.edit()) {
            putString("SELECTED_CATEGORY", json)
            apply()
        }
    }

    // Cargar el objeto CategoryURLs guardado
    private fun loadSelectedUrl() { // Renombrado en la lógica interna pero el nombre del método se mantiene
        val sharedPref = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val json = sharedPref.getString("SELECTED_CATEGORY", null)
        if (json != null) {
            URLManager.currentCategory = Gson().fromJson(json, CategoryURLs::class.java)
        }
        // Si no hay nada guardado, URLManager ya tiene los valores por defecto de "Benjamin A".
    }

    private fun reloadCurrentFragment() {
        // Obtenemos el ID del item seleccionado en el BottomNavigationView
        val selectedItemId = binding.bottomNavigation.selectedItemId
        // Volvemos a disparar el listener para ese item, lo que recargará el fragment
        binding.bottomNavigation.post { binding.bottomNavigation.setSelectedItemId(selectedItemId) }
    }
/*
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

 */
}