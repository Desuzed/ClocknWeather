package com.desuzed.everyweather.domain.interactor

import com.desuzed.everyweather.domain.model.location.FavoriteLocation
import com.desuzed.everyweather.domain.model.location.geo.ResultGeo
import com.desuzed.everyweather.domain.model.result.ActionType
import com.desuzed.everyweather.domain.model.result.FetchResult
import com.desuzed.everyweather.domain.model.result.QueryResult
import com.desuzed.everyweather.domain.model.settings.Lang
import com.desuzed.everyweather.domain.model.weather.Location
import com.desuzed.everyweather.domain.model.weather.WeatherContent
import com.desuzed.everyweather.domain.repository.local.LocationDb
import com.desuzed.everyweather.domain.repository.remote.RemoteDataRepository
import com.desuzed.everyweather.domain.repository.settings.SystemSettingsRepository
import com.desuzed.everyweather.domain.util.LatLngKeyGenerator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class LocationInteractor(
    private val remoteDataRepository: RemoteDataRepository,
    private val latLngKeyGenerator: LatLngKeyGenerator,
    private val locationDb: LocationDb,
    private val systemSettingsRepository: SystemSettingsRepository,
) {
    suspend fun fetchGeocodingResultOrError(query: String): ResultGeo {
        val lang =
            systemSettingsRepository.lang.firstOrNull()?.id?.lowercase() ?: Lang.EN.lang.lowercase()
        return when (val fetchResult = remoteDataRepository.forwardGeocoding(query, lang)) {
            is FetchResult.Success -> ResultGeo(fetchResult.body, null)
            is FetchResult.Failure -> ResultGeo(
                geoData = null,
                queryResult = QueryResult(
                    code = fetchResult.error.code,
                    query = query,
                    actionType = ActionType.RETRY,
                ),
            )
        }
    }

    suspend fun saveLocationToDb(content: WeatherContent?): Boolean {
        content ?: return false
        return locationDb.insert(content.location)
    }

    suspend fun isLocationSaved(location: Location): Boolean {
        val latLonKey = latLngKeyGenerator.generateKey(location)
        return locationDb.containsPrimaryKey(latLonKey)
    }

    suspend fun updateLocation(location: FavoriteLocation): Boolean {
        return locationDb.updateLocation(location)
    }

    suspend fun deleteFavoriteLocation(location: FavoriteLocation): Boolean {
        return locationDb.deleteItem(location)
    }

    fun getAllLocations(): Flow<List<FavoriteLocation>> = locationDb.getAllLocations()
}