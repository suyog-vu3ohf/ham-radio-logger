package com.hamradio.logger.domain.usecases

import com.hamradio.logger.data.db.entity.RadioContact
import com.hamradio.logger.data.network.QRZService
import com.hamradio.logger.domain.repository.ContactRepository
import java.time.format.DateTimeFormatter

class UploadToQRZUseCase(
    private val qrzService: QRZService,
    private val repository: ContactRepository
) {
    suspend operator fun invoke(
        username: String,
        password: String,
        contact: RadioContact
    ): Boolean {
        return try {
            val loginResponse = qrzService.login(username, password)
            if (loginResponse.isSuccessful && loginResponse.body()?.session != null) {
                val sessionKey = loginResponse.body()!!.session!!.key
                
                val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                val timeFormatter = DateTimeFormatter.ofPattern("HHmm")
                
                val insertResponse = qrzService.insertLogEntry(
                    sessionKey = sessionKey,
                    callSign = contact.callSign,
                    date = contact.timeOn.format(dateFormatter),
                    timeOn = contact.timeOn.format(timeFormatter),
                    timeOff = contact.timeOff?.format(timeFormatter) ?: "",
                    frequency = contact.frequency.toString(),
                    mode = contact.mode,
                    rstSent = contact.signalReport,
                    notes = contact.notes
                )
                
                if (insertResponse.isSuccessful) {
                    val updatedContact = contact.copy(uploadedToQRZ = true)
                    repository.updateContact(updatedContact)
                    true
                } else {
                    false
                }
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
