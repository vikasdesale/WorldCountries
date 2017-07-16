package com.countries.mycountries.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.countries.mycountries.R;
import com.countries.mycountries.model.Worldpopulation;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.countries.mycountries.R.drawable.vikas1;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {

    public ClickListener clickListener;
    Context mContext;
    private ArrayList<Worldpopulation> mWorldPopulations;
    public CountryAdapter(Context context, ArrayList<Worldpopulation> worldpopulations) {
        mContext=context;
        mWorldPopulations = worldpopulations;
}


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Worldpopulation worldpopulation=mWorldPopulations.get(position);
        String image=worldpopulation.getFlag();

        //Got Advantages why to use Glide over picasso that's why replaced picasso.
        Glide.with(mContext).load(image)
                .thumbnail(0.1f)
                .error(vikas1)
                .crossFade() //animation
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .skipMemoryCache(true)
                .into(holder.countryImage);
    }

    @Override
    public int getItemCount() {
        return mWorldPopulations.size();
    }
    public interface ClickListener {
         void itemClicked(View view, int position,CountryAdapter.ViewHolder vh);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.countryImage)
        ImageView countryImage;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            ViewHolder vh=new ViewHolder(view);
            if (clickListener != null) {
                clickListener.itemClicked(view,getAdapterPosition(), vh);
            }
        }
    }
}
