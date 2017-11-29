//package Helpers
//
//import helpers.GraphQLHelper._
//import org.apache.spark.sql.Encoders
//import org.apache.spark.sql.types.{ArrayType, DataType, StructField, StructType}
//
//
//object GraphQLHelper {
//
//  sealed trait Def {
//    def hasName: Boolean
//  }
//
//  case class FieldDef(name: String, sub: Option[Def]) extends Def {
//    override def hasName: Boolean = true
//  }
//
//  case class SeqDef(sub: Option[Def]) extends Def {
//    override def hasName: Boolean = sub.exists(_.hasName)
//  }
//
//  case class StructDef(fields: Seq[FieldDef]) extends Def {
//    override def hasName: Boolean = fields.exists(_.hasName)
//  }
//
//  def generateQueryFromDef(definition: Def): String = definition match {
//    case structDef: StructDef => structDef.fields.map(generateQueryFromDef).mkString(" ")
//    case FieldDef(name, Some(sub)) if sub.hasName => Seq(name, "{", generateQueryFromDef(sub), "}").mkString(" ")
//    case FieldDef(name, _) => name
//    case SeqDef(Some(sub)) => generateQueryFromDef(sub)
//    case _ => ""
//  }
//}
//
//object GraphQLHelperWithReflection {
//
//  import scala.reflect.runtime.universe._
//
//  private val mirror = scala.reflect.runtime.universe.runtimeMirror(Thread.currentThread().getContextClassLoader)
//
//  private def localTypeOf[TT: TypeTag] = {
//    val tag = implicitly[TypeTag[TT]]
//    tag.in(mirror).tpe.normalize
//  }
//
//  private def reflectionToDef[T <: Product](implicit m: TypeTag[T]): StructDef = {
//
//    def lowGen(tpe: scala.reflect.runtime.universe.Type): Option[Def] = {
//      tpe match {
//        case t@TypeRef(_, _, Seq(elementType)) if t <:< localTypeOf[Seq[_]] =>
//          Option(SeqDef(lowGen(elementType)))
//
//        case t@TypeRef(_, _, Seq(optType)) if t <:< localTypeOf[Option[_]] =>
//          lowGen(optType)
//
//        case t if t <:< localTypeOf[Product] => Option(StructDef(
//          t.members.collect({
//            case m: MethodSymbol if m.isCaseAccessor =>
//              FieldDef(m.name.toString, lowGen(m.returnType))
//          }).toSeq))
//
//        case _ => None
//      }
//    }
//
//    lowGen(localTypeOf[T]).get.asInstanceOf[StructDef]
//  }
//
//  def generateQueryFieldsWithReflection[T <: Product](implicit tt: TypeTag[T]): String = generateQueryFromDef(reflectionToDef[T])
//
//}
//
//object GraphQLHelperWithSpark {
//
//  def sparkSchemaToDef(schema: DataType): Option[Def] = {
//    schema match {
//      case StructType(fields) => Option(StructDef(fields.map({
//        case StructField(name, dt, _, _) => FieldDef(name, sparkSchemaToDef(dt))
//      })))
//      case ArrayType(dt, _) => Option(SeqDef(sparkSchemaToDef(dt)))
//
//      case _ => None
//
//    }
//  }
//
//  import scala.reflect.runtime.universe._
//
//  // Be carefull to use only serializable types (enums) by spark
//  def generateQueryFieldsWithSpark[T <: Product](implicit tt: TypeTag[T]): String = {
//    val schema = Encoders.product[T].schema
//
//    generateQueryFromDef(sparkSchemaToDef(schema).get)
//  }
//
//}
