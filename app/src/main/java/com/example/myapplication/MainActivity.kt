package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.random.Random


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BouncingBallApp()
        }
    }
}

//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BouncingBallApp() {
    // Set up a Scaffold to contain the content
//    Scaffold {
        // Bouncing Ball Composable
        BouncingBall(modifier = Modifier.fillMaxSize())
//    }
}

@Composable
fun BouncingBall(modifier: Modifier = Modifier) {

    var sim by remember {
        mutableStateOf(newSim())
    }

    // Animation Loop
    LaunchedEffect(Unit) {
        while (true) {
            sim = updateSimulation(sim)
            // Animation frame delay
            kotlinx.coroutines.delay(16L)
        }
    }

    render(sim = sim, modifier)
}

@Composable fun render(sim: Simulation, modifier: Modifier = Modifier) {
    var canvasSize by remember { mutableStateOf(androidx.compose.ui.geometry.Size.Zero) }
    var canvasMul by remember {
        mutableStateOf(1.0F)
    }

    fun centerOffset(): Offset {
        val diffW = canvasSize.width - sim.bounds.width * canvasMul
        val diffH = canvasSize.height - sim.bounds.height * canvasMul
        return Offset(diffW / 2.0f, diffH / 2.0f)
    }

    Canvas(
        modifier = modifier
            .onSizeChanged { size ->
                val mulX = size.width / sim.bounds.width;
                val mulY = size.height / sim.bounds.height;
                canvasMul = min(mulX, mulY);
                canvasSize = Size(size.width.toFloat(), size.height.toFloat())
            }
            .clickable {
//                gravity = -gravity
            }
    ) {
        drawRect(
            color = Color.Red,
            topLeft = centerOffset(),
            size = sim.bounds.size * canvasMul,
            style = Stroke(width = 1.0f)
        )
        sim.balls.forEach {
            drawCircle(color = it.color, radius = it.radius * canvasMul, center = centerOffset() + it.position * canvasMul)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BouncingBallApp()
}