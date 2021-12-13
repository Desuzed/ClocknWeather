package com.desuzed.everyweather.data.network.dto.weatherApi.mappers

interface EntityMapper <Entity, DomainModel> {
    fun mapFromEntity (entity: Entity) : DomainModel
}