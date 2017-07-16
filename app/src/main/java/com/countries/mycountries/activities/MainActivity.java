package com.countries.mycountries.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.countries.mycountries.R;
import com.countries.mycountries.adapters.CountryAdapter;
import com.countries.mycountries.model.CountryList;
import com.countries.mycountries.model.Worldpopulation;
import com.countries.mycountries.network.ApiClient;
import com.countries.mycountries.network.ApiInterface;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    CountryAdapter mAdapter;
    private ArrayList<Worldpopulation> mWorldPopulations= new ArrayList<>();
    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mCompositeDisposable = new CompositeDisposable();
        loadJSON();

    }
    private void loadJSON() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        mCompositeDisposable.add(apiService.getCountries()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }

    private void handleResponse(CountryList countryLists) {

        mWorldPopulations= (ArrayList<Worldpopulation>) countryLists.getWorldpopulation();
        mAdapter = new CountryAdapter(this,mWorldPopulations);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void handleError(Throwable error) {

        Toast.makeText(this, "Error "+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }
}
