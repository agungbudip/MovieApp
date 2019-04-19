package budi.agung.movieapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import budi.agung.movieapp.R;
import budi.agung.movieapp.models.Cast;

public class CastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Cast> items;
    private LayoutInflater inflater;

    public CastAdapter(Context context, List<Cast> items) {
        this.context = context;
        this.items = items;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.row_cast, viewGroup, false);
        return new CastHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Cast item = items.get(i);
        CastHolder cHolder = (CastHolder) viewHolder;

        cHolder.tvName.setText("- " + item.getName());
        cHolder.tvCharacter.setText(item.getCharacter());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CastHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCharacter;

        public CastHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvCharacter = itemView.findViewById(R.id.tvCharacter);
        }
    }

}
