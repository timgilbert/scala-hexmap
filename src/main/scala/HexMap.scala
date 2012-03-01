// Hex definition
package com.github.timgilbert.hexmap

import net.liftweb.json._
import net.liftweb.json.JsonDSL._

class InvalidAddressException(msg: String) extends IllegalArgumentException

/**
 * A hexmap represents an entire hexagonal grid.  The grid is addressed using (x,y) 
 * pairs, where (1,1) is the hex at the top left.
 * 
 * Every HexMap instance will be mixed in with a HexStore instance, whose purpose 
 * is to support map creation (via a create() method) and to retrieve actual data 
 * 
 * @see HexStore.scala
 */
class HexMap (h: Int, w: Int) {
  // Every instance of a HexMap will be mixed in with an apropriate HexStore
  self: HexStore =>
  
  val height = h
  val width = w
  
  /**
   * Get the Hex instance at (x,y)
   * @param addr the address to retrieve
   * @throws InvalidAddressException if x or y is < 0 or > map bounds
   */
  def hex(addr: Address): Hex = {
    if (!validAddress(addr)) throw new InvalidAddressException("Illegal address " + addr + " passed to hex()")
    // Not certain this is a sensible approach, but let's start this way
    new Hex(this, addr, data(addr))
  }
  
  /** Shortcut method */
  def hex(x: Int, y:Int): Hex = hex(Address(x, y))
  
  /**
   * Get the immediate neighbors of a hex instance 
   * @return a Map from the six cardinal directions to the neighboring hexes 
   *         on those sides, if any.
   */
  def getFaceNeighbors(addr: Address): Map[Cardinal, Hex] = {
    // It's tougher than I thought it would be getting this into a list comprehension form
    // and removing the list variable here
    val list = for {
      direction <- Cardinal.all()
      newAddr = addr.neighbor(direction)
      if (validAddress(newAddr))
    } yield (direction -> hex(newAddr))
    
    list toMap
  }
  
  /** @return true if the given address refers to a valid point on the map */
  def validAddress(a: Address): Boolean = {
    a.x >= 1 && a.y >= 1 && a.y <= height && a.x <= width
  }
  
  def allHexes(): JArray = {
    for (x <- 1 to width; y <- 1 to height) yield {
      val h: Hex = hex(x, y)
      h.toJson()
    }
  }
  
  /**
   * Produce a JSON serialization of this map
   */
  def toJson(): String = {
    //pretty(render(List(1, 1)))
    
    
    val result = ("map" -> allHexes())
    //return "{ 'map': [ [] ] }"
    pretty(render(result))
  }
}

// These are the six directions of the edges of a hex.
abstract sealed class Cardinal 
object Cardinal {
  /** @return a list of every address */
  def all(): List[Cardinal] = {
    List(NorthWest, North, NorthEast, SouthEast, South, SouthWest)
  }
}
case object North     extends Cardinal
case object NorthEast extends Cardinal
case object SouthEast extends Cardinal
case object South     extends Cardinal
case object SouthWest extends Cardinal
case object NorthWest extends Cardinal

/**
 * An address in the coordinate system.
 * 
 * Addresses start at (1,1) in the upper left of the map.
 * 
 * Address(1,2) is offset down 1/2 square on the grid;
 * Address(1,3) is another 1/2 square upwards to keep the wh
*/
case class Address(x: Int, y: Int) {
  /** 
   * Get the neighbor of this address in the given direction.  Note that this may return 
   * invalid addresses (relative to a given HexMap).
   */
  def neighbor(direction: Cardinal): Address = {
    val x1 = direction match {
      case North =>     x
      case South =>     x
      case NorthWest => x - 1
      case SouthWest => x - 1
      case NorthEast => x + 1
      case SouthEast => x + 1
    }
    val y1 = if (x % 2 == 0) {
      // Even column
      direction match {
        case North =>     y - 1
        case NorthEast => y
        case SouthEast => y + 1
        case South =>     y + 1
        case SouthWest => y + 1
        case NorthWest => y 
      }
    } else {
      // Odd column
      direction match {
        case North =>     y - 1
        case NorthEast => y - 1
        case SouthEast => y 
        case South =>     y + 1
        case SouthWest => y 
        case NorthWest => y - 1
      }
    }
    new Address(x1, y1)
  }
  def neighbors(): Map[Cardinal, Address] = {
    (for (d <- Cardinal.all()) yield (d -> neighbor(d))) toMap
  }
}

/**
 * One six-sided face
 */
class Hex(context: HexMap, address: Address, data: HexData) {
  // Getting the neighbor of a hex returns the hex in that 
  // direction, or None
  def neighbor(direction: Cardinal): Option[Hex] = {
    neighbors().get(direction)
  }
  
  override def toString(): String = {
    "Hex(" + address.x + "," + address.y + ")"
  }
  
  def toJson(): JValue = {
    ("x" -> address.x) ~ ("y" -> address.y) ~ ("data" -> data.toJson())
  }
  // All of the neighbors of this hex
  def neighbors(): Map[Cardinal, Hex] = {
    context.getFaceNeighbors(address)
  }
}
