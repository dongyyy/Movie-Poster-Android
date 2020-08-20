package com.github.libliboom.movieposter.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.libliboom.movieposter.R
import com.github.libliboom.movieposter.databinding.FragmentMovieBinding
import com.github.libliboom.movieposter.viewmodel.MovieViewModel

class MovieFragment : Fragment() {

    private val movieViewModel by lazy {
        ViewModelProvider(this)[MovieViewModel::class.java]
    }

    private lateinit var binding: FragmentMovieBinding

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieBinding.inflate(inflater, container, false)
        binding.viewModel = movieViewModel
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()

        movieViewModel.run {
            fetchMovie()
            downloadedSize.observe(
                viewLifecycleOwner,
                Observer { size ->
                    handler.post {
                        binding.size.text = "$size KB"
                    }
                }
            )
        }
    }

    private fun initViewModel() {
        movieViewModel.apply {
            setHandler(handler)
            setCompleteCallback(::onDownloadStarted, ::onDownloadFinished)
            loadMovie()
        }
    }

    private fun onDownloadStarted() {
        binding.state.text = getString(R.string.movie_start_to_download)
        binding.size.visibility = View.VISIBLE
    }

    private fun onDownloadFinished() {
        handler.post {
            binding.title.text = movieViewModel.getTitle()
            binding.state.text = getString(R.string.movie_finish_downloading)
            val bitmap = movieViewModel.run { getBitmap(currentPath.value!!) }
            binding.poster.setImageBitmap(bitmap)
        }

        handler.postDelayed({ binding.size.visibility = View.GONE }, 100)
    }
}
