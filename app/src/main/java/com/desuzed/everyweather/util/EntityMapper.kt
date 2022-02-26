package com.desuzed.everyweather.util

interface EntityMapper <Entity, DomainModel> {
    fun mapFromEntity (entity: Entity) : DomainModel
}