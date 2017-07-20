package com.countries.mycountries.network

import com.countries.mycountries.model.CountryList

import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by Dell on 7/16/2017.
 */

interface ApiInterface {


    @get:GET("tutorial/jsonparsetutorial.txt")
    val countries: Observable<CountryList>
}
