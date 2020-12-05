package com.macro.consoledrawer.domain.services.impl

import com.macro.consoledrawer.domain.models.Command
import com.macro.consoledrawer.domain.models.drawingtools.BucketFiller
import com.macro.consoledrawer.domain.models.drawingtools.CanvasDrawer
import com.macro.consoledrawer.domain.models.drawingtools.LineDrawer
import com.macro.consoledrawer.domain.models.drawingtools.RectangleDrawer
import com.macro.consoledrawer.domain.services.DrawingFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
internal class ConsoleDrawingFactoryImplTest(@Autowired val drawingFactory: DrawingFactory) {

    @Test
    fun `test picking LineDrawer`() {
        val picks = drawingFactory.picks(Command.L)

        assertThat(picks).isInstanceOf(LineDrawer::class.java)
    }

    @Test
    fun `test picking CanvasDrawer`() {
        val picks = drawingFactory.picks(Command.C)

        assertThat(picks).isInstanceOf(CanvasDrawer::class.java)
    }

    @Test
    fun `test picking BucketFiller`() {
        val picks = drawingFactory.picks(Command.B)

        assertThat(picks).isInstanceOf(BucketFiller::class.java)
    }

    @Test
    fun `test picking RectangleDrawer`() {
        val picks = drawingFactory.picks(Command.R)

        assertThat(picks).isInstanceOf(RectangleDrawer::class.java)
    }
}