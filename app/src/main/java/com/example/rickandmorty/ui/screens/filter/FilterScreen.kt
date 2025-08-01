package com.example.rickandmorty.ui.screens.filter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    onBackPressed: () -> Unit,
    onApplyFilters: (status: String, species: String, type: String, gender: String) -> Unit
) {
    val viewModel: FilterViewModel = hiltViewModel()
    val status by viewModel.status.collectAsState()
    val species by viewModel.species.collectAsState()
    val type by viewModel.type.collectAsState()
    val gender by viewModel.gender.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Filters") },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FilterSection(title = "Status") {
                FilterChipsRow(
                    items = listOf("Alive", "Dead", "unknown"),
                    selectedItem = status,
                    onItemClick = viewModel::onStatusChanged
                )
            }

            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

            FilterSection(title = "Gender") {
                FilterChipsRow(
                    items = listOf("Female", "Male", "Genderless", "unknown"),
                    selectedItem = gender,
                    onItemClick = viewModel::onGenderChanged
                )
            }

            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

            FilterSection(title = "Species") {
                OutlinedTextField(
                    value = species,
                    onValueChange = viewModel::onSpeciesChanged,
                    label = { Text("e.g. Human, Alien") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(32.dp),
                    trailingIcon = {
                        if (species.isNotEmpty()) {
                            IconButton(onClick = { viewModel.onSpeciesChanged("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear text")
                            }
                        }
                    }
                )
            }

            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

            FilterSection(title = "Type") {
                OutlinedTextField(
                    value = type,
                    onValueChange = viewModel::onTypeChanged,
                    label = { Text("e.g. Genetic experiment") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(32.dp),
                    trailingIcon = {
                        if (type.isNotEmpty()) {
                            IconButton(onClick = { viewModel.onTypeChanged("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear text")
                            }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onApplyFilters(status, species, type, gender) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("APPLY")
            }
        }
    }
}

@Composable
private fun FilterSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterChipsRow(
    items: List<String>,
    selectedItem: String,
    onItemClick: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            FilterChip(
                selected = selectedItem == item,
                onClick = { onItemClick(item) },
                label = { Text(item) },
                shape = RoundedCornerShape(32.dp)
            )
        }
    }
}