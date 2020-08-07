package com.shin.movieapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shin.movieapp.databinding.ActivityMainBinding
import com.shin.movieapp.model.user.UserDatabase
import com.shin.movieapp.repository.favorite.FavoriteRepository
import com.shin.movieapp.repository.reminder.ReminderRepository
import com.shin.movieapp.repository.user.UserRepository
import com.shin.movieapp.ui.favorite.FavoriteModelFactory
import com.shin.movieapp.ui.favorite.FavoriteViewModel
import com.shin.movieapp.ui.reminder.ReminderDataAdapter
import com.shin.movieapp.ui.reminder.ReminderModelFactory
import com.shin.movieapp.ui.reminder.ReminderViewModel
import com.shin.movieapp.ui.user.EditUserActivity
import com.shin.movieapp.ui.user.UserViewModel
import com.shin.movieapp.ui.user.UserViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    private val REQUEST_CODE = 101
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolBar)

        navController = findNavController(R.id.my_nav_host_fragment)

        // Move to details movie when click notification
        val notifyIntent = intent
        val id = notifyIntent.getIntExtra("movieId", 0)
        if(id != 0 ){
            var bundle = bundleOf(
                "movie_id" to id,
                "movie_title" to notifyIntent.getStringExtra("movieTitle")
            )
            navController.navigate(R.id.action_movieFragment_to_singleMovieFragment, bundle)
        }

        bottom_nav.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(binding.bottomNav.menu, binding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Edit user
        binding.editButton.setOnClickListener {
            binding.drawerLayout.closeDrawer(GravityCompat.START, false)
            val intent = Intent(this@MainActivity, EditUserActivity::class.java)
            startActivity(intent)
        }

        // Show all reminder
        binding.showAllReminder.setOnClickListener {
            navController.navigate(R.id.action_movieFragment_to_reminderFragment)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }

        // Get data
        val userDao = UserDatabase.getInstance(application).userDao()
        val userRepository = UserRepository(userDao)

        val factory = UserViewModelFactory(userRepository, application)

        userViewModel = ViewModelProviders.of(this, factory).get(UserViewModel::class.java)

        binding.viewModel = userViewModel

        binding.lifecycleOwner = this

        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("TAG", "Permission to record denied")
            makeRequest()
        }
        // Set badge for favorite
        getFavoriteSize()
        displayReminder()
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("TAG", "Permission has been denied by user")
                } else {
                    Log.i("TAG", "Permission has been granted by user")
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.setIcon(R.drawable.ic_list_white_24dp)
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
       // When change fragment of bottom navigation, I can open drawer layout
        return navController.navigateUp(appBarConfiguration)
    }

    fun getFavoriteSize(){
        val favoriteDao = UserDatabase.getInstance(this.applicationContext).favoriteDao()

        val favoriteRepository = FavoriteRepository(favoriteDao)

        val factory = FavoriteModelFactory(favoriteRepository, this.application)

        val viewModel = ViewModelProviders.of(this, factory).get(FavoriteViewModel::class.java)

        viewModel.favoriteList.observe(this, Observer {
            var badge = binding.bottomNav.getOrCreateBadge(R.id.favoriteFragment)
            badge.isVisible = true
            badge.number = it.size
        })
    }

    fun displayReminder(){
        val reminderDao = UserDatabase.getInstance(this).reminderDao()
        val reminderRepository = ReminderRepository(reminderDao)
        val reminderModelFactory = ReminderModelFactory(reminderRepository)
        val reminderViewModel = ViewModelProviders.of(this, reminderModelFactory)
            .get(ReminderViewModel::class.java)

        reminderViewModel.listReminder.observe(this, Observer {
            val reminderDataAdapter = ReminderDataAdapter(this, 2)
            it?.let {
                reminderDataAdapter.submitList(it)
            }
            val linearLayout = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false)
            binding.reminderListRecycle.layoutManager = linearLayout
            binding.reminderListRecycle.adapter = reminderDataAdapter
        })
    }
}