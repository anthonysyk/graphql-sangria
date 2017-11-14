import sangria.renderer.SchemaRenderer
import io.circe._
import sangria.marshalling.circe._
import sangria.schema.Schema
import sangria.macros._

object GraphQLClient {

//  case class Introspection

  def main(args: Array[String]): Unit = {

    val graphQLSchema: String = SchemaRenderer.renderSchema(SchemaDefinitionMovie.GraphQLSchema)
    println(graphQLSchema)





  }

}
