package com.github.timgilbert.hexmap

import org.scalatra.test.specs2._

class HelloWorldMutableServletSpec extends MutableScalatraSpec {
  addServlet(classOf[HexMapServlet], "/*") 

  "GET / on HexMapServlet" should {
    "return status 200" in {
      get("/") { 
        status must_== 200
      }
    }
  }
}
