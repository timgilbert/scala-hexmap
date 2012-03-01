package com.github.timgilbert.hexmap
import scala.util.Random
import net.liftweb.json._
import net.liftweb.json.JsonDSL._

/**
 * This class represents the data stored in a particular hex.
 * 
 * @todo I think I'll need to change this may change into a type parameter on 
 * the HexMap class eventually to avoid a lot of unpleasant casting in client 
 * code, but for now let's proceed in  Java-like fashion
 * 
 * @param a The address of this hex
 */
abstract class HexData(a: Address) {
  /**
   * This method should return the traversal weight incurred by traveling 
   * from this hex to the other hex (ie, the edge weight).  Note that this 
   * function assumes the two hexes are adjacent.
   */
  def traverseWeight(other: HexData): Int = 1
  val address = a
  /**  Return a Json object */
  def toJson(): JValue
}

/**
 * This trait defines the fundamental interface to a data store for hexmaps
 */
abstract trait HexStore {
  def data(addr: Address): HexData
}

trait MapBackedHexStore extends HexStore {
  
}
case class ColorData(a: Address, color: String) extends HexData (a) {
  def toJson(): JValue = {
    ("color" -> color)
  }
}

/** Test store implementation */
trait RandomHexMapStore extends HexStore {
  
  private lazy val store: Map[Address, ColorData] = createRandomMap()
  
  def data(addr: Address): ColorData = {
    store(addr)
  }
  
  private def createRandomMap(): Map[Address, ColorData] = {
    // Well, this isn't ideal
    val pairs = for (i <- 1 to 6; j <- 1 to 6) 
                yield  (Address(i,j) -> new ColorData(Address(i,j), randomColor()))
    pairs toMap
  }
  
  def randomColor(): String = {
    val colors = List("blue", "brown", "darkgreen", "grey", "lightgreen", "white", "yellow")
    val random = new Random()
    return colors(random.nextInt(colors.length))
  }
}

/** Test store factory method */
object RandomHexMapStore {
   def create(width: Int, height: Int): HexMap = {
     
     new HexMap(4, 4) with RandomHexMapStore
   }
}
