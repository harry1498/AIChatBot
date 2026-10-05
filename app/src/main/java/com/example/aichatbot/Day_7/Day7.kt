package com.example.aichatbot.Day_7

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

val TAG = "RecompositionPlayground"

@Composable
fun RecompositionPlayground(innerPadding: PaddingValues) {
    var count by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.padding(innerPadding)) {
/*
        Button(onClick = { count++ }) { Text("Count: $count") }
        ChildA()
        ChildB(count)
        ChildC()
*/

//        CalculationExperiment()
        ListExperiment()
    }
}

@Composable
fun ChildA() {
    Text("A")
    println("ChildA")
}

@Composable
fun ChildB(count: Int) {
    Text("B: $count")
    println("ChildB")
}

@Composable
fun ChildC() {
    Text("C")
    println("ChildC")
}

@Composable
fun CalculationExperiment() {
    var count by remember { mutableIntStateOf(0) }
    val result = calculateExpensiveValue()

    Column {
        Button(onClick = { count++ }) { Text("Count: $count") }
        Text("$result")
    }
}

fun calculateExpensiveValue(): Long {
    Log.e(TAG, "calculateExpensiveValue: CALCULATION RUN")
    var result = 0L
    repeat(5_000_000) { result += it }
    return result
}

data class Item(
    val id: Int,
    val name: String
)

@Composable
fun ListExperiment() {
    var mItems by remember {
        mutableStateOf(List(5) { Item(it, "Item $it") })
    }

    Column {
        Button(onClick = { mItems = listOf(Item(99, "New Item")) + mItems }) {
            Text("Add Item")
        }

        LazyColumn {
            items(items = mItems, key = { it.id }) { item ->
                Log.e(TAG, "ListExperiment: Item ${item.id}")
                Text(item.name)
            }
        }
    }
}
