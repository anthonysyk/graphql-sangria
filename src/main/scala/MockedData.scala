
object MockedData {

  // Actors
  val RobertDowneyJr = Actor(
    id = "001",
    fullname = "Robert Downey Jr.",
    popularity = Some(85)
  )

  val ScarlettJohansson = Actor(
    id = "002",
    fullname = "Scarlett Johansson",
    popularity = Some(78)
  )

  val ChrisEvans = Actor(
    id = "003",
    fullname = "Chris Evans",
    popularity = Some(78)
  )

  // Movies
  val Avengers = Movie(
    id = "001",
    title = "The Avengers",
    description = Some("When Loki returns and threatens to steal the Tesseract and use it to take over the world, Nick Fury must form a team of elite agents and superheros - featuring Iron Man, Captain America, Thor, the Hulk, Agent Natasha Romanoff and Agent Barton - to save New York."),
    genres = Seq(Genre.ACTION, Genre.ADVENTURE, Genre.SCI_FI),
    actors = Seq(RobertDowneyJr, ChrisEvans, ScarlettJohansson),
    rating = Some(89.0)
  )

  val Avengers2 = Movie(
    id = "002",
    title = "Avengers: Age of Ultron",
    description = Some("When Tony Stark and Bruce Banner try to jump-start a dormant peacekeeping program called Ultron, things go horribly wrong and it's up to Earth's mightiest heroes to stop the villainous Ultron from enacting his terrible plan."),
    genres = Seq(Genre.ACTION, Genre.ADVENTURE, Genre.SCI_FI),
    actors = Seq(RobertDowneyJr, ChrisEvans, ScarlettJohansson),
    rating = Some(86.0)
  )

  val SpiderManHomeComing = Movie(
    id = "003",
    title = "Spider-Man : HomeComing",
    description = Some("Peter Parker balances his life as an ordinary high school student in Queens with his superhero alter-ego Spider-Man, and finds himself on the trail of a new menace prowling the skies of New York City."),
    genres = Seq(Genre.ACTION, Genre.ADVENTURE, Genre.SCI_FI),
    actors = Seq(RobertDowneyJr),
    rating = Some(77)
  )

  val CaptainAmerica1 = Movie(
    id = "004",
    title = "Captain America: The Winter Soldier",
    description = Some("As Steve Rogers struggles to embrace his role in the modern world, he teams up with a fellow Avenger and S.H.I.E.L.D agent, Black Widow, to battle a new threat from history: an assassin known as the Winter Soldier."),
    genres = Seq(Genre.ACTION, Genre.ADVENTURE, Genre.SCI_FI),
    actors = Seq(ChrisEvans, ScarlettJohansson),
    rating = Some(87)
  )

  // TV Show
  //  val DareDevil = ???
  //  val JessicaJones = ???
  //  val LukeCage = ???


  val movies = Vector(Avengers, Avengers2, SpiderManHomeComing, CaptainAmerica1)

}
