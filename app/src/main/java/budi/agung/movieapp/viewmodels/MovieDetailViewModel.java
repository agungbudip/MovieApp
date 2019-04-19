package budi.agung.movieapp.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import budi.agung.movieapp.models.Cast;
import budi.agung.movieapp.models.Movie;
import budi.agung.movieapp.models.MovieDetailGetResponse;
import budi.agung.movieapp.repositories.CastlRepository;
import budi.agung.movieapp.repositories.MovieDetailRepository;

public class MovieDetailViewModel extends ViewModel {

    public LiveData<MovieDetailGetResponse> getItems() {
        return MovieDetailRepository.getInstances().getItems();
    }

    public LiveData<List<Cast>> getCast() {
        return CastlRepository.getInstances().getItems();
    }

    public void getDetail(int movie_id) {
        MovieDetailRepository.getInstances().getDetail(movie_id);
    }

    public void getCredit(int movie_id) {
        CastlRepository.getInstances().getCredit(movie_id);
    }
}
