package com.hamradio.logger.domain.repository

import com.hamradio.logger.data.db.entity.RadioContact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    suspend fun insertContact(contact: RadioContact): Long
    suspend fun updateContact(contact: RadioContact)
    suspend fun deleteContact(contact: RadioContact)
    fun getAllContacts(): Flow<List<RadioContact>>
    suspend fun getContactById(id: Long): RadioContact?
    fun getUnsyncedContacts(): Flow<List<RadioContact>>
    fun getContactCount(): Flow<Int>
}
