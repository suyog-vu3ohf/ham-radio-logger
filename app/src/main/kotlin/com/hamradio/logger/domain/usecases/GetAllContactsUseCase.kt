package com.hamradio.logger.domain.usecases

import com.hamradio.logger.data.db.entity.RadioContact
import com.hamradio.logger.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow

class GetAllContactsUseCase(
    private val repository: ContactRepository
) {
    operator fun invoke(): Flow<List<RadioContact>> {
        return repository.getAllContacts()
    }
}
