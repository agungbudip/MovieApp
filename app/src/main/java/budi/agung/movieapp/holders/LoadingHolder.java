package budi.agung.movieapp.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.ybq.android.spinkit.SpinKitView;

import budi.agung.movieapp.R;

public class LoadingHolder extends RecyclerView.ViewHolder {
    public SpinKitView progressbar;

    public LoadingHolder(View itemView) {
        super(itemView);
        progressbar = itemView.findViewById(R.id.progressbar);
    }
}
