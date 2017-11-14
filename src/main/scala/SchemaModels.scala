import sangria.macros.derive.GraphQLField

trait Video {
  val id: String
  val title: String
  val description: Option[String]
  val genres: Seq[Genre.Value]
  val actors: Seq[Actor]
  val rating: Option[Double]

  @GraphQLField
  def topActors(top: Int): Seq[Actor] = actors.sortBy(_.popularity).reverse.take(top)
}

object Genre extends Enumeration {
  val ACTION, COMEDY, HORROR, ANIMATION, ADVENTURE, SCI_FI = Value
}

sealed trait GenreEnum

object GenreEnum {
  case object ACTION extends GenreEnum
  case object COMEDY extends GenreEnum
  case object HORROR extends GenreEnum
  case object ANIMATION extends GenreEnum
  case object ADVENTURE extends GenreEnum
  case object SCI_FI extends GenreEnum
}

object Data {
  var moviesData: Vector[Movie] = Vector.empty[Movie]
}

case class Movie(id: String,
                 title: String,
                 description: Option[String],
                 genres: Seq[Genre.Value],
                 actors: Seq[Actor],
                 rating: Option[Double]
                ) extends Video

case class TvShow(id: String,
                  title: String,
                  description: Option[String],
                  genres: Seq[Genre.Value],
                  actors: Seq[Actor],
                  rating: Option[Double]
                 ) extends Video

case class Actor(id: String, fullname: String, popularity: Option[Int])