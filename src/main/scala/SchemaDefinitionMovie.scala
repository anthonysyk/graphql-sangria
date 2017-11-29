import controllers.MovieController
import models._
import sangria.macros.derive.{GraphQLDeprecated, GraphQLDescription, GraphQLField}
import sangria.schema._

@GraphQLDescription("This is our query type to fetch data")
trait Query {

  @GraphQLField
  @GraphQLDescription("Fetch all movies")
  def movies: Vector[Movie] = MovieController.getAllMovies

  @GraphQLField
  @GraphQLDescription("Fetch a specific movie by id")
  def movie(id: String): Option[Movie] = MovieController.getMovieById(id)

  @GraphQLField
  @GraphQLDescription("Fetch movies by genres")
  def getMoviesByGenre(genres: Seq[Genre.Value]): Vector[Movie] = MovieController.getMoviesByGenres(genres)

  @GraphQLField
  @GraphQLDescription("Fetch a specific by title")
  @GraphQLDeprecated("Please use movie field instead when possible")
  def movieByTitle(title: String): Option[Movie] = MovieController.getMovieByTitle(title)

}

// Mutation Definition
@GraphQLDescription("This is our mutation type to modify data")
trait Mutation {

  // Attention, on ne peux pas mettre en input une case class même si on la définit correctement (dérivationInputObjectType)
  @GraphQLField
  @GraphQLDescription("Add a new review to a movie")
  def createReview(movieId: String, author: String, rating: Double, content: String): Option[Movie] = {
    val review = Review(author = author, rating = rating, content = content)
    MovieController.addReview(movieId, review)
  }

}


// Root context of our application
class ApplicationContext {

  object MutationObject extends Mutation

  object QueryObject extends Query

}


/// Schema Definition

object SchemaDefinitionMovie {

  import sangria.macros.derive._

  implicit val ReviewType: ObjectType[Unit, Review] = deriveObjectType[Unit, Review]()

  implicit val ActorType: ObjectType[Unit, Actor] = deriveObjectType[Unit, Actor]()
  implicit val TvShowType: ObjectType[Unit, TvShow] = deriveObjectType[Unit, TvShow]()
  implicit val MovieType: ObjectType[VideoContext, Movie] = deriveObjectType[VideoContext, Movie](IncludeMethods("profit", "alert", "topActors", "lastReviews"))

  // Fonctionne aussi bien avec les Enumeration Scala que les sealed trait + case object
  implicit val GenreEnum: EnumType[Genre.Value] = deriveEnumType[Genre.Value]()
//  implicit val GenreEnumSealed = deriveEnumType[GenreEnum]()

  // On récupère les champs depuis le trait GraphQLFields avec les annotations @GraphQLField
  val QueryType: ObjectType[ApplicationContext, Unit] = deriveContextObjectType[ApplicationContext, Query, Unit](_.QueryObject)

  val MutationType: ObjectType[ApplicationContext, Unit] = deriveContextObjectType[ApplicationContext, Mutation, Unit](_.MutationObject)

  // On instancie le schema qui sera exposé
  val GraphQLSchema = Schema(QueryType, Some(MutationType))

}