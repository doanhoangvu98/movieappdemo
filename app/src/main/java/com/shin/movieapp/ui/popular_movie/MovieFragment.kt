package com.shin.movieapp.ui.popular_movie

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.shin.movieapp.R
import com.shin.movieapp.databinding.FragmentMovieBinding
import com.shin.movieapp.network.MovieApi
import com.shin.movieapp.network.MovieClient
import com.shin.movieapp.utils.TypeMovie
import kotlinx.android.synthetic.main.fragment_movie.*


class MovieFragment : Fragment() {

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MovieFragmentViewModel
    lateinit var movieRepository: MoviePageList
    private var isGridView: Boolean = false
    var typeMovie: String = "popular_movie"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        val view = binding.root

        savedInstanceState?.getBoolean("isgrid")?.let {
            isGridView = it
        }

        // get type call api movie from setting fragment
        val type = requireActivity().intent.extras?.getString("type")
        type?.let {
            typeMovie = type
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = TypeMovie.MovieTitle(type)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get popular movie
        val movieApi: MovieApi = MovieClient.getData()
        movieRepository = MoviePageList(movieApi, typeMovie)
        viewModel = getViewModel()
        switchLayout(!isGridView)
        setHasOptionsMenu(true)

        // Pull refresh movie data
        binding.swipeRefresh.setOnRefreshListener{
            switchLayout(!isGridView)
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("isgrid", isGridView)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.nav_header_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item1) {
            switchIcon(item, isGridView)
            switchLayout(isGridView)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun switchLayout(isGridView: Boolean) {
        if (isGridView) {
            val movieAdapter = MoviePageListAdapter(requireContext(), 1)
            var linearLayout =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            list_movie_recycle.layoutManager = linearLayout
            list_movie_recycle.adapter = movieAdapter
            viewModel.movePageList.observe(viewLifecycleOwner, Observer {
                movieAdapter.submitList(it)
                binding.loading.visibility = View.GONE
                binding.viewList.visibility = View.VISIBLE
            })
            this.isGridView = !isGridView
        } else {
            val movieAdapter = MoviePageListAdapter(requireContext(), 2)
            val gridLayoutManager = GridLayoutManager(requireContext(), 2)
            list_movie_recycle.layoutManager = gridLayoutManager
            list_movie_recycle.adapter = movieAdapter
            viewModel.movePageList.observe(viewLifecycleOwner, Observer {
                movieAdapter.submitList(it)
                binding.loading.visibility = View.GONE
                binding.viewList.visibility = View.VISIBLE
            })
            this.isGridView = !isGridView
        }
    }

    private fun switchIcon(item: MenuItem, isGridView: Boolean) {
        if (isGridView) {
            item.setIcon(R.drawable.ic_grid_white_24dp)
        } else {
            item.setIcon(R.drawable.ic_list_white_24dp)
        }
    }

    private fun getViewModel(): MovieFragmentViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieFragmentViewModel(movieRepository) as T
            }
        })[MovieFragmentViewModel::class.java]
    }
}
