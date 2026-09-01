package com.hamradio.logger.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "radio_contacts")
data class RadioContact(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val callSign: String,
    val frequency: Double, // MHz
    val mode: String, // SSB, CW, FM, etc.
    val timeOn: LocalDateTime,
    val timeOff: LocalDateTime? = null,
    val signalReport: String = "", // RST or 1-5
    val notes: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val uploadedToQRZ: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
