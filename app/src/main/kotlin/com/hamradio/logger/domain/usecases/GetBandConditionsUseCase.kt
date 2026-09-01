package com.hamradio.logger.domain.usecases

import com.hamradio.logger.data.network.BandConditions
import com.hamradio.logger.data.network.BandDataService

class GetBandConditionsUseCase(
    private val bandDataService: BandDataService
) {
    suspend operator fun invoke(): BandConditions? {
        return try {
            val response = bandDataService.getSolarConditions()
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
