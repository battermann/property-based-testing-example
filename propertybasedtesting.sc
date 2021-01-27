// scala 2.13.4

import $ivy.`org.scalacheck::scalacheck:1.14.1`
import $ivy.`org.scalatestplus::scalacheck-1-14:3.1.0.0`

import org.scalatest.matchers.should.Matchers
import org.scalatest.propspec.AnyPropSpec
import org.scalatest._
import org.scalatest.freespec.AnyFreeSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalactic.TypeCheckedTripleEquals

def add(x: Int, y: Int): Int =
  x + y

class ExampleBasedTests extends AnyFreeSpec with Matchers with TypeCheckedTripleEquals {

  // EXAMPLE BASED TESTS

  "when add 2+2 expect 4" in {
    add(2, 2) should ===(4)
  }

  "when add 1+3 expect 4" in {
    add(1, 3) should ===(4)
  }

  "when add -1+3 expect 2" in {
    add(-1, 3) should ===(2)
  }

  "when add 2+3 expect 5" in {
    add(2, 3) should ===(5)
  }

  "when add 1+41 expect 42" in {
    add(1, 41) should ===(42)
  }

  "when add 3+5 expect 8" in {
    add(3, 5) should ===(8)
  }

  "when add rnd nums expect correct sum" in {
    (1 to 100).foreach { _ =>
      val x        = scala.util.Random.nextInt
      val y        = scala.util.Random.nextInt
      val expected = x + y // <- bad
      add(x, y) should ===(expected)
    }
  }
}

val exampleBasedTests = new ExampleBasedTests()
run(exampleBasedTests)

class PoorMansPropertyBasedTests extends AnyFreeSpec with Matchers with TypeCheckedTripleEquals {

  // (POOR MAN'S) PROPERTY BASED TESTING

  "when add rnd nums order should not matter" in {
    (1 to 100).foreach { _ =>
      val x = scala.util.Random.nextInt
      val y = scala.util.Random.nextInt
      add(x, y) should ===(add(y, x))
    }
  }

  "add 1 twice is the same as add 2" in {
    (1 to 100).foreach { _ =>
      val x = scala.util.Random.nextInt
      val y = scala.util.Random.nextInt
      val z = scala.util.Random.nextInt
      add(add(x, y), z) should ===(add(x, add(y, z)))
    }
  }

  "add 0 is the same as doing nothing" in {
    (1 to 100).foreach { _ =>
      val x = scala.util.Random.nextInt
      add(x, 0) should ===(x)
    }
  }
}

val poorMansPropertyBasedTests = new PoorMansPropertyBasedTests()
run(poorMansPropertyBasedTests)

// PROPERTY BASED TESTING USING SCALA CHECK

class PropertyBasedTests extends AnyPropSpec with ScalaCheckPropertyChecks with Matchers with TypeCheckedTripleEquals {
  property("property commutativity") {
    forAll { (a: Int, b: Int) =>
      add(a, b) should ===(add(b, a))
    }
  }

  property("property associativity") {
    forAll { (a: Int, b: Int, c: Int) =>
      add(a, add(b, c)) should ===(add(add(a, b), c))
    }
  }
}

val propertyBasedTests = new PropertyBasedTests()
run(propertyBasedTests)
