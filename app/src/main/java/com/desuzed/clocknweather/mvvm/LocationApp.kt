package com.desuzed.clocknweather.mvvm
//TODO rename class
class LocationApp (val lat : Float, val lon : Float) {
    constructor(lat: Float, lon: Float, _cityName : String, _region : String, _country : String ) : this(lat, lon){
        this.cityName = _cityName
        this.region = _region
        this.country = _country
    }
    var cityName : String = ""
    var region : String = ""
    var country : String = ""
    override fun toString(): String {
        return "$lat,$lon"
    }
    fun hasLocationInfo () : Boolean = cityName.isNotEmpty() && region.isNotEmpty() && country.isNotEmpty()

}