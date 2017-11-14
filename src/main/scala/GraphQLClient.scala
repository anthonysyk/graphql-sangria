import sangria.renderer.SchemaRenderer

object GraphQLClient {

//  case class Introspection

  def main(args: Array[String]): Unit = {

    val graphQLSchema: String = SchemaRenderer.renderSchema(SchemaDefinitionMovie.GraphQLSchema)
    println(graphQLSchema)

    val query =
      graphql




  }

}
