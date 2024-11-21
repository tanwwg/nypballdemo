package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.min


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
    Scaffold { padding ->
        // Bouncing Ball Composable
        BouncingBall(modifier = Modifier.fillMaxSize().padding(padding))
    }
}

@Composable
fun BouncingBall(modifier: Modifier = Modifier) {

    var gameState by remember {
        mutableStateOf(newGameState()) }

    LaunchedEffect("physics") {
        while (true) {
            gameState = updateGameState(gameState)
            delay(1000/60)
        }
    }

    render(gameState, modifier)
}

@Composable fun render(sim: GameState, modifier: Modifier = Modifier) {

    Canvas(modifier) {
        val scaleFactor = min(
            size.width / sim.bounds.width,
            size.height / sim.bounds.height)
        withTransform({
            translate(0f, top = size.height)
            scale(pivot = Offset.Zero, scaleX = 1.0f, scaleY = -1.0f)
            translate(0f, (size.height - sim.bounds.height * scaleFactor) / 2.0f)
            scale(pivot = Offset.Zero, scale = scaleFactor)
            rotate(sim.frame.toFloat(), pivot = Offset(50f, 50f))
            scale(pivot = Offset(50f, 50f), scale = 0.8f)
        }) {
            drawRect(color = Color.LightGray, size = sim.bounds)
            drawCircle(sim.ball.color, sim.ball.radius, sim.ball.position)
        }

    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BouncingBallApp()
}