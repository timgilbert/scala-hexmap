package com.github.timgilbert.hexmap

import org.scalatra._
import scalate.ScalateSupport

class HexMapServlet extends ScalatraServlet with ScalateSupport {
  get("/") {
    contentType = "text/html"
    templateEngine.layout("WEB-INF/views/index.jade")
  }
  
  get("/map/new.json") {
    contentType = "application/json"
    """{ "map" : [["blue", "brown", "yellow"], ["lightgreen", "yellow", "blue"], ["grey", "grey", "darkgreen"]] }"""
  }
}
