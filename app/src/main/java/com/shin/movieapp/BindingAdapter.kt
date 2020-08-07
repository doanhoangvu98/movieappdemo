package com.shin.movieapp

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.shin.movieapp.network.IMAGE_URL

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String?){
    imageUrl?.let{
        val moviePosterURL = IMAGE_URL + imageUrl
        Glide.with(view.context).load(moviePosterURL).into(view)
    }
}

@BindingAdapter("imgStarDrawable")
fun loadIcon(view: ImageView, isFavorite: Boolean){
    if(isFavorite){
        view.setImageResource(R.drawable.ic_star_inline_24dp)
    } else {
        view.setImageResource(R.drawable.ic_star_outline_white_24dp)
    }
}