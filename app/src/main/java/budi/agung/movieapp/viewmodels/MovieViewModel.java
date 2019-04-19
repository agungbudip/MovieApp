package budi.agung.movieapp.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import budi.agung.movieapp.models.Movie;
import budi.agung.movieapp.models.MovieModel;
import budi.agung.movieapp.repositories.MovieNowPlayingRepository;
import budi.agung.movieapp.repositories.MoviePopularRepository;

public class MovieViewModel extends ViewModel {

    public LiveData<List<Movie>> getItems(int category) {
        if (category == MovieModel.CATEGORY_NOW_PLAYING) {
            return MovieNowPlayingRepository.getInstances().getItems();
        } else if (category == MovieModel.CATEGORY_POPULAR) {
            return MoviePopularRepository.getInstances().getItems();
        }
        return null;
    }

    public void loadData(final int page, final int category) {
        if (category == MovieModel.CATEGORY_NOW_PLAYING) {
            MovieNowPlayingRepository.getInstances().loadData(page);
        } else if (category == MovieModel.CATEGORY_POPULAR) {
            MoviePopularRepository.getInstances().loadData(page);
        }
    }
}
