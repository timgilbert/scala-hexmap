package com.github.timgilbert.hexmap

import org.scalatra._
import scalate.ScalateSupport

class HexMapServlet extends ScalatraServlet with ScalateSupport {
  get("/") {
    contentType = "text/html"
    templateEngine.layout("WEB-INF/views/index.mustache")
  }
  
  get("/map/new.json") {
    contentType = "application/json"
    "[['blue', 'yellow', 'blue'], ['blue', 'yellow', 'blue'], ['yellow', 'yellow', 'yellow']]"
  }
}
