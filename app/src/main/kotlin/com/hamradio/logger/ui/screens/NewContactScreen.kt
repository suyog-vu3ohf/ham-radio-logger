package com.hamradio.logger.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hamradio.logger.data.db.entity.RadioContact
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun NewContactScreen(navController: NavController) {
    var callSign by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("") }
    var mode by remember { mutableStateOf("SSB") }
    var timeOn by remember { mutableStateOf(LocalDateTime.now()) }
    var timeOff by remember { mutableStateOf<LocalDateTime?>(null) }
    var signalReport by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf<Double?>(null) }
    var longitude by remember { mutableStateOf<Double?>(null) }
    var gridSquare by remember { mutableStateOf("") }
    var isGPSActive by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }
    
    val modeOptions = listOf("SSB", "CW", "FM", "PSK", "RTTY", "FT8", "JT65")
    var expandedMode by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Contact") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = callSign,
                onValueChange = { callSign = it.uppercase() },
                label = { Text("Call Sign") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            
            OutlinedTextField(
                value = frequency,
                onValueChange = { frequency = it },
                label = { Text("Frequency (MHz)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { expandedMode = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Mode: $mode")
                }
                DropdownMenu(
                    expanded = expandedMode,
                    onDismissRequest = { expandedMode = false }
                ) {
                    modeOptions.forEach { modeOption ->
                        DropdownMenuItem(
                            text = { Text(modeOption) },
                            onClick = {
                                mode = modeOption
                                expandedMode = false
                            }
                        )
                    }
                }
            }
            
            OutlinedTextField(
                value = timeOn.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                onValueChange = { },
                label = { Text("Time On (UTC)") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )
            
            OutlinedTextField(
                value = timeOff?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) ?: "",
                onValueChange = { },
                label = { Text("Time Off (UTC) - Optional") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )
            
            Button(
                onClick = { timeOff = LocalDateTime.now() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Set End Time Now")
            }
            
            OutlinedTextField(
                value = signalReport,
                onValueChange = { signalReport = it },
                label = { Text("Signal Report (e.g., 5/9)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("GPS Location", style = MaterialTheme.typography.titleSmall)
                        Switch(
                            checked = isGPSActive,
                            onCheckedChange = { isGPSActive = it }
                        )
                    }
                    
                    if (isGPSActive) {
                        latitude?.let {
                            Text(
                                text = "Latitude: ${"%6.4f".format(it)}°",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        longitude?.let {
                            Text(
                                text = "Longitude: ${"%6.4f".format(it)}°",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        if (latitude != null && longitude != null) {
                            Text(
                                text = "Grid: $gridSquare",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
            
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes (Optional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                maxLines = 4
            )
            
            Button(
                onClick = {
                    if (callSign.isNotEmpty() && frequency.isNotEmpty()) {
                        isSaving = true
                        val contact = RadioContact(
                            callSign = callSign,
                            frequency = frequency.toDoubleOrNull() ?: 0.0,
                            mode = mode,
                            timeOn = timeOn,
                            timeOff = timeOff,
                            signalReport = signalReport,
                            notes = notes,
                            latitude = latitude,
                            longitude = longitude
                        )
                        isSaving = false
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                } else {
                    Text("Save Contact")
                }
            }
        }
    }
}
