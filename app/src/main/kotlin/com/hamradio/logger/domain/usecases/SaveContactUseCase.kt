package com.hamradio.logger.domain.usecases

import com.hamradio.logger.data.db.entity.RadioContact
import com.hamradio.logger.domain.repository.ContactRepository

class SaveContactUseCase(
    private val repository: ContactRepository
) {
    suspend operator fun invoke(contact: RadioContact): Long {
        return repository.insertContact(contact)
    }
}
