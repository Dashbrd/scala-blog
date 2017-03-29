package services

import com.google.inject.Inject
import models.{Blog, City}
import play.api.libs.json._
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import reactivemongo.api.commands.WriteResult
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Ankesh Dave on 3/21/2017.
  */
class MongoRepository @Inject()(val reactiveMongoApi: ReactiveMongoApi)(implicit executionContext: ExecutionContext) {

  println("Mock Repo Initialized")

  def citiesFuture: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection[JSONCollection]("city"))

  def blogsFuture: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection[JSONCollection]("blogs"))

  def createCity(city: City): Future[WriteResult] = {
    for {
      cities: JSONCollection <- citiesFuture
      returnValue: WriteResult <- cities.insert[City](city)
    } yield
      returnValue
  }

  def getBlogs(maxRecords: Int = 10): Future[List[Blog]] = {
    for {
      blogCollection: JSONCollection <- blogsFuture
      query: JsObject = Json.obj()
      blogs: List[Blog] <- blogCollection.find(query).cursor[Blog](ReadPreference.Primary)
        .collect[List](maxRecords, Cursor.FailOnError[List[Blog]]())
    }
    yield
      blogs
  }
}
