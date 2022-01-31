enum Hand {
  Rock = "ROCK",
  Paper = "PAPER",
  Scissors = "SCISSORS",
}

const handBeatsHand = (self: Hand, other: Hand) => {
  switch (self) {
    case Hand.Rock:
      return other === Hand.Scissors;
    case Hand.Paper:
      return other === Hand.Rock;
    case Hand.Scissors:
      return other === Hand.Paper;
  }
};

export default Hand;

export { handBeatsHand };
