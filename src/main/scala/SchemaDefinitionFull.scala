import java.util.UUID

import controllers.MovieController
import models._
import sangria.schema.{Field, _}

object SchemaDefinitionFull {

  val ActorType = ObjectType(
    "Actors",
    "Actor appearing in the Videos",
    fields = fields[Unit, Actor](
      Field(name = "id", fieldType = StringType, resolve = _.value.id),
      Field(name = "fullname", fieldType = StringType, resolve = _.value.fullname),
      Field(name = "popularity", fieldType = OptionType(IntType), resolve = _.value.popularity)
    )
  )

  val ReviewType = ObjectType(
    "Reviews",
    "Evaluation of the video",
    fields = fields[Unit, Review](
      Field(name = "id", fieldType = StringType, resolve = _.value.id),
      Field(name = "author", fieldType = StringType, resolve = _.value.author),
      Field(name = "content", fieldType = StringType, resolve = _.value.content),
      Field(name = "created", fieldType = LongType, resolve = _.value.created)
    )
  )

  val GenreEnum = EnumType(
    "Genre",
    Some("Genre of a video"),
    List(
      EnumValue("COMEDY", value = Genre.COMEDY, description = Some("Toc Toc Toc ...")),
      EnumValue("ACTION", value = Genre.ACTION, description = Some("Action videos")),
      EnumValue("HORROR", value = Genre.HORROR, description = Some("Bouuuh !")),
      EnumValue("ANIMATION", value = Genre.ANIMATION, description = Some("Perfect to have a good time")),
      EnumValue("ADVENTURE", value = Genre.ADVENTURE, description = Some("Adventure videos")),
      EnumValue("SCI_FI", value = Genre.SCI_FI, description = Some("Weird stuff happening"))
    )
  )

  // _ correspond au trait VideoContext
  // trait = interface
  val VideoType: InterfaceType[Unit, VideoContext] = InterfaceType(
    name = "VideoContext",
    description = "Template of any video",
    fields = fields[Unit, VideoContext](
      Field(name = "id", fieldType = StringType, resolve = _.value.id),
      Field(name = "title", fieldType = StringType, resolve = _.value.title),
      Field(name = "description", fieldType = OptionType(StringType), resolve = _.value.description),
      Field(name = "genres", fieldType = ListType(GenreEnum), resolve = _.value.genres),
      Field(name = "actors", fieldType = ListType(ActorType), resolve = _.value.actors),
      Field(name = "rating", fieldType = OptionType(FloatType), resolve = _.value.rating),
      Field(name = "topActors", ListType(ActorType), arguments = Argument("top", IntType) :: Nil,
        resolve = c => c.value.topActors(c.arg(Argument("top", IntType)))),
      Field(name = "lastReviews", ListType(ReviewType), arguments = Argument("last", IntType) :: Nil,
        resolve = c => c.value.lastReviews(c.arg(Argument("last", IntType))))
    )
  )

  // objectype = case class
  val MovieType = ObjectType(
    "Movie",
    "VideoContext you can watch at the theater",
    interfaces[Unit, Movie](VideoType),
    fields = fields[Unit, Movie](
      Field("id", StringType, Some("the id of the movie."), resolve = _.value.id),
      Field("title", StringType, Some("the title of the movie."), resolve = _.value.title),
      Field("actors", ListType(ActorType), Some("the actors appearing in the movie"), resolve = _.value.actors),
      Field("genres", ListType(GenreEnum), Some("the genres of the movie"), resolve = _.value.genres),
      Field("rating", OptionType(FloatType), resolve = _.value.rating),
      Field("reviews", ListType(ReviewType), resolve = _.value.reviews),
      Field("budget", LongType, resolve = _.value.budget),
      Field("gross", ListType(LongType), resolve = _.value.gross),
      Field("profit", LongType, arguments = Argument("week", IntType) :: Nil,
        resolve = ctx => ctx.value.profit(ctx.arg(Argument("week", IntType)))),
      Field("alert", BooleanType, resolve = _.value.alert)
    ))

  val TvShowType = ObjectType(
    "TvShow",
    "VideoContext you can watch on TV",
    interfaces[Unit, TvShow](VideoType),
    fields = fields[Unit, TvShow](
      Field("id", StringType, Some("the id of the tvshow."), resolve = _.value.id),
      Field("tile", StringType, Some("the title of the tvshow."), resolve = _.value.title),
      Field("actors", ListType(ActorType), Some("the actors appearing in the tvshow."), resolve = _.value.actors),
      Field("genres", ListType(GenreEnum), Some("the genres of the tvshow."), resolve = _.value.genres),
      Field("rating", fieldType = OptionType(FloatType), resolve = _.value.rating)
    )
  )

  val MovieQueryArg = Argument("id", OptionInputType(StringType), description = "id of the movie")

  val Query = ObjectType(
    name = "Query",
    description = "Required Query Schema",
    fields = fields[MovieRepo, Unit](
      Field("movies", ListType(MovieType), arguments = Nil,
        resolve = movieRepo => movieRepo.ctx.Query.movies),
      Field("movie", OptionType(MovieType), arguments = MovieQueryArg :: Nil,
        resolve = movieRepo => movieRepo.ctx.Query.movie(movieRepo.arg(MovieQueryArg))),
      Field("getMoviesByGenre", ListType(MovieType), arguments = Argument("genres", ListInputType(GenreEnum)) :: Nil,
        resolve = movieRepo => movieRepo.ctx.Query.getMoviesByGenre(movieRepo.arg(Argument("genres", ListInputType(GenreEnum)))))
    )
  )

  val MovieIdArg = Argument("movieId", StringType)
  val AuthorArg = Argument("author", StringType)
  val ContentArg = Argument("content", StringType)
  val RatingArg = Argument("rating", FloatType)

  val Mutation = ObjectType(
    name = "Mutation",
    description = "Mutation on data",
    fields = fields[MovieRepo, Unit](
      Field("createReview", OptionType(MovieType),
        arguments = MovieIdArg :: AuthorArg :: ContentArg :: RatingArg :: Nil,
        resolve = movieRepo => movieRepo.ctx.Mutation.createReview(
          movieRepo.arg(MovieIdArg),
          movieRepo.arg(AuthorArg),
          movieRepo.arg(ContentArg),
          movieRepo.arg(RatingArg)
        )
      )
    )
  )

  val GraphQLSchema: Schema[MovieRepo, Unit] = Schema(Query)

}

class MovieRepo {

  object Query {
    def movies: Vector[Movie] = MovieController.getAllMovies

    def movie(maybeId: Option[String]): Option[Movie] = movies.find(movie => maybeId.contains(movie.id))

    def getMoviesByGenre(genres: Seq[Genre.Value]): Vector[Movie] = movies.filter { movie =>
      val similarityByGenre = genres.foldLeft(0) { (acc, nextGenre) =>
        if (movie.genres.contains(nextGenre)) acc + 1 else acc
      }
      similarityByGenre == genres.size
    }
  }


  object Mutation {
    def createReview(movieId: String, author: String, content: String, rating: Double): Option[Movie] = {
      val review = Review(author = author, content = content, rating = rating)
      MovieController.addReview(movieId, review)
    }
  }

}
