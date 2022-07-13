package com.desuzed.everyweather.view.fragments.location.main

import com.desuzed.everyweather.data.room.FavoriteLocationDto

sealed interface LocationUserInteraction {
    object MyLocation : LocationUserInteraction
    class FavoriteLocation(val favoriteLocationDto: FavoriteLocationDto) : LocationUserInteraction
    class DeleteFavoriteLocation(val favoriteLocationDto: FavoriteLocationDto) :
        LocationUserInteraction

    object FindOnMap : LocationUserInteraction
    object FindByQuery : LocationUserInteraction
}