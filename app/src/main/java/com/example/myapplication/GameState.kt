package com.example.myapplication

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import kotlin.math.sqrt
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
    val ball: Ball,
    val score: Int = 0,
    val bullet: Ball? = null
)

fun newGameState(): GameState {
    return GameState(
        gravity = Offset(0f, -9.8f * 100f),
        bounds = Size(100f, 100f),
        ball = Ball(
            position = Offset(50f, 90f),
            radius = 5f,
            velocity = Offset(50f, 0f),
            color = Color.Blue
        )
    )
}

fun updateGameState(sim: GameState, tap: Boolean): GameState {
    val deltaTime = 1.0f / 60.0f
    var newVelocity = sim.ball.velocity + sim.gravity * deltaTime
    var newPos = sim.ball.position + newVelocity * deltaTime
    if (newPos.y - sim.ball.radius < 0) {
        newPos = Offset(newPos.x, sim.ball.position.y)
        newVelocity = Offset(newVelocity.x, -newVelocity.y)
    }
    if (newPos.x + sim.ball.radius > sim.bounds.width || newPos.x - sim.ball.radius < 0) {
        newPos = Offset(sim.ball.position.x, newPos.y)
        newVelocity = Offset(-newVelocity.x, newVelocity.y)
    }

    var bullet = sim.bullet
    if (tap) {
        bullet = Ball(
            position = Offset(0f, 0f),
            radius = 1f,
            velocity = Offset(100f, 100f),
            color = Color.Black
        )
    }

    var newScore = sim.score
    if (bullet != null) {
        bullet = bullet.copy(position = bullet.position + bullet.velocity * deltaTime)

        if (distance(bullet.position, newPos) < sim.ball.radius) {
            bullet = null
            newScore += 1
        }
    }

    return sim.copy(frame = sim.frame +1,
        ball = sim.ball.copy(position = newPos, velocity = newVelocity),
        bullet = bullet,
        score = newScore)
}

fun distance(a: Offset, b: Offset): Float {
    val dx = b.x - a.x
    val dy = b.y - a.y
    return sqrt(dx * dx + dy * dy)
}