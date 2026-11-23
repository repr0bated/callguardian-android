package com.example.callguardian.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.callguardian.ui.AppViewModel
import com.example.callguardian.ui.components.SectionCard

private data class Sample(val title: String, val sender: String, val body: String)

private val samples = listOf(
    Sample("Package redelivery", "+1 (202) 555-0147", "USPS: Your package is on hold due to an incomplete address. Update within 12 hours or it will be returned: https://usps-redelivery-help.top/track"),
    Sample("Unpaid toll", "+1 (305) 555-0188", "E-ZPass FINAL NOTICE: You have an unpaid toll of \$4.15. Pay today to avoid a \$50 late fee: http://ezpass-pay.info/settle"),
    Sample("Bank lock + code", "+1 (415) 555-0110", "Chase Alert: Unusual sign-in detected. Your account is locked. Reply with the 6-digit code we just sent to verify it's you."),
    Sample("Wrong-number crypto", "+1 (646) 555-0133", "Hi Amy, is this still your number? It's Lisa from the yoga class. My uncle has a crypto trading tip that doubled my savings last month."),
    Sample("Job offer", "+1 (312) 555-0161", "Hello! Our HR team found your resume. Remote part-time role, \$300-\$500/day, no experience. Reply YES to learn more on WhatsApp."),
    Sample("Legit reminder", "+1 (617) 555-0102", "Reminder: your dental appointment is Tue Dec 1 at 3:30pm. Reply C to confirm or call the office to reschedule."),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabScreen(vm: AppViewModel, onEvent: (Long) -> Unit) {
    var sender by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("Lab") })
        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            item {
                Text("Run a text through the full pipeline (rules → on-device model → schema → policy → method → receipt). Test events never block real numbers or post alerts. Works in airplane mode.",
                    style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            items(samples) { s ->
                SectionCard(Modifier.clickable { vm.runTest(s.sender, s.body, onEvent) }) {
                    Text(s.title, style = MaterialTheme.typography.titleSmall)
                    Text(s.body, style = MaterialTheme.typography.bodySmall, maxLines = 2, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            item {
                SectionCard {
                    Text("Your own text", style = MaterialTheme.typography.titleSmall)
                    OutlinedTextField(sender, { sender = it }, label = { Text("Sender (optional)") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(body, { body = it }, label = { Text("Message") }, minLines = 3, modifier = Modifier.fillMaxWidth())
                    Button(onClick = { vm.runTest(sender, body, onEvent) }, enabled = body.isNotBlank()) { Text("Screen it") }
                }
            }
        }
    }
}
