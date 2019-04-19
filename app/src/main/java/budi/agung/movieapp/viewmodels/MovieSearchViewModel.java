package budi.agung.movieapp.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import budi.agung.movieapp.models.Movie;
import budi.agung.movieapp.repositories.MovieSearchRepository;

public class MovieSearchViewModel extends ViewModel {
    public MutableLiveData<Integer> page = new MutableLiveData<>();

    public MutableLiveData<Integer> getPage() {
        return page;
    }

    public LiveData<List<Movie>> getItems() {
        return MovieSearchRepository.getInstances().getItems();
    }

    public void searchMovie(String query, int page) {
        this.page.setValue(page);
        MovieSearchRepository.getInstances().searchMovie(query, page);
    }
}
