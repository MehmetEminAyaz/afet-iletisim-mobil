package com.example.bitirmev2.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bitirmev2.data.AddressDataProvider

@Composable
fun AdresSecimiBileseni(
    modifier: Modifier = Modifier,
    initialCity: String? = null,
    initialDistrict: String? = null,
    initialNeighborhood: String? = null,
    initialStreet: String? = null,
    initialApartment: String? = null,
    onAddressChanged: (city: String, district: String, neighborhood: String, street: String, apartment: String) -> Unit
) {
    var selectedCity by remember { mutableStateOf(initialCity ?: "") }
    var selectedDistrict by remember { mutableStateOf(initialDistrict ?: "") }
    var selectedNeighborhood by remember { mutableStateOf(initialNeighborhood ?: "") }
    var selectedStreet by remember { mutableStateOf(initialStreet ?: "") }
    var selectedApartment by remember { mutableStateOf(initialApartment ?: "") }

    // Her seçim değiştiğinde tüm adres zincirini dışarıya bildir
    LaunchedEffect(
        selectedCity,
        selectedDistrict,
        selectedNeighborhood,
        selectedStreet,
        selectedApartment
    ) {
        onAddressChanged(
            selectedCity,
            selectedDistrict,
            selectedNeighborhood,
            selectedStreet,
            selectedApartment
        )
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {

        // İl
        DropdownField(
            label = "İl",
            options = AddressDataProvider.addressData.keys.toList(),
            selectedOption = selectedCity,
            onOptionSelected = {
                selectedCity = it
                selectedDistrict = ""
                selectedNeighborhood = ""
                selectedStreet = ""
                selectedApartment = ""
            }
        )

        // İlçe
        DropdownField(
            label = "İlçe",
            options = AddressDataProvider.getDistricts(selectedCity),
            selectedOption = selectedDistrict,
            onOptionSelected = {
                selectedDistrict = it
                selectedNeighborhood = ""
                selectedStreet = ""
                selectedApartment = ""
            }
        )

        // Mahalle
        DropdownField(
            label = "Mahalle",
            options = AddressDataProvider.getNeighborhoods(selectedCity, selectedDistrict),
            selectedOption = selectedNeighborhood,
            onOptionSelected = {
                selectedNeighborhood = it
                selectedStreet = ""
                selectedApartment = ""
            }
        )

        // Cadde / Sokak
        DropdownField(
            label = "Cadde / Sokak",
            options = AddressDataProvider.getStreets(selectedCity, selectedDistrict, selectedNeighborhood),
            selectedOption = selectedStreet,
            onOptionSelected = {
                selectedStreet = it
                selectedApartment = ""
            }
        )

        // Apartman No
        DropdownField(
            label = "Apartman No",
            options = AddressDataProvider.getApartments(selectedCity, selectedDistrict, selectedNeighborhood, selectedStreet),
            selectedOption = selectedApartment,
            onOptionSelected = {
                selectedApartment = it
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
