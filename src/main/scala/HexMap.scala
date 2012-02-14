// Hex definition

class HexMap (height: Int, width: Int) {
  // Get the hex at (x,y) or return None if that's off the map
  def hex(addr: Address): Option[Hex] = { None }
  
  def getFaceNeighbors(addr: Address): Map[Cardinal, Hex] = {
    /*val list = for (direction <- Cardinal.all(); n <- addr.neighbor(direction)) 
      yield n
    
    list.toMap*/
    Map()
  }
  
  def getVertexNeighbors(addr: Address, vertex: VertexCardinal): Map[Cardinal, Hex] = { 
    Map() 
  }
}

object HexMap {
  def create(height: Int, width: Int): HexMap = { 
    assert(height > 0)
    assert(width > 0)
    new HexMap(height, width) 
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

// An address in the coordinate system
// Addresses start at (1,1) in the upper left of the map
// Address(1,2) is offset down 1/2 square on the grid
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
    val y1 = direction match {
      case NorthWest => y 
      case North =>     y - 1
      case NorthEast => y
      case SouthEast => y + 1
      case South =>     y + 1
      case SouthWest => y + 1
    }
    new Address(x1, y1)
  }
}

// Not positive if Enumerations are the proper representation for these but let's give it a go
// Note that we are assuming hex grids in which the top is flat (versus the sides being flat)
object EdgeCardinal extends Enumeration {
  val North     = Value("n")
  val NorthEast = Value("ne")
  val SouthEast = Value("se")
  val South     = Value("s")
  val SouthWest = Value("sw")
  val NorthWest = Value("nw")
}


// These are the six directions of the vertices (points) of a hex
sealed case class VertexCardinal extends Enumeration {
  val NorthEast = Value("ne")
  val East = Value("e")
  val SouthEast = Value("se")
  val SouthWest = Value("sw")
  val West = Value("w")
  val NorthWest = Value("nw")
}

// This class represents the data stored in a particular hex.
// In practice this will probably be a type parameter on the Hex class
abstract class HexData

// One six-sided face
class Hex(context: HexMap, address: Address, data: HexData) {
  // Getting the neighbor of a hex returns the hex in that 
  // direction, or None
  def neighbor(direction: Cardinal): Option[Hex] = {
    neighbors().get(direction)
  }
  
  // All of the neighbors of this hex
  def neighbors(): Map[Cardinal, Hex] = {
    context.getFaceNeighbors(address)
  }
  
  // At the vertex there are three possible neighbors, including this one
  def vertex(direction: VertexCardinal): Map[Cardinal, Hex] = {
    context.getVertexNeighbors(address, direction)
  }
}

class TestHexData (val color: String) extends HexData
