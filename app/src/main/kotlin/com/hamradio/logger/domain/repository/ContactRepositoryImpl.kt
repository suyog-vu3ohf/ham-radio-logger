package com.hamradio.logger.domain.repository

import com.hamradio.logger.data.db.dao.ContactDao
import com.hamradio.logger.data.db.entity.RadioContact
import kotlinx.coroutines.flow.Flow

class ContactRepositoryImpl(
    private val contactDao: ContactDao
) : ContactRepository {

    override suspend fun insertContact(contact: RadioContact): Long {
        return contactDao.insertContact(contact)
    }

    override suspend fun updateContact(contact: RadioContact) {
        contactDao.updateContact(contact)
    }

    override suspend fun deleteContact(contact: RadioContact) {
        contactDao.deleteContact(contact)
    }

    override fun getAllContacts(): Flow<List<RadioContact>> {
        return contactDao.getAllContacts()
    }

    override suspend fun getContactById(id: Long): RadioContact? {
        return contactDao.getContactById(id)
    }

    override fun getUnsyncedContacts(): Flow<List<RadioContact>> {
        return contactDao.getUnsyncedContacts()
    }

    override fun getContactCount(): Flow<Int> {
        return contactDao.getContactCount()
    }
}
