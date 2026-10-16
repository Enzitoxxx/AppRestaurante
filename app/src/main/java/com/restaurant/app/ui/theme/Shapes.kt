package com.restaurant.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),   // tarjetas y campos
    large = RoundedCornerShape(20.dp),    // botones grandes
    extraLarge = RoundedCornerShape(28.dp)
)