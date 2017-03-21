package controllers

import javax.inject._

import models.City
import play.api.Logger
import play.api.libs.json._
import play.api.mvc._
import play.modules.reactivemongo._
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.api.commands.WriteResult
import reactivemongo.play.json._
import reactivemongo.play.json.collection._
import services.MongoRepository
import utils.Errors

import scala.concurrent.{ExecutionContext, Future}


/**
  * Simple controller that directly stores and retrieves [models.City] instances into a MongoDB Collection
  * Input is first converted into a city and then the city is converted to JsObject to be stored in MongoDB
  */
@Singleton
class CityController @Inject()(val mongoRepository: MongoRepository)(implicit exec: ExecutionContext) extends Controller {

  //def citiesFuture: Future[JSONCollection] = database.map(_.collection[JSONCollection]("city"))

  def create(name: String, population: Int) = Action.async {
    for {
      lastError: WriteResult <- mongoRepository.createCity(City(name,population))
    }yield
      Ok("Mongo LastError: %s".format(lastError.code))
  }

//  def createFromJson = Action.async(parse.json) { request =>
//    Json.fromJson[City](request.body) match {
//      case JsSuccess(city, _) =>
//        for {
//          cities <- citiesFuture
//          lastError: WriteResult <- cities.insert(city)
//        } yield {
//          Logger.debug(s"Successfully inserted with LastError: $lastError")
//          Created("Created 1 city")
//        }
//      case JsError(errors) =>
//        Future.successful(BadRequest("Could not build a city from the json provided. " + Errors.show(errors)))
//    }
//  }
//
//  def createBulkFromJson = Action.async(parse.json) { request =>
//    Json.fromJson[Seq[City]](request.body) match {
//      case JsSuccess(newCities, _) =>
//        citiesFuture.flatMap { cities =>
//          val documents = newCities.map(implicitly[cities.ImplicitlyDocumentProducer](_))
//
//          cities.bulkInsert(ordered = true)(documents: _*).map { multiResult =>
//            Logger.debug(s"Successfully inserted with multiResult: $multiResult")
//            Created(s"Created ${multiResult.n} cities")
//          }
//        }
//      case JsError(errors) =>
//        Future.successful(BadRequest("Could not build a city from the json provided. " + Errors.show(errors)))
//    }
//  }
//
//  def findByName(name: String) = Action.async {
//    // let's do our query
//    val futureCitiesList: Future[List[City]] = citiesFuture.flatMap {
//      // find all cities with name `name`
//      _.find(Json.obj("name" -> name)).
//      // perform the query and get a cursor of JsObject
//      cursor[City](ReadPreference.primary).
//      // Collect the results as a list
//      collect[List](Int.MaxValue,Cursor.FailOnError[List[City]]())
//    }
//
//    // everything's ok! Let's reply with a JsValue
//    futureCitiesList.map { cities =>
//      Ok(Json.toJson(cities))
//    }
//  }
}


