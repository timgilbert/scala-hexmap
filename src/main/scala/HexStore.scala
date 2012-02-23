package com.github.timgilbert.hexmap

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
}

/**
 * This trait defines the fundamental interface to a data store for hexmaps
 */
abstract trait HexStore {
  def data(addr: Address): HexData
}
