package budi.agung.movieapp.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.util.List;

import budi.agung.movieapp.models.Cast;
import budi.agung.movieapp.models.CreditGetResponse;
import budi.agung.movieapp.models.MovieDetailGetResponse;
import budi.agung.movieapp.utilities.ApiClient;
import budi.agung.movieapp.utilities.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CastlRepository {
    private static CastlRepository instances;

    private MutableLiveData<List<Cast>> items;

    public static CastlRepository getInstances() {
        if (instances == null) {
            instances = new CastlRepository();
            instances.items = new MutableLiveData<>();
        }
        return instances;
    }

    public LiveData<List<Cast>> getItems() {
        return items;
    }

    public void getCredit(final int movie_id) {
        ApiInterface client = ApiClient.getClient().create(ApiInterface.class);
        client.getMovieCredits(movie_id, "c6a8ab2a50d10a5c5a194ecd0f637f8b").enqueue(new Callback<CreditGetResponse>() {
            @Override
            public void onResponse(Call<CreditGetResponse> call, Response<CreditGetResponse> response) {
                items.setValue(response.body().getCast());
            }

            @Override
            public void onFailure(Call<CreditGetResponse> call, Throwable t) {
                Log.d("TESTRESULT", t.getMessage() + "");
            }
        });
    }
}
