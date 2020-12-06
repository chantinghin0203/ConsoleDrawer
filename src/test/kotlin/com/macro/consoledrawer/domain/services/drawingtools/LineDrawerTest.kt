package com.macro.consoledrawer.domain.services.drawingtools

import com.macro.consoledrawer.domain.models.Canvas
import com.macro.consoledrawer.domain.models.Command
import com.macro.consoledrawer.exception.CanvasNotCreatedException
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

        private val expectedCanvas = Canvas(
                grids = arrayOf(
                        charArrayOf(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', ' ', ' ', 'x', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', ' ', ' ', 'x', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', ' ', ' ', 'x', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', ' ', ' ', 'x', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', ' ', ' ', 'x', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', ' ', ' ', 'x', ' ', ' ', ' ', ' ', ' ')
                )
        )

        @Test
        fun validates() {
            val matchResult = lineDrawer.validates("${Command.L} $expectedX1 $expectedY1 $expectedX2 $expectedY2", canvas)

            assertThat(matchResult.groups["x1"]!!.value.toInt()).isEqualTo(expectedX1)
            assertThat(matchResult.groups["y1"]!!.value.toInt()).isEqualTo(expectedY1)
            assertThat(matchResult.groups["x2"]!!.value.toInt()).isEqualTo(expectedX2)
            assertThat(matchResult.groups["y2"]!!.value.toInt()).isEqualTo(expectedY2)
        }

        @Test
        fun `validates with no canvas created`() {
            val canvas = Canvas()

            assertThrows(CanvasNotCreatedException::class.java) {
                lineDrawer.validates("${Command.L} $expectedX1 $expectedY1", canvas)
            }
        }

        @Test
        fun `validates with less inputs should throw WrongUserInputException`() {
            assertThrows(WrongUserInputException::class.java) {
                lineDrawer.validates("${Command.L} $expectedX1 $expectedY1", canvas)
            }
        }

        @Test
        fun `validates with extra inputs should throw WrongUserInputException`() {
            assertThrows(WrongUserInputException::class.java) {
                lineDrawer.validates("${Command.L} $expectedX1 $expectedY1 $expectedX2 $expectedY2 6", canvas)
            }
        }


        @Test
        fun `validates with out of bound height should throw WrongUserInputException`() {
            assertThrows(WrongUserInputException::class.java) {
                lineDrawer.validates("${Command.L} $expectedX1 20 $expectedY1 $expectedY2", canvas)
            }
            assertThrows(WrongUserInputException::class.java) {
                lineDrawer.validates("${Command.L} $expectedX1 $expectedY1 $expectedY1 20", canvas)
            }
        }

        @Test
        fun `validates with zero height should throw WrongUserInputException`() {
            assertThrows(NotImplementedError::class.java) {
                lineDrawer.validates("${Command.L} 0 $expectedY1 $expectedX1 $expectedY2", canvas)
            }
            assertThrows(NotImplementedError::class.java) {
                lineDrawer.validates("${Command.L} $expectedX1 $expectedY1 0 $expectedY2", canvas)
            }
        }

        @Test
        fun draws() {
            val matchResult = lineDrawer.validates("${Command.L} $expectedX1 $expectedY1 $expectedX2 $expectedY2", canvas)
            val actualCanvas = lineDrawer.draws(matchResult, canvas)

            assertThat(actualCanvas).isEqualTo(expectedCanvas)
        }

        @Test
        fun `draws with y1 is greater than y2`() {
            val matchResult = lineDrawer.validates("${Command.L} $expectedX1 $expectedY2 $expectedX2 $expectedY1", canvas)
            val actualCanvas = lineDrawer.draws(matchResult, canvas)

            assertThat(actualCanvas).isEqualTo(expectedCanvas)

        }
    }


    @Nested
    inner class HorizontalLine {
        private val expectedX1 = 5
        private val expectedY1 = 5
        private val expectedX2 = 10
        private val expectedY2 = 5

        private val expectedCanvas = Canvas(
                grids = arrayOf(
                        charArrayOf(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', ' ', ' ', 'x', 'x', 'x', 'x', 'x', 'x'),
                        charArrayOf(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ')
                )
        )

        @Test
        fun validates() {
            val matchResult = lineDrawer.validates("${Command.L} $expectedX1 $expectedY1 $expectedX2 $expectedY2", canvas)

            assertThat(matchResult.groups["x1"]!!.value.toInt()).isEqualTo(expectedX1)
            assertThat(matchResult.groups["y1"]!!.value.toInt()).isEqualTo(expectedY1)
            assertThat(matchResult.groups["x2"]!!.value.toInt()).isEqualTo(expectedX2)
            assertThat(matchResult.groups["y2"]!!.value.toInt()).isEqualTo(expectedY2)
        }

        @Test
        fun `validates with less inputs should throw WrongUserInputException`() {
            assertThrows(WrongUserInputException::class.java) {
                lineDrawer.validates("${Command.L} $expectedX1 $expectedY1", canvas)
            }
        }

        @Test
        fun `validates with extra inputs should throw WrongUserInputException`() {
            assertThrows(WrongUserInputException::class.java) {
                lineDrawer.validates("${Command.L} $expectedX1 $expectedY1 $expectedX2 $expectedY2 6", canvas)
            }
        }

        @Test
        fun `validates with out of bound width should throw WrongUserInputException`() {
            assertThrows(WrongUserInputException::class.java) {
                lineDrawer.validates("${Command.L} $expectedX1 $expectedY1 20 $expectedY2", canvas)
            }

            assertThrows(WrongUserInputException::class.java) {
                lineDrawer.validates("${Command.L} 20 $expectedY1 $expectedX2 $expectedY2", canvas)
            }
        }

        @Test
        fun `validates with zero width should throw WrongUserInputException`() {
            assertThrows(WrongUserInputException::class.java) {
                lineDrawer.validates("${Command.L} $expectedX1 $expectedY1 0 $expectedY2", canvas)
            }
            assertThrows(WrongUserInputException::class.java) {
                lineDrawer.validates("${Command.L} 0 $expectedY1 $expectedX2 $expectedY2", canvas)
            }
        }

        @Test
        fun draws() {
            val matchResult = lineDrawer.validates("${Command.L} $expectedX1 $expectedY1 $expectedX2 $expectedY2", canvas)
            val actualCanvas = lineDrawer.draws(matchResult, canvas)

            assertThat(actualCanvas).isEqualTo(expectedCanvas)
        }

        @Test
        fun `draws with x1 is greater than x2`() {
            val matchResult = lineDrawer.validates("${Command.L} $expectedX2 $expectedY1 $expectedX1 $expectedY2", canvas)
            val actualCanvas = lineDrawer.draws(matchResult, canvas)

            assertThat(actualCanvas).isEqualTo(expectedCanvas)
        }
    }

    @Test
    fun `validates with wrong inputs should throw WrongUserInputException`() {
        assertThrows(WrongUserInputException::class.java) {
            lineDrawer.validates("${Command.L} a b c d", canvas)
        }
    }

}