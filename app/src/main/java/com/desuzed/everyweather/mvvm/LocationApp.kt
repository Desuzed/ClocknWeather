package com.desuzed.everyweather.mvvm
class LocationApp (val lat : Float, val lon : Float) {
    constructor(lat: Float, lon: Float, _cityName : String, _region : String, _country : String ) : this(lat, lon){
        this.cityName = _cityName
        this.region = _region
        this.country = _country
    }
    private var cityName : String = ""
    private var region : String = ""
    private var country : String = ""
    override fun toString(): String {
        return "$lat, $lon"
    }

//    fun toStringInfoFields(): String {
//        return "$cityName, $region, $country"
//    }
//    fun hasLocationInfo () : Boolean = cityName.isNotEmpty() && region.isNotEmpty() && country.isNotEmpty()

}