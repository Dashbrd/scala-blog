package controllers

import models._
import javax.inject._

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller, Result}
import services.MongoRepository

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by E9923207 on 3/28/2017.
  */
@Singleton
class BlogController @Inject()(val mongoRepository: MongoRepository)(implicit exec: ExecutionContext) extends Controller{

  def getBlogs() = Action.async {
    for {
      blogs: List[Blog] <- mongoRepository.getBlogs()
    } yield
    Ok(Json.toJson(blogs))
  }
}
