package com.hamradio.logger.domain.logic

import android.content.Context
import com.hamradio.logger.data.db.entity.RadioContact
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExportHelper(private val context: Context) {

    fun exportToCSV(contacts: List<RadioContact>): String {
        val csv = StringBuilder()
        csv.append("Call Sign,Frequency (MHz),Mode,Time On,Time Off,Signal Report,Notes,Latitude,Longitude,Uploaded to QRZ\n")
        
        for (contact in contacts) {
            csv.append("${contact.callSign},")
            csv.append("${contact.frequency},")
            csv.append("${contact.mode},")
            csv.append("${contact.timeOn},")
            csv.append("${contact.timeOff ?: "N/A"},")
            csv.append("${contact.signalReport},")
            csv.append("\"${contact.notes.replace("\"", "\\\"")}\",")
            csv.append("${contact.latitude ?: "N/A"},")
            csv.append("${contact.longitude ?: "N/A"},")
            csv.append("${if (contact.uploadedToQRZ) "Yes" else "No"}\n")
        }
        
        return csv.toString()
    }

    fun exportToADIF(contacts: List<RadioContact>): String {
        val adif = StringBuilder()
        adif.append("Program: Ham Radio Logger\n")
        adif.append("<ADIF_VER:3>3.0.7>\n<EOH>\n")
        
        for (contact in contacts) {
            adif.append("<CALL:${contact.callSign.length}>${contact.callSign}\n")
            adif.append("<FREQ:${contact.frequency.toString().length}>${contact.frequency}\n")
            adif.append("<MODE:${contact.mode.length}>${contact.mode}\n")
            adif.append("<QSO_DATE:8>${contact.timeOn.toLocalDate().toString().replace("-", "")}\n")
            adif.append("<TIME_ON:6>${contact.timeOn.toLocalTime().toString().replace(":", "")}\n")
            contact.timeOff?.let {
                adif.append("<TIME_OFF:6>${it.toLocalTime().toString().replace(":", "")}\n")
            }
            adif.append("<RST_SENT:${contact.signalReport.length}>${contact.signalReport}\n")
            if (contact.notes.isNotEmpty()) {
                adif.append("<NOTES:${contact.notes.length}>${contact.notes}\n")
            }
            adif.append("<EOR>\n")
        }
        
        return adif.toString()
    }

    fun saveToFile(fileName: String, content: String): Boolean {
        return try {
            val file = File(context.getExternalFilesDir(null), fileName)
            file.writeText(content)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
