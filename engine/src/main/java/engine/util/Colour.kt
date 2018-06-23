package engine.util

fun switch(colour: Colour): Colour {
    return if (colour == Colour.WHITE) Colour.BLACK else Colour.WHITE
}

/**
 * The colours of the players and/or pieces
 */
enum class Colour {
    WHITE,
    BLACK;
}
