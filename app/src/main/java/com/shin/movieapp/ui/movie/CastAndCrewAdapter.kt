package com.shin.movieapp.ui.movie

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shin.movieapp.R
import com.shin.movieapp.model.Movie.ProductionCompany
import com.shin.movieapp.network.IMAGE_URL
import kotlinx.android.synthetic.main.cast_crew_list_item.view.*
import kotlinx.android.synthetic.main.fragment_single_movie.view.*

class CastAndCrewAdapter(public val context: Context, private val companies: List<ProductionCompany>) :
    RecyclerView.Adapter<CastAndCrewAdapter.Viewholder>() {

    class Viewholder(val view: View) : RecyclerView.ViewHolder(view){
        fun bind(item: ProductionCompany, context: Context){
            itemView.name.text = item.origin_country
            Log.i("name", "${item.origin_country}")
            if(item.logo_path == null){
                val moviePosterURL = "https://www.ictskillnet.ie/wp-content/uploads/2019/01/placeholder-company-5f3438282f524800f1d49cd2921bb0a56101e1aa16097ebd313b64778fc7c4bd.png"
                Glide.with(context).load(moviePosterURL).into(itemView.profile_image)
            } else {
                val moviePosterURL = IMAGE_URL + item.logo_path
                Glide.with(context).load(moviePosterURL).into(itemView.profile_image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): Viewholder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cast_crew_list_item, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int)
            = holder.bind(companies[position], context)

    override fun getItemCount() = companies.size
}