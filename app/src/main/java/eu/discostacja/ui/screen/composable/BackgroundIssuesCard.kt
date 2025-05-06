package eu.discostacja.ui.screen.composable


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import eu.discostacja.AppPreferences
import eu.discostacja.utils.checkBackgroundRestrictions
import eu.discostacja.utils.openNotificationSettings
import eu.discostacja.utils.requestIgnoreBatteryOptimizations
import kotlinx.coroutines.launch

@Composable
fun BackgroundIssuesCard(
    onDismiss: () -> Unit
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val problems = remember { context.checkBackgroundRestrictions() }

    if (problems.notificationsDisabled || problems.batteryOptimizationsEnabled) {
        AlertDialog(
            onDismissRequest = onDismiss,
            text = {
                Column {
                    if (problems.notificationsDisabled) {
                        Text(
                            text = "• Powiadomienia są wyłączone.",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            text = "Włącz powiadomienia systemowe aby mieć pewność, że radio nie wyłączy się po kilku minutach"
                        )
                        TextButton(
                            onClick = {
                                context.openNotificationSettings()
                            }
                        ) {
                            Text("Włącz powiadomienia")
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    if (problems.batteryOptimizationsEnabled) {
                        Text(
                            text = "• Optymalizacja baterii jest włączona.",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            text = "Zdarza się, że system wyłącza nieużywane aplikacje. Jeśli chcesz słuchać przy zablokowanym telefonie koniecznie wyłącz optymalizację baterii dla aplikacji DiscoStacja"
                        )
                        TextButton(
                            onClick = {
                                context.requestIgnoreBatteryOptimizations()
                            }
                        ) {
                            Text("Wyłącz optymalizację baterii")
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
            },
            confirmButton = {
                TextButton(onClick = {
                    coroutineScope.launch {
                        AppPreferences.doNotShowBackgroundProblemsDialogAgain.update(true)
                    }
                    onDismiss()
                }) {
                    Text(
                        text = "Nie pokazuj ponownie",
                        color = Color.Black
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(
                        text = "Zamknij",
                        color = Color.Black
                    )
                }
            }
        )
    }
}