package com.countries.mycountries.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.countries.mycountries.R;
import com.countries.mycountries.adapters.CountryAdapter;
import com.countries.mycountries.model.CountryList;
import com.countries.mycountries.model.Worldpopulation;
import com.countries.mycountries.network.ApiClient;
import com.countries.mycountries.network.ApiInterface;
import com.countries.mycountries.network.NetworkUtil;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    CountryAdapter mAdapter;
    @Nullable
    @BindView(R.id.country_empty)
    TextView countryEmpty;
    @BindView(R.id.progressbar)
    CircularProgressBar progressBar;

    private ArrayList<Worldpopulation> mWorldPopulations;
    private CompositeDisposable mCompositeDisposable;
    private final String Country_Parse = "v";
    private Disposable clickDispose;
    private ArrayList<CountryList> countryList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mCompositeDisposable = new CompositeDisposable();
        if (!NetworkUtil.isNetworkConnected(this)) {
            mRecyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            countryEmpty.setText(R.string.no_network_found);
            countryEmpty.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            countryEmpty.setVisibility(View.GONE);
            if (savedInstanceState == null) {
                loadJSON();
            } else {
                mWorldPopulations = Parcels.unwrap(savedInstanceState.getParcelable(Country_Parse));
                setupAdapter(mWorldPopulations);
            }
        }
    }

    private void loadJSON() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        mCompositeDisposable.add(apiService.getCountries()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        );
    }

    private void handleResponse(@NonNull CountryList countryLists) {
        mWorldPopulations = (ArrayList<Worldpopulation>) countryLists.getWorldpopulation();
        if (mWorldPopulations != null) {
            setupAdapter(mWorldPopulations);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            countryEmpty.setText(R.string.data_not_found);
            countryEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void handleClick(Integer position) {

        Bundle args = new Bundle();
        args.putString("imgURL", mWorldPopulations.get(position).getFlag());
        CountryDialogFragment dialogFragment = new CountryDialogFragment();
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "TAG");


    }

    private void handleError(Throwable error) {

        //  Toast.makeText(this, "Error " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        countryEmpty.setText(R.string.server_error);
        countryEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (mWorldPopulations != null) {
            outState.putParcelable(Country_Parse, Parcels.wrap(mWorldPopulations));
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCompositeDisposable != null && clickDispose != null) {
            mCompositeDisposable.dispose();
            clickDispose.dispose();
        }
    }


    public void setupAdapter(ArrayList<Worldpopulation> world) {
        progressBar.setVisibility(View.GONE);
        mAdapter = new CountryAdapter(this, world);
        clickDispose = mAdapter.getItemClickSignal()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleClick, this::handleError);
        mRecyclerView.setAdapter(mAdapter);
    }
}
