package com.github.timgilbert.hexmap

import org.specs2.mutable._
import org.specs2.specification._

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
  }
}

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
class HexMapSpec extends Specification {
  
  "The HexMap" should {
    "be initialiazable" in new map5 {
      hm must not be none
    }
    /*"retain its height" in new map5 {
      hm.height must_== 5
    }
    "retain its width" in new map5 {
      hm.width must_== 5
    }*/
  }
}


trait map5 extends Scope {
  val hm: HexMap = TestHexStore.create()
}

trait addr22 extends Scope {
  val a = new Address(2,2)
}

