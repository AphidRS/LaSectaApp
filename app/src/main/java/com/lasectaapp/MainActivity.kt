package com.lasectaapp

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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

class MainActivity : AppCompatActivity() {

    //private lateinit var toggle: ActionBarDrawerToggle
    //private lateinit var drawerLayout: DrawerLayout
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
/*
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

 */
}