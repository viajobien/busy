package com.viajobien.busy.models.routing

import org.scalatest.mock.MockitoSugar
import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }

class PathUtilSpec extends WordSpecLike with Matchers with BeforeAndAfterAll with MockitoSugar {
  "PathUtil.splitPath" must {

    "split correct paths" in {

      //set up
      val path = "/x/y/z"
      val pathVariant = "/x/y/z/"

      //test
      val testObj = new TestClass

      val result = testObj.splitPath(path)
      val resultVariant = testObj.splitPath(pathVariant)

      //asserts
      assert(3 == result.size)
      assert(3 == resultVariant.size)
      assert(result.containsSlice(List("x", "y", "z")))
      assert(resultVariant.containsSlice(List("x", "y", "z")))
    }

    "split paths with query strings" in {

      //set up
      val path = "/x/y/z?x=1&y=2&z=3"

      //test
      val testObj = new TestClass

      val result = testObj.splitPath(path)

      //asserts
      assert(3 == result.size)
      assert(result.containsSlice(List("x", "y", "z")))
    }

    "split with limits" in {

      //set up
      val path = "/x/y/z/a/b/c"

      //test
      val testObj = new TestClass

      val result = testObj.splitPath(path, 3)

      //asserts
      assert(3 == result.size)
      assert(result.containsSlice(List("x", "y", "z/a/b/c")))
      assert(!result.contains("a"))
      assert(!result.contains("b"))
      assert(!result.contains("c"))
    }
  }
}

class TestClass extends PathUtil