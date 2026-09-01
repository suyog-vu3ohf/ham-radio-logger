package com.hamradio.logger.domain.logic

import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.math.pow

object BandPredictionLogic {

    data class BandOpening(
        val band: String,
        val frequency: String,
        val optimal: Boolean,
        val confidence: Float // 0-1
    )

    /**
     * Predict which bands are open based on solar activity and local time
     */
    fun predictOpenBands(
        solarFlux: Int,
        kIndex: Int,
        sunSpots: Int,
        currentTime: LocalDateTime = LocalDateTime.now()
    ): List<BandOpening> {
        val openBands = mutableListOf<BandOpening>()
        val hour = currentTime.hour
        
        // Solar activity levels
        val quietConditions = solarFlux < 70
        val normalConditions = solarFlux in 70..100
        val activeConditions = solarFlux in 101..150
        val extremeConditions = solarFlux > 150
        
        // Daytime bands (better during day: 10-16 UTC)
        val isDaytime = hour in 10..16
        
        val confidenceMultiplier = calculateConfidence(solarFlux, kIndex)
        
        when {
            extremeConditions -> {
                // 10m, 12m, 15m, 17m, 20m wide open
                openBands.add(BandOpening("10m", "28.0-29.7 MHz", true, 0.95f * confidenceMultiplier))
                openBands.add(BandOpening("12m", "24.89-24.99 MHz", true, 0.90f * confidenceMultiplier))
                openBands.add(BandOpening("15m", "21.0-21.45 MHz", true, 0.92f * confidenceMultiplier))
                openBands.add(BandOpening("17m", "18.068-18.168 MHz", true, 0.88f * confidenceMultiplier))
                openBands.add(BandOpening("20m", "14.0-14.35 MHz", true, 0.85f * confidenceMultiplier))
            }
            activeConditions -> {
                openBands.add(BandOpening("10m", "28.0-29.7 MHz", isDaytime, 0.85f * confidenceMultiplier))
                openBands.add(BandOpening("15m", "21.0-21.45 MHz", true, 0.88f * confidenceMultiplier))
                openBands.add(BandOpening("20m", "14.0-14.35 MHz", true, 0.80f * confidenceMultiplier))
                openBands.add(BandOpening("40m", "7.0-7.3 MHz", !isDaytime, 0.75f * confidenceMultiplier))
            }
            normalConditions -> {
                openBands.add(BandOpening("20m", "14.0-14.35 MHz", true, 0.85f * confidenceMultiplier))
                openBands.add(BandOpening("40m", "7.0-7.3 MHz", true, 0.80f * confidenceMultiplier))
                openBands.add(BandOpening("80m", "3.5-4.0 MHz", !isDaytime, 0.78f * confidenceMultiplier))
            }
            else -> {
                // Quiet conditions - lower bands work
                openBands.add(BandOpening("40m", "7.0-7.3 MHz", true, 0.75f * confidenceMultiplier))
                openBands.add(BandOpening("80m", "3.5-4.0 MHz", true, 0.80f * confidenceMultiplier))
                openBands.add(BandOpening("160m", "1.8-2.0 MHz", !isDaytime, 0.70f * confidenceMultiplier))
            }
        }
        
        return openBands.sortedByDescending { it.confidence }
    }

    /**
     * Calculate confidence score based on solar conditions
     */
    private fun calculateConfidence(solarFlux: Int, kIndex: Int): Float {
        var confidence = 0.5f
        
        // Solar flux component (0-1)
        confidence += (solarFlux.toFloat() / 300f).coerceIn(0f, 0.3f)
        
        // K-index component (higher K = worse conditions)
        val kIndexPenalty = (kIndex.toFloat() / 9f).coerceIn(0f, 0.2f)
        confidence -= kIndexPenalty
        
        return confidence.coerceIn(0f, 1f)
    }

    /**
     * Get the best band for current time and location
     */
    fun getBestBand(
        solarFlux: Int,
        kIndex: Int,
        sunSpots: Int,
        latitude: Double,
        currentTime: LocalDateTime = LocalDateTime.now()
    ): BandOpening? {
        val openBands = predictOpenBands(solarFlux, kIndex, sunSpots, currentTime)
        
        // Adjust for latitude
        val adjustedBands = openBands.map { band ->
            val latitudeAdjustment = calculateLatitudeAdjustment(latitude, band.band)
            band.copy(confidence = band.confidence * latitudeAdjustment)
        }
        
        return adjustedBands.maxByOrNull { it.confidence }
    }

    /**
     * Adjust band suitability based on geographic latitude
     */
    private fun calculateLatitudeAdjustment(latitude: Double, band: String): Float {
        val absLat = kotlin.math.abs(latitude)
        return when (band) {
            "10m", "15m" -> 1.0f + (absLat / 90f) * 0.1f // Better at equator
            "20m" -> 1.0f // Good everywhere
            "40m", "80m" -> 0.8f + (absLat / 90f) * 0.2f // Better at higher latitudes
            "160m" -> 0.6f + (absLat / 90f) * 0.3f // Significantly better at poles
            else -> 1.0f
        }
    }

    /**
     * Get active frequencies based on time and band
     */
    fun getActiveFrequencies(band: String, hour: Int): List<String> {
        val frequencies = mutableListOf<String>()
        
        // CW calling frequencies
        frequencies.add("$band CW Calling: " + when (band) {
            "10m" -> "28.03 MHz"
            "15m" -> "21.03 MHz"
            "20m" -> "14.03 MHz"
            "40m" -> "7.03 MHz"
            "80m" -> "3.53 MHz"
            else -> "TBD"
        })
        
        // SSB calling frequencies
        frequencies.add("$band SSB Calling: " + when (band) {
            "10m" -> "28.30 MHz"
            "15m" -> "21.27 MHz"
            "20m" -> "14.26 MHz"
            "40m" -> "7.17 MHz"
            "80m" -> "3.88 MHz"
            else -> "TBD"
        })
        
        return frequencies
    }
}
