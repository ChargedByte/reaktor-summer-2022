package dev.chargedbyte.reaktor_summer_2022.model

enum class Hand {
    ROCK, PAPER, SCISSORS;

    fun beats(other: Hand): Boolean {
        return when (this) {
            ROCK -> other == SCISSORS
            PAPER -> other == ROCK
            SCISSORS -> other == PAPER
        }
    }
}
