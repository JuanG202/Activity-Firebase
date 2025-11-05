package com.example.actividadinvestigativa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.actividadinvestigativa.ui.theme.ActividadInvestigativaTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ActividadInvestigativaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FormScreen()
                }
            }
        }
    }
}

@Composable
fun FormScreen() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Registro (API Node)",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (name.isNotEmpty() && email.isNotEmpty()) {

                    // JSON a enviar
                    val json = """
                        {
                          "name": "$name",
                          "email": "$email"
                        }
                    """.trimIndent()

                    // Cliente HTTP
                    val client = OkHttpClient()
                    val mediaType = "application/json; charset=utf-8".toMediaType()
                    val body = json.toRequestBody(mediaType)

                    val request = Request.Builder()
                        .url("http://10.0.2.2:3000/users") // EMULADOR → API LOCAL
                        .post(body)
                        .build()

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val response = client.newCall(request).execute()
                            println("Respuesta del servidor: ${response.body?.string()}")
                        } catch (e: Exception) {
                            println("Error: ${e.message}")
                        }
                    }

                    name = ""
                    email = ""
                } else {
                    println("Por favor llena todos los campos")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FormScreenPreview() {
    ActividadInvestigativaTheme {
        FormScreen()
    }
}
