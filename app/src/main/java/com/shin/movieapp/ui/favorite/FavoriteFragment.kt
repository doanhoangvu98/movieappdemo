package com.shin.movieapp.ui.favorite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.shin.movieapp.databinding.FragmentFavoriteBinding
import com.shin.movieapp.model.user.UserDatabase
import com.shin.movieapp.repository.favorite.FavoriteRepository
import kotlinx.android.synthetic.main.fragment_favorite.*

class FavFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var favoriteRepository: FavoriteRepository
    lateinit var favoriteDataAdapter: FavoriteDataAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val view = binding.root

        val favoriteDao = UserDatabase.getInstance(view.context).favoriteDao()

        favoriteRepository = FavoriteRepository(favoriteDao)

        val factory = FavoriteModelFactory(favoriteRepository, this.requireActivity()!!.application)

        viewModel = ViewModelProviders.of(this, factory).get(FavoriteViewModel::class.java)

        viewModel.favoriteList.observe(viewLifecycleOwner, Observer {
            binding.loading.visibility = View.GONE
            binding.favoriteRecyclerview.visibility = View.VISIBLE
            favoriteDataAdapter = FavoriteDataAdapter(this.requireContext(), it)
            var linearLayout = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,
                false)
            binding.favoriteRecyclerview.layoutManager = linearLayout
            binding.favoriteRecyclerview.adapter = favoriteDataAdapter
        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchViewText.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search_view_text.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                favoriteDataAdapter.filter.filter(newText)
                return false
            }
        })
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchViewText.getWindowToken(), 0)
    }

}