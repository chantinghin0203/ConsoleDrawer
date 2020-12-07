import com.macro.consoledrawer.domain.models.Canvas

interface CanvasHelper {
    companion object {
        fun createDummyCanvas(widht: Int, height: Int) = Canvas(
                grids = Array(height) {
                    CharArray(widht) {
                        ' '
                    }
                }
        )
    }
}