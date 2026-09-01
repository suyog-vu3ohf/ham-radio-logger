package com.hamradio.logger.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    var showQRZLogin by remember { mutableStateOf(false) }
    
    if (showQRZLogin) {
        QRZLoginDialog(
            onDismiss = { showQRZLogin = false },
            onLogin = { username, password ->
                showQRZLogin = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Ham Radio Logger",
            style = MaterialTheme.typography.headlineLarge
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = { navController.navigate("new_contact") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("New Contact")
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Button(
            onClick = { navController.navigate("logbook") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("View Logbook")
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Button(
            onClick = { navController.navigate("band_predictor") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Band Predictor")
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Button(
            onClick = { showQRZLogin = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("QRZ Upload")
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Button(
            onClick = { navController.navigate("settings") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Settings")
        }
    }
}

@Composable
fun QRZLoginDialog(
    onDismiss: () -> Unit,
    onLogin: (username: String, password: String) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("QRZ.com Login") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
                
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    enabled = !isLoading
                )
                
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (username.isNotEmpty() && password.isNotEmpty()) {
                        isLoading = true
                        onLogin(username, password)
                        isLoading = false
                        onDismiss()
                    } else {
                        errorMessage = "Please enter username and password"
                    }
                },
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                } else {
                    Text("Login")
                }
            }
        },
        dismissButton = {
            Button(onClick = onDismiss, enabled = !isLoading) {
                Text("Cancel")
            }
        }
    )
}
