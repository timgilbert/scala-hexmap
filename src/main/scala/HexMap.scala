// Hex definition
package com.github.timgilbert.hexmap

class InvalidAddressException(msg: String) extends IllegalArgumentException

class HexMap (h: Int, w: Int) {
  // Every instance of a HexMap will be mixed in with an apropriate HexStore
  self: HexStore =>
  
  val height = h
  val width = w
  
  // Get the hex at (x,y) or return None if that's off the map
  def hex(addr: Address): Hex = {
    if (!validAddress(addr)) throw new InvalidAddressException("Illegal address " + addr + " passed to hex()")
    // Not certain this is a sensible approach, but let's start this way
    new Hex(this, addr, data(addr))
  }
  
  // Shortcut method
  def hex(x: Int, y:Int): Hex = {
    hex(Address(x, y))
  }
  
  def getFaceNeighbors(addr: Address): Map[Cardinal, Hex] = {
    // It's tougher than I thought it would be getting this into a list comprehension form
    val list = for {
      direction <- Cardinal.all()
      newAddr = addr.neighbor(direction)
      if (validAddress(newAddr))
    } yield (direction -> hex(newAddr))
    
    list toMap
  }
  
  def validAddress(a: Address): Boolean = {
    a.x >= 1 && a.y >= 1 && a.y <= height && a.x <= width
  }
}

// These are the six directions of the edges of a hex.
abstract sealed class Cardinal 
object Cardinal {
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

// One six-sided face
class Hex(context: HexMap, address: Address, data: HexData) {
  // Getting the neighbor of a hex returns the hex in that 
  // direction, or None
  def neighbor(direction: Cardinal): Option[Hex] = {
    neighbors().get(direction)
  }
  
  override def toString(): String = {
    "Hex(" + address.x + "," + address.y + ")"
  }
  
  // All of the neighbors of this hex
  def neighbors(): Map[Cardinal, Hex] = {
    context.getFaceNeighbors(address)
  }
}
