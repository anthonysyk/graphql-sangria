import sangria.ast.Document
import sangria.execution.Executor
import sangria.macros._
import sangria.marshalling.sprayJson._
import sangria.renderer.SchemaRenderer
import spray.json.{JsObject, JsValue}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._



object GraphQLClient {

//  case class Introspection

  def main(args: Array[String]): Unit = {

    val graphQLSchema: String = SchemaRenderer.renderSchema(SchemaDefinitionMovie.GraphQLSchema)
    println(graphQLSchema)

  }


}


