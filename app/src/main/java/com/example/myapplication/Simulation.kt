package com.example.myapplication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

data class Ball(
    val position: Offset,
    val velocity: Offset = Offset.Zero,
    val radius: Float,
    val color: Color
)

data class Simulation(
    val gravity: Offset,
    val bounds: Rect,
    val balls: List<Ball>
)

fun updateBall(sim: Simulation, ball: Ball): Ball {
    var newVel = ball.velocity + sim.gravity
    var newPos = ball.position + newVel

    if (newPos.x - ball.radius < sim.bounds.left ||
        newPos.x + ball.radius >= sim.bounds.right) {
        newPos = ball.position
        newVel = Offset(-newVel.x, newVel.y)
    }
    if (newPos.y - ball.radius < sim.bounds.top ||
        newPos.y + ball.radius >= sim.bounds.bottom) {
        newPos = ball.position
        newVel = Offset(newVel.x, -newVel.y)
    }
    return ball.copy(velocity = newVel, position = newPos)
}

fun updateSimulation(sim: Simulation): Simulation {
    return sim.copy(balls = sim.balls.map { ball -> updateBall(sim, ball) })
}

fun randomFloat(min: Float, max: Float): Float {
    return Random.nextFloat() * (max - min) + min
}

fun newBall(): Ball {
    return Ball(
        position = Offset(randomFloat(200f, 500f), randomFloat(200f, 500f)),
        radius = 30.0f,
        velocity = Offset(randomFloat(-5.0f ,5.0f), 0.0f),
        color = randomColor())
}

fun newSim(): Simulation {
    return Simulation(
        gravity = Offset(0f, 1.0f),
        bounds = Rect(0f, 0f, 600f, 800f),
        balls = listOf(
            newBall()
        )
    )
}

fun randomColor(): Color {
    return Color(
        red = Random.nextFloat(),
        green = Random.nextFloat(),
        blue = Random.nextFloat(),
        alpha = 1f // or Random.nextFloat() for a random alpha value
    )
}