package services

import java.util.UUID

import models.{Actor, Genre, Movie, Review}

object MockedData {

  // Actors

  object Actors {

    val RobertDowneyJr: Actor = Actor(
      id = "001",
      fullname = "Robert Downey Jr.",
      popularity = Some(85)
    )

    val ScarlettJohansson: Actor = Actor(
      id = "002",
      fullname = "Scarlett Johansson",
      popularity = Some(78)
    )

    val ChrisEvans: Actor = Actor(
      id = "003",
      fullname = "Chris Evans",
      popularity = Some(78)
    )

  }

  object Reviews {

    val review1 = Review(
      id = UUID.randomUUID().toString,
      author = "Alex",
      content = "Amazing movie ! I love superheroes",
      rating = 10
    )

    val review2 = Review(
      id = UUID.randomUUID().toString,
      author = "Hannah",
      content = "Best Marvel ever.",
      rating = 7.5
    )

    val review3 = Review(
      id = UUID.randomUUID().toString,
      author = "David",
      content = "I didn't have high expectations and wasn't disappointed",
      rating = 7
    )

    val review4 = Review(
      id = UUID.randomUUID().toString,
      author = "Alice",
      content = "I love Marvel universe. Great movie !",
      rating = 9
    )

    val review5 = Review(
      id = UUID.randomUUID().toString,
      author = "Andy",
      content = "Wow... Speechless",
      rating = 9
    )

    val review6 = Review(
      id = UUID.randomUUID().toString,
      author = "Chloe",
      content = "Last one was better",
      rating = 6.5
    )

  }



  // Movies
  object Movies {
    val Avengers: Movie = Movie(
      id = "001",
      title = "The Avengers",
      description = Some("When Loki returns and threatens to steal the Tesseract and use it to take over the world, Nick Fury must form a team of elite agents and superheros - featuring Iron Man, Captain America, Thor, the Hulk, Agent Natasha Romanoff and Agent Barton - to save New York."),
      genres = Seq(Genre.ACTION, Genre.ADVENTURE, Genre.SCI_FI),
      actors = Seq(Actors.RobertDowneyJr, Actors.ChrisEvans, Actors.ScarlettJohansson),
      rating = Some(8.1),
      reviews = Seq(Reviews.review1, Reviews.review2, Reviews.review3, Reviews.review4, Reviews.review5),
      budget = 225000000,
      gross = Seq(207438708, 103052274, 55644102, 36686871)
    )

    val Avengers2: Movie = Movie(
      id = "002",
      title = "The Avengers: Age of Ultron",
      description = Some("When Tony Stark and Bruce Banner try to jump-start a dormant peacekeeping program called Ultron, things go horribly wrong and it's up to Earth's mightiest heroes to stop the villainous Ultron from enacting his terrible plan."),
      genres = Seq(Genre.ACTION, Genre.ADVENTURE, Genre.SCI_FI),
      actors = Seq(Actors.RobertDowneyJr, Actors.ChrisEvans, Actors.ScarlettJohansson),
      rating = Some(7.4),
      reviews = Nil,
      budget = 330600000,
      gross = Seq(191271109, 77746929, 38859900, 21691697)
    )

    val SpiderManHomeComing: Movie = Movie(
      id = "003",
      title = "Spider-Man : HomeComing",
      description = Some("Peter Parker balances his life as an ordinary high school student in Queens with his superhero alter-ego Spider-Man, and finds himself on the trail of a new menace prowling the skies of New York City."),
      genres = Seq(Genre.ACTION, Genre.ADVENTURE, Genre.SCI_FI),
      actors = Seq(Actors.RobertDowneyJr),
      rating = Some(7.6),
      reviews = Nil,
      budget = 175000000,
      gross = Seq(117027503, 44205210, 22150085, 13261372, 8845978)
    )

    val CaptainAmerica1: Movie = Movie(
      id = "004",
      title = "Captain America: The Winter Soldier",
      description = Some("As Steve Rogers struggles to embrace his role in the modern world, he teams up with a fellow Avenger and S.H.I.E.L.D agent, Black Widow, to battle a new threat from history: an assassin known as the Winter Soldier."),
      genres = Seq(Genre.ACTION, Genre.ADVENTURE, Genre.SCI_FI),
      actors = Seq(Actors.ChrisEvans, Actors.ScarlettJohansson),
      rating = Some(7.8),
      reviews = Nil,
      budget = 170000000,
      gross = Seq(95023721, 41274861, 25587056, 16219025, 7774269)
    )
  }

  // TV Show
  //  val DareDevil = ???
  //  val JessicaJones = ???
  //  val LukeCage = ???

  var movies: Vector[Movie] = Vector(Movies.Avengers, Movies.Avengers2, Movies.SpiderManHomeComing, Movies.CaptainAmerica1)

}
