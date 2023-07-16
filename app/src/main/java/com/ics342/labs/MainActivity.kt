package com.ics342.labs

import android.content.res.Resources
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ics342.labs.ui.theme.LabsTheme
import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val jsonData = loadData(resources)
        val data = dataFromJsonString(jsonData)
        setContent {
            LabsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LazyColumn {
                        itemsIndexed(data) { _, person ->
                            Text(
                                text = "${person.id} - Name: ${person.giveName} ${person.familyName}, Age: ${person.age}",
                                modifier = Modifier.padding(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


private fun loadData(resources: Resources): String {
    return resources
        .openRawResource(R.raw.data)
        .bufferedReader()
        .use { it.readText() }
}

data class Person(
    @Json(name = "id") val id: Int,
    @Json(name = "give_name") val giveName: String,
    @Json(name = "family_name") val familyName: String,
    @Json(name = "age") val age: Int
)

@OptIn(ExperimentalStdlibApi::class)
private fun dataFromJsonString(json: String): List<Person> {
    val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    val jsonAdapter: JsonAdapter<List<Person>> = moshi.adapter()
    return jsonAdapter.fromJson(json) ?: listOf()
}
