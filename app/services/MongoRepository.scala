package services

import com.google.inject.Inject
import models.City
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.commands.WriteResult
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Ankesh Dave on 3/21/2017.
  */
class MongoRepository @Inject()(val reactiveMongoApi: ReactiveMongoApi)(implicit executionContext: ExecutionContext){
 def citiesFuture: Future[JSONCollection] = reactiveMongoApi.database.map(_.collection[JSONCollection]("city"))

  def createCity (city: City): Future[WriteResult] = {
    for {
      cities: JSONCollection <- citiesFuture
      returnValue: WriteResult <- cities.insert[City](city)
    } yield
      returnValue
  }
}
