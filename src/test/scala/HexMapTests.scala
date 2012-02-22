package com.github.timgilbert.hexmap

import org.specs2.mutable._
import org.specs2.specification._

/** Used in address unit tests */
trait addr22 extends Scope {
  val a = new Address(2,2)
}

/** Used in address unit tests */
trait addr32 extends Scope {
  val a = new Address(3,2)
}

/** Address Unit tests */
class AddressSpec extends Specification {
  "The (2,2) address" should {
    "be equal to itself" in new addr22 {
      a must_== new Address(2,2)
    }
    "have (1,2) to the NorthWest" in new addr22 {
      a.neighbor(NorthWest) must_== new Address(1,2)
    }
    "have (2,1) to the North" in new addr22 {
      a.neighbor(North) must_== new Address(2,1)
    }
    "have (3,2) to the NorthEast" in new addr22 {
      a.neighbor(NorthEast) must_== new Address(3,2)
    }
    "have (3,3) to the SouthEast" in new addr22 {
      a.neighbor(SouthEast) must_== new Address(3,3)
    }
    "have (2,3) to the South" in new addr22 {
      a.neighbor(South) must_== new Address(2,3)
    }
    "have (1,3) to the SouthWest" in new addr22 {
      a.neighbor(SouthWest) must_== new Address(1,3)
    }
    "have orthogonal directions" in new addr22 {
      a.neighbor(North).neighbor(South) must_== a
      a.neighbor(NorthWest).neighbor(SouthEast) must_== a
      a.neighbor(NorthEast).neighbor(SouthWest) must_== a
      a.neighbor(South).neighbor(North) must_== a
      a.neighbor(SouthWest).neighbor(NorthEast) must_== a
      a.neighbor(SouthEast).neighbor(NorthWest) must_== a
    }
  }
  "The (3,2) address" should {
    "be equal to itself" in new addr32 {
      a must_== new Address(3,2)
    }
    "have (2,1) to the NorthWest" in new addr32 {
      a.neighbor(NorthWest) must_== new Address(2,1)
    }
    "have (3,1) to the North" in new addr32 {
      a.neighbor(North) must_== new Address(3,1)
    }
    "have (4,1) to the NorthEast" in new addr32 {
      a.neighbor(NorthEast) must_== new Address(4,1)
    }
    "have (4,2) to the SouthEast" in new addr32 {
      a.neighbor(SouthEast) must_== new Address(4,2)
    }
    "have (3,3) to the South" in new addr32 {
      a.neighbor(South) must_== new Address(3,3)
    }
    "have (2,2) to the SouthWest" in new addr32 {
      a.neighbor(SouthWest) must_== new Address(2,2)
    }
    "have orthogonal directions" in new addr22 {
      a.neighbor(North).neighbor(South) must_== a
      a.neighbor(NorthWest).neighbor(SouthEast) must_== a
      a.neighbor(NorthEast).neighbor(SouthWest) must_== a
      a.neighbor(South).neighbor(North) must_== a
      a.neighbor(SouthWest).neighbor(NorthEast) must_== a
      a.neighbor(SouthEast).neighbor(NorthWest) must_== a
    }
  }
}

/** Tests for the cardinal directions */
class CardinalSpec extends Specification {
  "The Cardinal class" should {
    "have 6 directions" in {
      Cardinal.all().size must_== 6
    }
    "contain all directions" in {
      val original = Cardinal.all().toSet
      original.size must_== 6
      val newSet = Set(North, South, NorthEast, SouthEast, NorthWest, SouthWest)
      original must_== newSet
    }
  }
}

/** Used in HexMap unit tests - see below */
trait map4 extends Scope {
  /** Test store implementation */
  trait TestHexStore extends HexStore {
    case class TestData(a: Address, color: String) extends HexData (a)

    def data(addr: Address): HexData = {
      new TestData(addr, "Red")
    }
  }
  /** Test store factory method */
  object TestHexStore {
     def create(): HexMap = {
       new HexMap(4, 4) with TestHexStore
     }
  }
  /** The test map */
  val hm: HexMap = TestHexStore.create()
}

/** HexMap unit tests */
class HexMapSpec extends Specification {
  "The HexMap" should {
    "be initialiazable" in new map4 {
      hm must not be none
    }
    "retain its dimensions" in new map4 {
      hm.height must_== 4
      hm.width must_== 4
    }
    "return data for a valid address" in new map4 {
      hm.hex(1,1) must not be none
    }
    "throw an error for a negative address" in new map4 {
      hm.hex(0,0) must throwA[InvalidAddressException]
      hm.hex(1,0) must throwA[InvalidAddressException]
      hm.hex(Address(0,1)) must throwA[InvalidAddressException]
    }
    "throw an error for an out-of-bounds address" in new map4 {
      hm.hex(6,6) must throwA[InvalidAddressException]
      hm.hex(1,6) must throwA[InvalidAddressException]
      hm.hex(6,1) must throwA[InvalidAddressException]
    }
    "find the proper neighbors for Address(1,1)" in new map4 {
      val neighbors = hm.getFaceNeighbors(Address(1,1))
      println (neighbors)
      neighbors.size must_== 2
      //neighbors must_== Map(SouthEast -> hm.hex(2,1), South -> hm.hex(1,2))
      
    }
  }
}



/*

Ok, so obviously ASCII isn't ideal, but...

 >-<   >-<   
< a >-< c >-<
 >-< b >-< d >
< e >-< g >-<
 >-< f >-< h >
< i >-< k >-<
 >-< j >-< l >
< m >-< o >-<
 >-< n >-< p >
    >-<   >-<

That's a 4x4 hexmap; f is at Address(2,2),
a is at Address(1,1), and g is at Address(3,2)

*/