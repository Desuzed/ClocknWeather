package com.desuzed.clocknweather.util

interface EntityMapper <Entity, DomainModel> {
    fun mapFromEntity (entity: Entity) : DomainModel
}