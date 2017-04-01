package services

import com.google.inject.Inject
import models.{Blog, City}
import play.api.libs.json._
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.commands.WriteResult
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.play.json._
import reactivemongo.play.json.collection.JSONBatchCommands.FindAndModifyCommand
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Ankesh Dave on 3/21/2017.
  */
class MongoRepository @Inject()(val reactiveMongoApi: ReactiveMongoApi)(implicit executionContext: ExecutionContext) {
  def deleteBlog(id: String): Future[Boolean] = {
    for {
      blogCollection <- blogsFuture
      query = BSONDocument("_id" -> BSONObjectID.parse(id).get)
      result: WriteResult <- blogCollection.remove(query)
    } yield {
       result.ok
    }
  }


  def updateBlog(id: String, blog: Blog): Future[Option[Blog]] = {
    for {
      blogCollection: JSONCollection <- blogsFuture
      query: BSONDocument = BSONDocument("_id" -> BSONObjectID.parse(id).get)
      returnValue: FindAndModifyCommand.FindAndModifyResult <- blogCollection.findAndUpdate(query,blog , true)
      result: Option[Blog] = returnValue.result[Blog]
    } yield {
      result
    }
  }

  def getBlog(id: String): Future[Option[Blog]] = {
    for{
      blogCollection: JSONCollection <- blogsFuture
      query  = BSONDocument("_id" -> BSONObjectID.parse(id).get)
      blogOption: Option[Blog] <- blogCollection.find(query).one[Blog]
    } yield
      blogOption
  }


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

  def addBlog(blog : Blog): Future[Boolean] = {
    for {
      blogCollection <- blogsFuture
      lastError <- blogCollection.insert[Blog](blog)
    } yield
      {lastError.ok}
  }
}
