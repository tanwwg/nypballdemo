package com.example.myapplication

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

data class Ball(
    val position: Offset,
    val velocity: Offset = Offset.Zero,
    val radius: Float,
    val color: Color
)

data class GameState(
    val frame: Int = 0,
    val gravity: Offset,
    val bounds: Size,
    val ball: Ball
)

fun newGameState(): GameState {
    return GameState(
        gravity = Offset(0f, -9.8f * 100f),
        bounds = Size(100f, 100f),
        ball = Ball(
            position = Offset(50f, 90f),
            radius = 5f,
            velocity = Offset.Zero,
            color = Color.Blue
        )
    )
}

fun updateGameState(sim: GameState): GameState {
    val deltaTime = 1.0f / 60.0f
    var newVelocity = sim.ball.velocity + sim.gravity * deltaTime
    var newPos = sim.ball.position + newVelocity * deltaTime
    if (newPos.y - sim.ball.radius < 0) {
        newPos = sim.ball.position
        newVelocity = Offset(newVelocity.x, -newVelocity.y)
    }
    return sim.copy(frame = sim.frame +1,
        ball = sim.ball.copy(position = newPos, velocity = newVelocity))
}
