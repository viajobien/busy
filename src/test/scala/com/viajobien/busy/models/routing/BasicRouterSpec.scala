package com.viajobien.busy.models.routing

import com.viajobien.busy.repositories.Repository
import org.mockito.Mockito.when
import org.scalatest.mock.MockitoSugar
import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }
import play.api.mvc.Results

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{ Await, Future }

class BasicRouterSpec extends WordSpecLike with Results with Matchers with BeforeAndAfterAll with MockitoSugar {

  "BasicRouterSpec" must {

    "reload with emtpy route list" in {

      //set up
      val routeRepositoryMock = mock[Repository[Route]]
      when(routeRepositoryMock.findAll()).thenReturn(Future.successful(Nil))

      //test
      val router = new BasicRouter(routeRepositoryMock)
      val duration = Duration(1, "seconds")
      val result = Await.result(router.reload, duration)

      //asserts
      assert(result.isSuccess)

    }

  }

}