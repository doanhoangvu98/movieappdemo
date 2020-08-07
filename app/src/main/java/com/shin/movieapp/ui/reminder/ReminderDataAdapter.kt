package com.shin.movieapp.ui.reminder

import android.app.Activity
import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.shin.movieapp.R
import com.shin.movieapp.model.reminder.Reminder
import com.shin.movieapp.model.user.UserDatabase
import com.shin.movieapp.network.IMAGE_URL
import com.shin.movieapp.network.MovieClient
import com.shin.movieapp.repository.movie.MovieDetailsRepository
import com.shin.movieapp.repository.reminder.ReminderRepository
import com.shin.movieapp.ui.movie.SingleMovieViewModel
import kotlinx.android.synthetic.main.reminder_list_item.view.*
import kotlinx.android.synthetic.main.reminder_list_item_large.view.*
import kotlinx.android.synthetic.main.reminder_list_item_large.view.reminder_time
import java.util.*

class ReminderDataAdapter(val context: Context, val viewType: Int) :
    ListAdapter<Reminder, RecyclerView.ViewHolder>(ReminderDiffCallBack()){

    val REMINDER_SETTING_TYPE = 1
    val REMINDER_DRAWER_TYPE = 2
    val LIMIT_ITEM_DISPLAY  = 2

    var listReminder = mutableListOf<Reminder>()
    var mContext: Context? = null

    init {
        this.mContext = context
    }

    class SettingViewHolder(val view: View) : ViewHolder(view){
        fun bind(item: Reminder, context: Context){
            val apiService = MovieClient.getData()
            var movieRepository = MovieDetailsRepository(apiService)
            var movieViewModel = SingleMovieViewModel(movieRepository, item.movieId)
            // Get details movie
            movieViewModel.movieDetails.observe(view.context as LifecycleOwner, Observer {

                var moviePosterUrl = IMAGE_URL + it.poster_path
                Glide.with(context).load(moviePosterUrl).into(itemView.poster_image)
                itemView.movie_name.text = it.original_title
                itemView.rating.text = it.vote_average.toString() + "/10"
                itemView.reminder_time.text = item.reminderTime

                val movie = it
                // View movie details
                itemView.arrow_icon.setOnClickListener{
                    val navController: NavController = Navigation.findNavController(it)
                    var bundle = bundleOf("movie_id" to movie.id,
                        "movie_title" to movie.original_title)
                    navController.navigate(R.id.action_reminderFragment_to_singleMovieFragment, bundle)
                }
            })
        }
    }

    class DrawerViewHolder(val view: View) : ViewHolder(view){
        fun bind(item: Reminder, context: Context){
            val apiService = MovieClient.getData()
            var movieRepository = MovieDetailsRepository(apiService)
            var movieViewModel = SingleMovieViewModel(movieRepository, item.movieId)
            // Get details movie
            movieViewModel.movieDetails.observe(view.context as LifecycleOwner, Observer {
                val cal = Calendar.getInstance()
                val dateParts: List<String> = it.release_date.split("-")
                itemView.movie_info.text = it.original_title + " - " + dateParts[0] + " - " +
                        it.vote_average + "/10"
                itemView.reminder_time.text = item.reminderTime
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View
        if(viewType == REMINDER_SETTING_TYPE){
            view = layoutInflater.inflate(R.layout.reminder_list_item_large, parent,
                false)
            return SettingViewHolder(view)
        } else if(viewType == REMINDER_DRAWER_TYPE){
            view = layoutInflater.inflate(R.layout.reminder_list_item, parent,
                false)
            return DrawerViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.reminder_list_item_large, parent,
                false)
            return SettingViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        if(super.getItemCount() > 2 && viewType == REMINDER_DRAWER_TYPE){
            return LIMIT_ITEM_DISPLAY
        } else {
            return super.getItemCount()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == REMINDER_SETTING_TYPE){
            (holder as SettingViewHolder).bind(super.getItem(position), context)
        } else if(getItemViewType(position) == REMINDER_DRAWER_TYPE){
            (holder as DrawerViewHolder).bind(super.getItem(position), context)
        } else {
            (holder as SettingViewHolder).bind(super.getItem(position), context)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    fun delete(position: Int){
        val reminderDao = UserDatabase.getInstance(context).reminderDao()
        val reminderRepository = ReminderRepository(reminderDao)
        val reminderId = getItem(position).id
        val reminderModelFactory = ReminderModelFactory(reminderRepository)
        val reminderViewModel = ViewModelProviders.of(context as FragmentActivity, reminderModelFactory)
            .get(ReminderViewModel::class.java)
        reminderViewModel.deleteReminder(reminderId!!)
    }

    class ReminderDiffCallBack : DiffUtil.ItemCallback<Reminder>(){
        override fun areContentsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areItemsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
            return oldItem == newItem
        }
    }

}