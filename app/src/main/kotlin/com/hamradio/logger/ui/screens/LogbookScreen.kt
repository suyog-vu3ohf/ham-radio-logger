package com.hamradio.logger.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hamradio.logger.data.db.entity.RadioContact
import java.time.format.DateTimeFormatter

@Composable
fun LogbookScreen(navController: NavController) {
    var contacts by remember { mutableStateOf<List<RadioContact>>(emptyList()) }
    var showExportDialog by remember { mutableStateOf(false) }
    
    if (showExportDialog) {
        ExportDialog(
            onDismiss = { showExportDialog = false },
            onExportCSV = {
                showExportDialog = false
            },
            onExportADIF = {
                showExportDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Logbook") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showExportDialog = true }) {
                        Icon(Icons.Filled.Download, contentDescription = "Export")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (contacts.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("No contacts logged yet.", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(contacts) { contact ->
                    ContactCard(contact)
                }
            }
        }
    }
}

@Composable
fun ContactCard(contact: RadioContact) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = contact.callSign,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${contact.frequency} MHz - ${contact.mode}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                if (contact.uploadedToQRZ) {
                    Text(
                        text = "✓ QRZ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Time On: ${contact.timeOn.format(DateTimeFormatter.ofPattern("HH:mm UTC"))}",
                        style = MaterialTheme.typography.labelSmall
                    )
                    contact.timeOff?.let {
                        Text(
                            text = "Time Off: ${it.format(DateTimeFormatter.ofPattern("HH:mm UTC"))}",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                    Text(
                        text = "Signal: ${contact.signalReport}",
                        style = MaterialTheme.typography.labelSmall
                    )
                    contact.latitude?.let {
                        Text(
                            text = "Grid: ${formatGridSquare(it, contact.longitude ?: 0.0)}",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
            
            if (contact.notes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Notes: ${contact.notes}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

fun formatGridSquare(latitude: Double, longitude: Double): String {
    val lon = (longitude + 180) / 2
    val lat = (latitude + 90) / 10
    val gridA = "ABCDEFGHIJKLMNOPQR"
    val gridB = "ABCDEFGHIJ"
    val lon1 = gridA[lon.toInt().coerceIn(0, 17)]
    val lat1 = gridA[lat.toInt().coerceIn(0, 17)]
    val lon2 = gridB[((lon % 1) * 10).toInt().coerceIn(0, 9)]
    val lat2 = gridB[((lat % 1) * 10).toInt().coerceIn(0, 9)]
    return "$lon1$lat1$lon2$lat2"
}

@Composable
fun ExportDialog(
    onDismiss: () -> Unit,
    onExportCSV: () -> Unit,
    onExportADIF: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Export Logbook") },
        text = { Text("Choose export format") },
        confirmButton = {
            Button(onClick = onExportCSV) {
                Text("CSV")
            }
        },
        dismissButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = onExportADIF, modifier = Modifier.weight(1f)) {
                    Text("ADIF")
                }
                Button(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                    Text("Cancel")
                }
            }
        }
    )
}
