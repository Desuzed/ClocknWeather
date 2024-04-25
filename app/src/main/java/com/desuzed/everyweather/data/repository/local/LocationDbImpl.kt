package com.desuzed.everyweather.data.repository.local

import com.desuzed.everyweather.data.mapper.location.FavoriteLocationMapper
import com.desuzed.everyweather.data.room.FavoriteLocationDAO
import com.desuzed.everyweather.data.room.FavoriteLocationDto
import com.desuzed.everyweather.domain.model.location.FavoriteLocation
import com.desuzed.everyweather.domain.model.weather.Location
import com.desuzed.everyweather.domain.repository.local.LocationDb
import com.desuzed.everyweather.domain.util.LatLngKeyGenerator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class LocationDbImpl(
    private val favoriteLocationDAO: FavoriteLocationDAO,
    private val favoriteLocationMapper: FavoriteLocationMapper,
    private val latLngKeyGenerator: LatLngKeyGenerator,
    private val dispatcher: CoroutineDispatcher,
) : LocationDb {

    override suspend fun insert(location: Location): Boolean =
        withContext(dispatcher) {
            val latLon = latLngKeyGenerator.generateKey(location)
            val favoriteLocationDto = FavoriteLocationDto.buildFavoriteLocationObj(
                location = location,
                latLon = latLon,
            )
            val inserted = favoriteLocationDAO.insert(favoriteLocationDto)
            inserted > 0
        }

    override suspend fun deleteItem(favoriteLocation: FavoriteLocation): Boolean =
        withContext(dispatcher) {
            val deleted = favoriteLocationDAO.deleteItem(getEntity(favoriteLocation))
            deleted > 0
        }

    override suspend fun updateLocation(favoriteLocation: FavoriteLocation): Boolean =
        withContext(dispatcher) {
            val updated = favoriteLocationDAO.updateLocation(getEntity(favoriteLocation))
            updated > 0
        }


    override suspend fun containsPrimaryKey(latLon: String): Boolean =
        withContext(dispatcher) {
            favoriteLocationDAO.containsPrimaryKey(latLon)
        }

    override fun getAllLocations(): Flow<List<FavoriteLocation>> =
        favoriteLocationDAO.getAlphabetizedLocations()
            .map { it.map(favoriteLocationMapper::mapFromEntity) }

    private fun getEntity(favoriteLocation: FavoriteLocation) =
        favoriteLocationMapper.mapFromDomain(favoriteLocation)
}