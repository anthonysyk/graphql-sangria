package models

import java.time.Instant
import java.util.UUID

import sangria.macros.derive.{GraphQLDescription, GraphQLField}

trait VideoContext {
  val id: String
  val title: String
  val description: Option[String]
  val genres: Seq[Genre.Value]
  val actors: Seq[Actor]
  val rating: Option[Double]
  val reviews: Seq[Review]

  @GraphQLField
  def topActors(top: Int): Seq[Actor] = actors.sortBy(_.popularity).reverse.take(top)

  @GraphQLField
  def lastReviews(last: Int): Seq[Review] = reviews.takeRight(last)
}

case class Review(id: String = UUID.randomUUID().toString,
                  author: String,
                  content: String,
                  rating: Double,
                  created: Long = Instant.now.toEpochMilli
                 )

@GraphQLDescription("List of Genres")
object Genre extends Enumeration {
  val ACTION = Value
  val COMEDY = Value
  val HORROR = Value
  val ANIMATION = Value
  val ADVENTURE = Value
  val SCI_FI = Value
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

@GraphQLDescription("This is a video you can watch in movie theaters")
case class Movie(id: String,
                 title: String,
                 description: Option[String],
                 genres: Seq[Genre.Value],
                 actors: Seq[Actor],
                 rating: Option[Double],
                 reviews: Seq[Review],
                 budget: Long,
                 gross: Seq[Long]
                ) extends VideoContext {

  @GraphQLField
  def profit(week: Int): Long = gross.take(week - 1).sum - budget

  @GraphQLField
  def alert: Boolean = gross.sum < 0

}

case class TvShow(id: String,
                  title: String,
                  description: Option[String],
                  genres: Seq[Genre.Value],
                  actors: Seq[Actor],
                  rating: Option[Double],
                  reviews: Seq[Review]
                 ) extends VideoContext

@GraphQLDescription("This is a person playing in a movie")
case class Actor(id: String,
                 fullname: String,
                 popularity: Option[Int]
                )