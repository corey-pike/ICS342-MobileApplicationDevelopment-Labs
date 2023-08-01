package com.ics342.labs

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ics342.labs.ui.theme.LabsTheme
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MainActivity : ComponentActivity() {
    private var personData: List<Person>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        personData = loadData(resources)?.let { dataFromJsonString(it) }

        setContent {
            LabsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShowContent(personData)
                }
            }
        }
    }

    @Composable
    private fun ShowContent(data: List<Person>?) {
        data?.let {
            ContentWithPermissionCheck(data)
        }
    }
}

@Composable
private fun ContentWithPermissionCheck(data: List<Person>) {
    val context = LocalContext.current
    val locationPermissionState = remember { mutableStateOf(false) }
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        locationPermissionState.value = isGranted
    }

    LaunchedEffect(Unit) {
        if (!locationPermissionState.value) {
            locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    if (locationPermissionState.value) {
        ContentWithLocation(data)
    } else {
        showToast("Location permission required to display data.", context)
    }
}

@Composable
private fun ContentWithLocation(data: List<Person>) {
    ShowLocationView(data)
}

@Composable
private fun ShowLocationView(data: List<Person>) {
    LazyColumn {
        itemsIndexed(data) { _, person ->
            Text(
                text = "${person.id} - Name: ${person.giveName} ${person.familyName}, Age: ${person.age}",
                modifier = Modifier.padding(20.dp)
            )
        }
    }
}

private fun showToast(message: String, context: Context) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

private fun loadData(resources: Resources): String? {
    return try {
        resources.openRawResource(R.raw.data).bufferedReader().use { it.readText() }
    } catch (e: Exception) {
        null
    }
}

@OptIn(ExperimentalStdlibApi::class)
private fun dataFromJsonString(json: String): List<Person>? {
    return try {
        val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val jsonAdapter: JsonAdapter<List<Person>> = moshi.adapter()
        jsonAdapter.fromJson(json)
    } catch (e: Exception) {
        null
    }
}
