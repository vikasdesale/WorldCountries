package com.countries.mycountries.model


import org.parceler.Parcel



@Parcel
class Worldpopulation {

     var rank: Int? = null
     var country: String? = null
     var population: String? = null
     var flag: String? = null

    /**
     * No args constructor for use in serialization

     */
    constructor() {}

    /**

     * @param rank
     * *
     * @param flag
     * *
     * @param population
     * *
     * @param country
     */
    constructor(rank: Int?, country: String, population: String, flag: String) : super() {
        this.rank = rank
        this.country = country
        this.population = population
        this.flag = flag
    }

}
