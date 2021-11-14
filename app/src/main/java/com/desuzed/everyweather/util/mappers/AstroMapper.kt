package com.desuzed.everyweather.util.mappers

import com.desuzed.everyweather.mvvm.model.Astro
import com.desuzed.everyweather.network.dto.AstroDto

class AstroMapper : EntityMapper<AstroDto, Astro> {
    override fun mapFromEntity(entity: AstroDto): Astro {
        return Astro(entity.sunrise, entity.sunset, entity.moonrise, entity.moonset)
    }
}