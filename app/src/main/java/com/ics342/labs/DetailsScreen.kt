package com.ics342.labs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ics342.labs.data.DataItem

@Composable
fun DetailsScreen(dataItem: DataItem, navController: NavController) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "ID: ${dataItem.id}", fontSize = 24.sp)
        Text(text = "Name: ${dataItem.name}", fontSize = 20.sp)
        Text(text = "Description: ${dataItem.description}", fontSize = 16.sp)

        Button(
            onClick = { navController.navigate("dataItemList") },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Back")
        }
    }
}