import com.google.inject.Singleton
import org.scalatest._
import org.scalatest.matchers.Matcher

/**
  * Created by Ankesh Dave on 3/27/2017.
  */
@Singleton
class DemoSpec extends FlatSpec with Matchers with OptionValues with Inside with Inspectors {
  "test" should "pass" in {
    "this" equals("this")
  }
}