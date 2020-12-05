package com.macro.consoledrawer.domain.services.drawingtools

import com.macro.consoledrawer.domain.models.Canvas
import com.macro.consoledrawer.exception.WrongUserInputException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles


@SpringBootTest
@ActiveProfiles("test")
internal class LineDrawerTest(@Autowired val lineDrawer: LineDrawer) {

    private val canvas: Canvas = Canvas(
            grids = Array(10) { CharArray(10) { ' ' } }
    )

    @Nested
    inner class VerticalLine {
        private val expectedX1 = 5
        private val expectedY1 = 5
        private val expectedX2 = 5
        private val expectedY2 = 10

        @Test
        fun `validates`() {
            val matchResult = lineDrawer.validates("L $expectedX1 $expectedY1 $expectedX2 $expectedY2", canvas)

            assertThat(matchResult.groups["x1"]!!.value.toInt()).isEqualTo(expectedX1)
            assertThat(matchResult.groups["y1"]!!.value.toInt()).isEqualTo(expectedY1)
            assertThat(matchResult.groups["x2"]!!.value.toInt()).isEqualTo(expectedX2)
            assertThat(matchResult.groups["y2"]!!.value.toInt()).isEqualTo(expectedY2)
        }

        @Test
        fun `validates with less inputs should throw WrongUserInputException`() {
            assertThrows(WrongUserInputException::class.java) {
                lineDrawer.validates("L $expectedX1 $expectedY1", canvas)
            }
        }

        @Test
        fun `validates with extra inputs should throw WrongUserInputException`() {
            assertThrows(WrongUserInputException::class.java) {
                lineDrawer.validates("L $expectedX1 $expectedY1 $expectedX2 $expectedY2 6", canvas)
            }
        }


        @Test
        fun `validates with out of bound height should throw WrongUserInputException`() {
            assertThrows(WrongUserInputException::class.java) {
                lineDrawer.validates("L $expectedX1 20 $expectedY1 $expectedY2", canvas)
            }
            assertThrows(WrongUserInputException::class.java) {
                lineDrawer.validates("L $expectedX1 $expectedY1 $expectedY1 20", canvas)
            }
        }

        @Test
        fun `validates with zero height should throw WrongUserInputException`() {
            assertThrows(NotImplementedError::class.java) {
                lineDrawer.validates("L 0 $expectedY1 $expectedX1 $expectedY2", canvas)
            }
            assertThrows(NotImplementedError::class.java) {
                lineDrawer.validates("L $expectedX1 $expectedY1 0 $expectedY2", canvas)
            }
        }

        @Test
        fun draws() {
            val matchResult = lineDrawer.validates("L $expectedX1 $expectedY1 $expectedX2 $expectedY2", canvas)

            val newCanvas = lineDrawer.draws(matchResult, canvas)

            for (i in expectedY1..expectedY2) {
                assertThat(newCanvas.getGrid(i, 5)).isEqualTo('x')
            }
        }
    }


    @Nested
    inner class HorizontalLine {
        private val expectedX1 = 5
        private val expectedY1 = 5
        private val expectedX2 = 10
        private val expectedY2 = 5

        @Test
        fun `validates`() {
            val matchResult = lineDrawer.validates("L $expectedX1 $expectedY1 $expectedX2 $expectedY2", canvas)

            assertThat(matchResult.groups["x1"]!!.value.toInt()).isEqualTo(expectedX1)
            assertThat(matchResult.groups["y1"]!!.value.toInt()).isEqualTo(expectedY1)
            assertThat(matchResult.groups["x2"]!!.value.toInt()).isEqualTo(expectedX2)
            assertThat(matchResult.groups["y2"]!!.value.toInt()).isEqualTo(expectedY2)
        }

        @Test
        fun `validates with less inputs should throw WrongUserInputException`() {
            assertThrows(WrongUserInputException::class.java) {
                lineDrawer.validates("L $expectedX1 $expectedY1", canvas)
            }
        }

        @Test
        fun `validates with extra inputs should throw WrongUserInputException`() {
            assertThrows(WrongUserInputException::class.java) {
                lineDrawer.validates("L $expectedX1 $expectedY1 $expectedX2 $expectedY2 6", canvas)
            }
        }

        @Test
        fun `validates with out of bound width should throw WrongUserInputException`() {
            assertThrows(WrongUserInputException::class.java) {
                lineDrawer.validates("L $expectedX1 $expectedY1 20 $expectedY2", canvas)
            }

            assertThrows(WrongUserInputException::class.java) {
                lineDrawer.validates("L 20 $expectedY1 $expectedX2 $expectedY2", canvas)
            }
        }

        @Test
        fun `validates with zero width should throw WrongUserInputException`() {
            assertThrows(WrongUserInputException::class.java) {
                lineDrawer.validates("L $expectedX1 $expectedY1 0 $expectedY2", canvas)
            }
            assertThrows(WrongUserInputException::class.java) {
                lineDrawer.validates("L 0 $expectedY1 $expectedX2 $expectedY2", canvas)
            }
        }

        @Test
        fun draws() {
            val matchResult = lineDrawer.validates("L $expectedX1 $expectedY1 $expectedX2 $expectedY2", canvas)

            val newCanvas = lineDrawer.draws(matchResult, canvas)

            for (i in expectedX1..expectedX2) {
                assertThat(newCanvas.getGrid(i, 5)).isEqualTo('x')
            }
        }
    }

    @Test
    fun `validates with wrong inputs should throw WrongUserInputException`() {
        assertThrows(WrongUserInputException::class.java) {
            lineDrawer.validates("L a b c d", canvas)
        }
    }

}