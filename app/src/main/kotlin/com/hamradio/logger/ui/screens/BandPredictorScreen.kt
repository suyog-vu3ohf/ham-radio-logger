package com.hamradio.logger.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hamradio.logger.domain.logic.BandPredictionLogic

@Composable
fun BandPredictorScreen(navController: NavController) {
    var solarFlux by remember { mutableStateOf(70) }
    var kIndex by remember { mutableStateOf(2) }
    var sunSpots by remember { mutableStateOf(50) }
    var latitude by remember { mutableStateOf(0.0) }
    var openBands by remember { mutableStateOf(emptyList<BandPredictionLogic.BandOpening>()) }
    
    LaunchedEffect(Unit) {
        val bands = BandPredictionLogic.predictOpenBands(
            solarFlux = solarFlux,
            kIndex = kIndex,
            sunSpots = sunSpots
        )
        openBands = bands
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Band Predictor") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Solar Conditions", style = MaterialTheme.typography.titleMedium)
                        
                        Column {
                            Text("Solar Flux: $solarFlux")
                            Slider(
                                value = solarFlux.toFloat(),
                                onValueChange = {
                                    solarFlux = it.toInt()
                                    openBands = BandPredictionLogic.predictOpenBands(
                                        solarFlux = solarFlux,
                                        kIndex = kIndex,
                                        sunSpots = sunSpots
                                    )
                                },
                                valueRange = 0f..300f,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        
                        Column {
                            Text("K-Index: $kIndex")
                            Slider(
                                value = kIndex.toFloat(),
                                onValueChange = {
                                    kIndex = it.toInt()
                                    openBands = BandPredictionLogic.predictOpenBands(
                                        solarFlux = solarFlux,
                                        kIndex = kIndex,
                                        sunSpots = sunSpots
                                    )
                                },
                                valueRange = 0f..9f,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        
                        Column {
                            Text("Sunspots: $sunSpots")
                            Slider(
                                value = sunSpots.toFloat(),
                                onValueChange = {
                                    sunSpots = it.toInt()
                                    openBands = BandPredictionLogic.predictOpenBands(
                                        solarFlux = solarFlux,
                                        kIndex = kIndex,
                                        sunSpots = sunSpots
                                    )
                                },
                                valueRange = 0f..200f,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
            
            item {
                Text("Predicted Open Bands", style = MaterialTheme.typography.titleMedium)
            }
            
            if (openBands.isEmpty()) {
                item {
                    Text("No data available", style = MaterialTheme.typography.bodySmall)
                }
            } else {
                items(openBands.size) { index ->
                    BandCard(openBands[index])
                }
            }
        }
    }
}

@Composable
fun BandCard(band: BandPredictionLogic.BandOpening) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (band.optimal) MaterialTheme.colorScheme.tertiaryContainer
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = band.band,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "${(band.confidence * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Text(
                text = band.frequency,
                style = MaterialTheme.typography.bodySmall
            )
            if (band.optimal) {
                Text(
                    text = "Optimal conditions",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
