package com.desuzed.everyweather.data.mapper

interface DomainMapper<DomainModel, Entity> {
    fun mapFromDomain(domain: DomainModel): Entity
}