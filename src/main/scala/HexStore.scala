package com.github.timgilbert.hexmap

// This class represents the data stored in a particular hex.
// In practice this will probably be a type parameter on the Hex class
abstract class HexData

abstract trait HexStore {
  def data(addr: Address): HexData
}

