package com.hamradio.logger.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hamradio.logger.data.db.entity.RadioContact
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Insert
    suspend fun insertContact(contact: RadioContact): Long

    @Update
    suspend fun updateContact(contact: RadioContact)

    @Delete
    suspend fun deleteContact(contact: RadioContact)

    @Query("SELECT * FROM radio_contacts ORDER BY timeOn DESC")
    fun getAllContacts(): Flow<List<RadioContact>>

    @Query("SELECT * FROM radio_contacts WHERE id = :id")
    suspend fun getContactById(id: Long): RadioContact?

    @Query("SELECT * FROM radio_contacts WHERE uploadedToQRZ = 0")
    fun getUnsyncedContacts(): Flow<List<RadioContact>>

    @Query("SELECT COUNT(*) FROM radio_contacts")
    fun getContactCount(): Flow<Int>

    @Query("DELETE FROM radio_contacts")
    suspend fun deleteAllContacts()
}
