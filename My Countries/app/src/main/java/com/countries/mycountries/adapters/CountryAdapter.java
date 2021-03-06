package com.countries.mycountries.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.countries.mycountries.R;
import com.countries.mycountries.model.Worldpopulation;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static com.countries.mycountries.R.drawable.error;
import static com.countries.mycountries.R.drawable.loading;


public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {

    private final PublishSubject<Integer> onClickSubject = PublishSubject.create();

    @NonNull
    public Observable<Integer> getItemClickSignal() {
        return onClickSubject;
    }

    private final Context mContext;
    private View view;
    private final ArrayList<Worldpopulation> mWorldPopulations;
    public CountryAdapter(Context context, ArrayList<Worldpopulation> worldpopulations) {
        mContext=context;
        mWorldPopulations = worldpopulations;
}


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Worldpopulation worldpopulation=mWorldPopulations.get(position);
        String image=worldpopulation.getFlag();

        //Got Advantages why to use Glide over picasso that's why replaced picasso.
        Glide.with(mContext).load(image)
                .thumbnail(0.1f)
                .error(error)
                .placeholder(loading)
                .crossFade() //animation
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .skipMemoryCache(true)
                .into(holder.countryImage != null ? holder.countryImage : null);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
      return mWorldPopulations == null ? 0 : mWorldPopulations.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder  {

        @Nullable
        @BindView(R.id.countryImage)
        ImageView countryImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, view);
            RxView.clicks(itemView)
                    .map(__ -> getAdapterPosition())
                    .subscribe(onClickSubject);
        }



    }
   

}
