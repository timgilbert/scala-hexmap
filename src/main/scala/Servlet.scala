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
  
  get("/map/new-random.json") {
    val height = params get "height" map (_.toInt) getOrElse 10
    val width =  params get "width"  map (_.toInt) getOrElse 10
    
    val hexmap = RandomHexMapStore.create(width, height)
    contentType = "application/json"
    hexmap.toJson()
  }
  

  /** todo: increase randomness */
  get("/map/random-path.json") {
    contentType = "application/json"
    """{ "path" : [ [1,1], [2,2], [3,2], [3,3], [3,4] ] }"""
  }
}
