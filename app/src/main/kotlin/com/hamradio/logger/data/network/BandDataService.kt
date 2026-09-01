package com.hamradio.logger.data.network

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET

data class BandConditions(
    @SerializedName("solarflux")
    val solarFlux: Int?,
    @SerializedName("sunspots")
    val sunSpots: Int?,
    @SerializedName("kindex")
    val kIndex: Int?,
    @SerializedName("recommended_band")
    val recommendedBand: String?,
    @SerializedName("bands_open")
    val bandsOpen: List<String>?
)

interface BandDataService {

    @GET("solargmi.php")
    suspend fun getSolarConditions(): Response<BandConditions>
}
