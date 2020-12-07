package integration

import com.macro.consoledrawer.ConsoleDrawerApplication
import com.macro.consoledrawer.domain.models.Canvas
import com.macro.consoledrawer.domain.services.DrawingService
import com.macro.consoledrawer.interfaces.CommandLineTaskExecutor
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import org.springframework.test.context.ActiveProfiles
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.nio.file.Files


@SpringBootTest(classes = [ConsoleDrawerApplication::class])
@ActiveProfiles("test")
internal class CommandLineTaskExecutorTest(@Autowired val drawingService: DrawingService) {
    val commandLineRunner: CommandLineTaskExecutor = CommandLineTaskExecutor(drawingService)

    @Test
    fun `test drawing with the example provided in the question`() {
        val myOut = ByteArrayOutputStream()

        val expectedOutput = Files.readString(ClassPathResource("example-output.txt").file.toPath())


        val myIn = ByteArrayInputStream("""
            |C 20 4
            |L 1 2 6 2
            |L 6 3 6 4
            |R 14 1 18 3
            |B 10 3 o
            |Q
        """.trimMargin().toByteArray()
        )

        System.setIn(myIn)
        System.setOut(PrintStream(myOut))

        commandLineRunner.run()

        val actualOutput = myOut.toString()

        assertThat(actualOutput).isEqualToNormalizingWhitespace(expectedOutput)
    }

    @Test
    fun `test drawing CS and filled with color`() {
        val myOut = ByteArrayOutputStream()

        val expectedOutput = Files.readString(ClassPathResource("draw-cs-logo.txt").file.toPath())


        val myIn = ByteArrayInputStream("""
            |C 20 20
            |R 3 3 18 18
            |L 5 5 9 5
            |L 5 5 5 16
            |L 9 5 9 7
            |L 9 7 7 7
            |L 7 7 7 14
            |L 7 14 9 14
            |L 9 14 9 16
            |L 9 16 5 16
            |R 12 5 16 6
            |L 12 6 12 9
            |R 12 10 16 11
            |L 16 12 16 14
            |R 16 15 12 16
            |B 6 9 o
            |B 1 1 a
            |Q
        """.trimMargin().toByteArray()
        )

        System.setIn(myIn)
        System.setOut(PrintStream(myOut))

        commandLineRunner.run()

        val actualOutput = myOut.toString()

        assertThat(actualOutput).isEqualToNormalizingWhitespace(expectedOutput)
    }


    @Test
    fun `test drawing with wrong input should throw WrongUserInputException and skipped`() {
        val expectedCanvas = Canvas(
                arrayOf(
                        charArrayOf('o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'x', 'x', 'x', 'x', 'x', 'o', 'o'),
                        charArrayOf('x', 'x', 'x', 'x', 'x', 'x', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'x', ' ', ' ', ' ', 'x', 'o', 'o'),
                        charArrayOf('o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'x', 'x', 'x', 'x', 'x', 'o', 'o'),
                        charArrayOf('o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o')
                )
        )

        val myIn = ByteArrayInputStream("""
            |C 20 4
            |L 1 2 6 2
            |L 6 3 6 5
            |R 14 1 18 3
            |B 10 3 o
            |Q
        """.trimMargin().toByteArray()
        )

        System.setIn(myIn)

        commandLineRunner.run()

        assertThat(commandLineRunner.getCanvas()).isEqualTo(expectedCanvas)
    }

}