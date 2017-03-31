package controllers

import models._
import javax.inject._

import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc._
import services.MongoRepository
import utils.Errors

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by E9923207 on 3/28/2017.
  */
@Singleton
class BlogController @Inject()(val mongoRepository: MongoRepository)(implicit exec: ExecutionContext) extends Controller {

  def getBlogs(): Action[AnyContent] = Action.async {
    for {
      blogs: List[Blog] <- mongoRepository.getBlogs()
    } yield
      Ok(Json.toJson(blogs))
  }

  def addBlog(): Action[JsValue] = Action.async(parse.json) {
    request => {
      Json.fromJson[Blog](request.body) match {

        case JsSuccess(blog, _) => {
          for {
            result <- mongoRepository.addBlog(blog)
          } yield {
            Ok(s"Blog Add result $result")
          }
        }
        case JsError(errors) => {
          Future.successful(BadRequest(s"Error adding data ${Errors.show(errors)}"))
        }
      }

    }
  }

  def getBlog(id: String) = Action.async {
    _ => {
      for {
        result: Option[Blog] <- mongoRepository.getBlog(id)
      } yield{
        if (result.isEmpty)
        NotFound(s"Blog with Id : $id was not found")
        else
          Ok(Json.toJson(result.get))
      }
    }
  }
}
