

import sangria.schema.InterfaceType
import sangria.schema._


/// Query Definition

trait GraphQLQueryFields {

  import sangria.macros.derive.GraphQLField

  @GraphQLField
  def movies: Vector[Movie]

  @GraphQLField
  def movie(id: String): Option[Movie] = movies.find(_.id == id)

  //  def actorRelatedMovies(last: Option[Int]) = {
  //    ???
  //  }

}

class GraphQLQueryRepo(moviesData: Vector[Movie]) {

  object Query extends GraphQLQueryFields {

    override def movies: Vector[Movie] = moviesData

  }

}

/// Schema Definition

object SchemaDefinitionMovie {

  import sangria.macros.derive._

  implicit val ActorType: ObjectType[Unit, Actor] = deriveObjectType[Unit, Actor]()


  implicit val TvShowType: ObjectType[Unit, TvShow] = deriveObjectType[Unit, TvShow]()

  // Fonctionne aussi bien avec les Enumeration Scala que les sealed trait + case object
  implicit val GenreEnum: EnumType[Genre.Value] = deriveEnumType[Genre.Value]()

  implicit val MovieType: ObjectType[Video, Movie] = deriveObjectType[Video, Movie](IncludeMethods("topActors"))

//    val TopActorArg = Argument("top", IntType)
//
//    implicit val MovieType = ObjectType(
//      "Movie",
//      "Video you can watch at the theater",
//      interfaces[Unit, Movie](SchemaDefinitionMovieWithoutDerivation.VideoType),
//      fields = fields[Unit, Movie](
//        Field("id", StringType, Some("the id of the movie."), resolve = _.value.id),
//        Field("title", StringType, Some("the title of the movie."), resolve = _.value.title),
//        Field("actors", ListType(ActorType), Some("the actors appearing in the movie"), resolve = _.value.actors),
//        Field("genres", ListType(GenreEnum), Some("the genres of the movie"), resolve = _.value.genres),
//        Field("topActors", ListType(ActorType),
//          arguments = TopActorArg :: Nil,
//          resolve = c => c.value.topActors(c.arg(TopActorArg)))
//      )
//    )

  // On récupère les champs depuis le trait GraphQLFields avec les annotations @GraphQLField
  implicit val QueryType: ObjectType[GraphQLQueryRepo, Unit] = deriveContextObjectType[GraphQLQueryRepo, GraphQLQueryFields, Unit](_.Query)

  // On instancie le schema qui sera exposé
  implicit val GraphQLSchema = Schema(QueryType)

}

////////

object SchemaDefinitionMovieWithoutDerivation {

  val ActorType = ObjectType(
    "Actors",
    "Actor appearing in the Videos",
    fields = fields[Unit, Actor](
      Field(name = "id", fieldType = StringType, resolve = _.value.id),
      Field(name = "fullname", fieldType = StringType, resolve = _.value.fullname),
      Field(name = "popularity", fieldType = OptionType(IntType), resolve = _.value.popularity)
    )
  )

  val GenreEnum = EnumType(
    "Genre",
    Some("Genre of a video"),
    List(
      EnumValue("COMEDY",
        value = Genre.COMEDY,
        description = Some("Toc Toc Toc ...")
      ),
      EnumValue("ACTION",
        value = Genre.ACTION,
        description = Some("Action videos")
      ),
      EnumValue("HORROR",
        value = Genre.HORROR,
        description = Some("Bouuuh !")
      ),
      EnumValue("ANIMATION",
        value = Genre.ANIMATION,
        description = Some("Perfect to have a good time")
      ),
      EnumValue("ADVENTURE",
        value = Genre.ADVENTURE,
        description = Some("Adventure videos")
      ),
      EnumValue("SCI_FI",
        value = Genre.SCI_FI,
        description = Some("Weird stuff happening")
      )
    )
  )

  // _ correspond au trait Video
  // trait = interface
  val VideoType: InterfaceType[Unit, Video] = InterfaceType(
    name = "Video",
    description = "Template of any video",
    fields = fields[Unit, Video](
      Field(name = "id", fieldType = StringType, resolve = _.value.id),
      Field(name = "title", fieldType = StringType, resolve = _.value.title),
      Field(name = "description", fieldType = StringType, resolve = _.value.description),
      Field(name = "genres", fieldType = StringType, resolve = _.value.genres),
      Field(name = "actors", fieldType = StringType, resolve = _.value.actors),
      Field(name = "rating", fieldType = StringType, resolve = _.value.rating),
      Field("topActors", ListType(ActorType), arguments = Argument("top", IntType) :: Nil, resolve = c => c.value.topActors(c.arg(Argument("top", IntType)))))
    )

  // objectype = case class
  val MovieType = ObjectType(
    "Movie",
    "Video you can watch at the theater",
    interfaces[Unit, Movie](VideoType),
    fields = fields[Unit, Movie](
      Field("id", StringType, Some("the id of the movie."), resolve = _.value.id),
      Field("title", StringType, Some("the title of the movie."), resolve = _.value.title),
      Field("actors", ListType(ActorType), Some("the actors appearing in the movie"), resolve = _.value.actors),
      Field("genres", ListType(GenreEnum), Some("the genres of the movie"), resolve = _.value.genres),
      Field(name = "rating", fieldType = StringType, resolve = _.value.rating)
    )
  )

  val TvShowType = ObjectType(
    "TvShow",
    "Video you can watch on TV",
    interfaces[Unit, TvShow](VideoType),
    fields = fields[Unit, TvShow](
      Field("id", StringType, Some("the id of the tvshow."), resolve = _.value.id),
      Field("tile", StringType, Some("the title of the tvshow."), resolve = _.value.title),
      Field("actors", ListType(ActorType), Some("the actors appearing in the tvshow."), resolve = _.value.actors),
      Field("genres", ListType(GenreEnum), Some("the genres of the tvshow."), resolve = _.value.genres),
      Field(name = "rating", fieldType = StringType, resolve = _.value.rating)
    )
  )


  val MovieQueryArg = Argument("id", OptionInputType(StringType), description = "id of the movie")

  val Query = ObjectType(
    name = "Query",
    description = "Required Query Schema",
    fields = fields[MovieRepo, Unit](
      Field("movie", OptionType(MovieType), arguments = MovieQueryArg :: Nil, resolve = (ctx) => ctx.ctx.movie(ctx.arg(MovieQueryArg)))
    )
  )

  val GraphQLSchema: Schema[MovieRepo, Unit] = Schema(Query)

}

class MovieRepo {

  def movies: Vector[Movie] = Data.moviesData

  def movie(maybeId: Option[String]): Option[Movie] = movies.find(movie => maybeId.contains(movie.id))


}
