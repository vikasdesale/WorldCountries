
package com.countries.mycountries.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Worldpopulation {

    public Integer rank;
    public String country;
    public String population;
    public String flag;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Worldpopulation() {
    }

    /**
     * 
     * @param rank
     * @param flag
     * @param population
     * @param country
     */
    public Worldpopulation(Integer rank, String country, String population, String flag) {
        super();
        this.rank = rank;
        this.country = country;
        this.population = population;
        this.flag = flag;
    }

}
