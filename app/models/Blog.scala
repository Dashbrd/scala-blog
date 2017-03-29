package models

import play.api.libs.json.{Json, OFormat}
import reactivemongo.bson.BSONObjectID
import reactivemongo.play.json._

/**
  * Created by Ankesh on 3/28/2017.
  */
case class Blog(_id: Option[String],title:String, author:String, body:String) {
}
object Blog {
  implicit val formatter: OFormat[Blog] = Json.format[Blog]
}
