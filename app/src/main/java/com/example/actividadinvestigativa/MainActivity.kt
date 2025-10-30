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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.FirebaseApp
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
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
    var grade by remember { mutableStateOf("") }
    val database = FirebaseDatabase.getInstance("https://actividadinvestigativa-de9a6-default-rtdb.firebaseio.com/")
        .getReference("students")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Registro de Estudiantes",
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
            value = grade,
            onValueChange = { grade = it },
            label = { Text("Grade") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (name.isNotEmpty() && grade.isNotEmpty()) {
                    val studentId = database.push().key // genera un ID único
                    val student = Student(name, grade)

                    if (studentId != null) {
                        database.child(studentId).setValue(student)
                            .addOnSuccessListener {
                                name = ""
                                grade = ""
                                // mensaje de éxito opcional
                                println("Datos guardados correctamente en Firebase")
                            }
                            .addOnFailureListener {
                                println("Error al guardar: ${it.message}")
                            }
                    }
                } else {
                    println("Por favor, llena todos los campos")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }

    }
}
data class Student(
    val name: String = "",
    val grade: String = ""
)

@Preview(showBackground = true)
@Composable
fun FormScreenPreview() {
    ActividadInvestigativaTheme {
        FormScreen()
    }
}
