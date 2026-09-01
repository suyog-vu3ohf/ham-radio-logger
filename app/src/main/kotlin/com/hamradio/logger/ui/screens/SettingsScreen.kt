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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SettingsScreen(navController: NavController) {
    var qrzUsername by remember { mutableStateOf("") }
    var qrzPassword by remember { mutableStateOf("") }
    var myCallSign by remember { mutableStateOf("") }
    var myGridSquare by remember { mutableStateOf("") }
    var autoUpload by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
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
            Text("User Profile", style = MaterialTheme.typography.titleMedium)
            
            OutlinedTextField(
                value = myCallSign,
                onValueChange = { myCallSign = it.uppercase() },
                label = { Text("My Call Sign") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            
            OutlinedTextField(
                value = myGridSquare,
                onValueChange = { myGridSquare = it.uppercase() },
                label = { Text("My Grid Square") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text("QRZ.com Configuration", style = MaterialTheme.typography.titleMedium)
            
            OutlinedTextField(
                value = qrzUsername,
                onValueChange = { qrzUsername = it },
                label = { Text("QRZ Username") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            
            OutlinedTextField(
                value = qrzPassword,
                onValueChange = { qrzPassword = it },
                label = { Text("QRZ Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Auto Upload to QRZ")
                Switch(
                    checked = autoUpload,
                    onCheckedChange = { autoUpload = it }
                )
            }
            
            Button(
                onClick = { isSaving = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                } else {
                    Text("Save Settings")
                }
            }
        }
    }
}
