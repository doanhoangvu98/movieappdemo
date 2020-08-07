package com.shin.movieapp.ui.movie

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.shin.movieapp.databinding.FragmentSingleMovieBinding
import com.shin.movieapp.model.favorite.Favorite
import com.shin.movieapp.model.reminder.Reminder
import com.shin.movieapp.model.user.UserDatabase
import com.shin.movieapp.network.MovieClient
import com.shin.movieapp.repository.favorite.FavoriteRepository
import com.shin.movieapp.repository.movie.MovieDetailsRepository
import com.shin.movieapp.repository.reminder.ReminderRepository
import com.shin.movieapp.ui.favorite.FavoriteModelFactory
import com.shin.movieapp.ui.favorite.FavoriteViewModel
import com.shin.movieapp.ui.reminder.ReminderModelFactory
import com.shin.movieapp.ui.reminder.ReminderReceiver
import com.shin.movieapp.ui.reminder.ReminderViewModel
import com.shin.movieapp.utils.DateTimeFormat
import java.util.*


class SingleMovieFragment : Fragment(){

    private var _binding: FragmentSingleMovieBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var movieRepository: MovieDetailsRepository
    private lateinit var favoriteRepository: FavoriteRepository
    private var movieid: Int? = 0
    private var movieTitle: String? = null
    lateinit var castAdapter: CastAndCrewAdapter
    private lateinit var favoriteViewModel: FavoriteViewModel
    lateinit var favorite: Favorite
    private var dateTime: String? = null
    private var calendar: Calendar? = null
    private var posterUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSingleMovieBinding.inflate(inflater, container, false)
        val view = binding.root
        // Get movie id
        movieid = arguments?.getInt("movie_id")
        movieTitle = arguments?.getString("movie_title")
        // Set title toolbar
        (activity as AppCompatActivity).supportActionBar?.title =  movieTitle

        val apiService = MovieClient.getData()
        movieRepository = MovieDetailsRepository(apiService)
        viewModel = SingleMovieViewModel(movieRepository, movieid!!)
        binding.viewModel = viewModel

        // Init favorite repo for check item is in favorite list
        val favoriteDao = UserDatabase.getInstance(requireContext()).favoriteDao()
        favoriteRepository = FavoriteRepository(favoriteDao)
        val factory = FavoriteModelFactory(favoriteRepository, this.requireActivity()!!.application)

        favoriteViewModel = ViewModelProviders.of(this, factory).get(FavoriteViewModel::class.java)

        binding.favoriteViewModel = favoriteViewModel

        binding.lifecycleOwner = this

        var linearLayout = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,
            false)
        binding.castCrewRecyclerview.layoutManager = linearLayout
        binding.castCrewRecyclerview.setHasFixedSize(true)

        viewModel.movieDetails.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.GONE
            binding.movieOverview.visibility = View.VISIBLE
            castAdapter = CastAndCrewAdapter(this.requireContext(), it.production_companies)
            posterUrl = it.poster_path
            binding.castCrewRecyclerview.adapter = castAdapter
        })

        favoriteViewModel.checkIsFavorite(movieid!!)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val favoriteDao = UserDatabase.getInstance(view.context).favoriteDao()

        val favoriteRepository = FavoriteRepository(favoriteDao)

        val factory = FavoriteModelFactory(favoriteRepository, this.requireActivity()!!.application)

        favoriteViewModel = ViewModelProviders.of(this, factory)
            .get(FavoriteViewModel::class.java)

        // Add favorite event
        binding.starIconImage.setOnClickListener {
            val favorite = Favorite(movieId = movieid!!, movieTitle = movieTitle!!)
            favoriteViewModel.favoriteEvent(favorite)
        }
        // Show message when finish event add or delete favorite
        favoriteViewModel.message.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, "${it.toString()}", Toast.LENGTH_SHORT).show()
        })
        binding.reminderButton.setOnClickListener {
            datePicker()
        }
    }

    fun datePicker(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(this.requireContext(),
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                timePicker(year, month, dayOfMonth)
            }, year, month, day)
        dpd.show()
    }

    fun timePicker(year: Int, month: Int, date: Int){
        val c = Calendar.getInstance()
        var mHour = c.get(Calendar.HOUR_OF_DAY)
        var mMinute = c.get(Calendar.MINUTE)

        val timeSetListener = TimePickerDialog.OnTimeSetListener{view, hour, minute ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.DAY_OF_MONTH, date)
            c.set(Calendar.HOUR_OF_DAY, hour)
            c.set(Calendar.MINUTE, minute)
            c.set(Calendar.SECOND, 0)
            dateTime = DateTimeFormat.formatDate(year, month, date, hour, minute)
            showAlertDialog(c)
        }
        TimePickerDialog(this.requireContext(), timeSetListener, mHour, mMinute, true).show()
    }

    private fun showAlertDialog(calendar: Calendar) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this.requireContext())
        alertDialog.setTitle("Reminder")
        alertDialog.setMessage("$movieTitle - $dateTime")

        // Init reminder
        val reminderDao = UserDatabase.getInstance(this.requireContext()).reminderDao()
        val reminderRepository = ReminderRepository(reminderDao)
        val factory = ReminderModelFactory(reminderRepository)
        val reminderViewModel = ViewModelProviders.of(this, factory)
            .get(ReminderViewModel::class.java)

        alertDialog.setPositiveButton("Create") {_, _ ->
            val reminder = Reminder(movieId = movieid!!, reminderTime = dateTime!! )
            reminderViewModel.eventReminder(reminder)
            setAlarm(calendar)
        }

        reminderViewModel.message.observe(viewLifecycleOwner, Observer {
            Toast.makeText(this.requireContext(), "$it", Toast.LENGTH_LONG).show()
        })

        alertDialog.setNegativeButton("Cancel") {_, _ -> }
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }

    // Set alarm reminder watch movie
    private fun setAlarm(calendar: Calendar){
        // Init code to make different request code for set multiple alarm
        var am = requireActivity().getSystemService(Context.ALARM_SERVICE)
                as AlarmManager
        var code = System.currentTimeMillis().toInt()

        val intent = Intent(this.activity, ReminderReceiver::class.java)
        intent.putExtra("extra", "on")
        intent.putExtra("movieId", movieid)
        intent.putExtra("posterUrl", posterUrl)
        intent.putExtra("movieTitle", movieTitle)

        var pendingIntent = PendingIntent.getBroadcast(requireActivity(), code,
        intent, PendingIntent.FLAG_UPDATE_CURRENT)

        am.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

//        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10* 1000, pendingIntent)

    }
}