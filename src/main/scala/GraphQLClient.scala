import sangria.renderer.SchemaRenderer

object GraphQLClient {

  def main(args: Array[String]): Unit = {

    val toto: String = SchemaRenderer.renderSchema(SchemaDefinitionMovie.GraphQLSchema)

    println(toto)


  }

}
