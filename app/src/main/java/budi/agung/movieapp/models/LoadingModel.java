package budi.agung.movieapp.models;

public class LoadingModel implements Movie {
    private int page;

    public LoadingModel(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }
}
