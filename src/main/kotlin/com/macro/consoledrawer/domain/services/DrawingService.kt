package com.macro.consoledrawer.domain.services

import com.macro.consoledrawer.domain.models.Canvas

interface DrawingService {
    fun draw(userInput: String, canvas: Canvas): Canvas
}