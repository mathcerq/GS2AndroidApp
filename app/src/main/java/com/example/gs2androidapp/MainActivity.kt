package com.example.gs2androidapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gs2androidapp.ui.theme.GS2AndroidAppTheme
import kotlin.math.pow


@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GS2AndroidAppTheme {
                AppNavigation()
            }
        }
    }
}



sealed class Screen(val route: String) {
    data object Login : Screen("login_screen")
    data object Menu : Screen("menu_screen")
    data object CalculadoraIMC : Screen("imc_screen")
    data object Equipe : Screen("equipe_screen")
}



@OptIn(ExperimentalMaterial3Api::class) // ADICIONADO
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {

        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.Menu.route) { MenuScreen(navController) }
        composable(Screen.CalculadoraIMC.route) { CalculadoraIMCScreen(navController) }
        composable(Screen.Equipe.route) { EquipeScreen(navController) }
    }
}



const val USER_VALIDO = "admin"
const val SENHA_VALIDA = "123456"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "LOGIN", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.padding(bottom = 32.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuário") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            singleLine = true
        )

        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (username == USER_VALIDO && password == SENHA_VALIDA) {
                    errorMessage = null
                    navController.navigate(Screen.Menu.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                } else {
                    errorMessage = "Usuário inválido na tela."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class) // ADICIONADO
@Composable
fun MenuScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "MENU PRINCIPAL",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        Button(
            onClick = { navController.navigate(Screen.CalculadoraIMC.route) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Cálculo de IMC")
        }

        Button(
            onClick = { navController.navigate(Screen.Equipe.route) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Equipe")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Menu.route) { inclusive = true }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Voltar para Login")
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculadoraIMCScreen(navController: NavHostController) {
    var nome by remember { mutableStateOf("") }
    var pesoText by remember { mutableStateOf("") }
    var alturaText by remember { mutableStateOf("") }
    var imcResult by remember { mutableStateOf<Float?>(null) }
    var classificacao by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calculadora IMC") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.Menu.route) }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar para o Menu")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Seu nome") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                singleLine = true
            )


            OutlinedTextField(
                value = pesoText,
                onValueChange = { newValue ->

                    pesoText = newValue.filter { it.isDigit() || it == '.' }
                },
                label = { Text("Seu Peso (kg)") },

                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                singleLine = true
            )


            OutlinedTextField(
                value = alturaText,
                onValueChange = { newValue ->

                    alturaText = newValue.filter { it.isDigit() || it == '.' }
                },
                label = { Text("Sua Altura (m)") },

                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                singleLine = true
            )

            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Button(
                    onClick = {
                        val peso = pesoText.toFloatOrNull()
                        val altura = alturaText.toFloatOrNull()

                        if (nome.isBlank() || peso == null || altura == null || altura <= 0 || peso <= 0) {
                            errorMessage = "Preencha todos os campos com valores válidos."
                            imcResult = null
                            classificacao = null
                        } else {
                            errorMessage = null

                            val imc = peso / altura.pow(2)
                            imcResult = imc

                            classificacao = when {
                                imc < 18.5 -> "Abaixo do peso"
                                imc < 24.9 -> "Peso normal"
                                imc < 29.9 -> "Sobrepeso"
                                imc < 34.9 -> "Obesidade Grau I"
                                imc < 39.9 -> "Obesidade Grau II"
                                else -> "Obesidade Grau III (Mórbida)"
                            }
                        }
                    },
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text("Calcular")
                }


                OutlinedButton(
                    onClick = { navController.navigate(Screen.Menu.route) },
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    Text("Voltar")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))


            imcResult?.let { imc ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Resultado para: $nome", // Exibe o nome
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "IMC: ${"%.2f".format(imc)}",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        classificacao?.let {
                            Text(
                                text = "Classificação: $it",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipeScreen(navController: NavHostController) {

    val integrantes = listOf(
        "Matheus da Silva Cerqueira (RM: 99996)",
        "Gabriel Fernando Gimenez (RM: 92957)"

    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "EQUIPE",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )


        Card(
            modifier = Modifier.fillMaxWidth().weight(1f),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Integrantes:", style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary))
                Spacer(modifier = Modifier.height(8.dp))

                integrantes.forEach { nome ->
                    Text(text = "• $nome", modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))


        Button(
            onClick = { navController.navigate(Screen.Menu.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Voltar para o Menu")
        }
    }
}