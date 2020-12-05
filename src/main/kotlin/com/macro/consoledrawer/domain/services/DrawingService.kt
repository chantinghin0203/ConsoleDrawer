package com.macro.consoledrawer.domain.services

import com.macro.consoledrawer.domain.models.Canvas

interface DrawingService {
    fun draws(userInput: String, canvas: Canvas): Canvas
}