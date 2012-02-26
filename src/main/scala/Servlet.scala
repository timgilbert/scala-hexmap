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
    """{ "map" : [["blue", "brown", "yellow"], 
                  ["lightgreen", "yellow", "blue"], 
                  ["grey", "grey", "darkgreen"]] }"""
  }

  /** todo: increase randomness */
  get("/map/random-path.json") {
    contentType = "application/json"
    """{ "path" : [ [1,1], [2,2], [3,2], [3,3], [3,4] ] }"""
  }
}
