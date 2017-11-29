import org.scalatest.FunSuite
import sangria.ast.Document
import sangria.execution.Executor
import sangria.macros._
import sangria.marshalling.sprayJson._
import sangria.renderer.SchemaRenderer
import sangria.validation.{QueryValidator, Violation}
import spray.json.{JsObject, JsValue}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class SchemaSpec extends FunSuite {

  test("query parsed correctly") {

    val query =
      graphql"""
         query {
           movie(id: "001") {
             title
           }
         }
       """

    assert(executeQuery(query).toString == """{"data":{"movie":{"title":"The Avengers"}}}""")

  }

  test("query is valid") {

    val query =
      graphql"""
         query {
           movie(id: "001") {
             title
             description
             rating
             lastReviews(last: 5) {
               author
               content
             }
           }
         }
       """

    assert(QueryValidator.default.validateQuery(SchemaDefinitionMovie.GraphQLSchema, query) == Vector.empty[Violation])

  }

  def executeQuery(query: Document, vars: JsObject = JsObject.empty): JsValue = {
    val futureResult = Executor.execute(SchemaDefinitionMovie.GraphQLSchema, query,
      variables = vars,
      userContext = new ApplicationContext)
    Await.result(futureResult, 10.seconds)
  }

}