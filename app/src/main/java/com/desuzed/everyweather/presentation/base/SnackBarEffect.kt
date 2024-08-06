package com.desuzed.everyweather.presentation.base

import com.desuzed.everyweather.domain.model.result.QueryResult

/**
 * Если нужно показать снекбар на экране, то создать сайд-эффект и наследовать его также от этго
 * интерфейса
 **/
interface SnackBarEffect {
    val data: QueryResult
}