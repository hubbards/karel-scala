package karel.test

import junit.framework.Assert._
import junit.framework.TestCase

import karel.syntax._
import karel.state._
import karel.semantics.prog

/**
 * `KarelTestCase` is a test case for the Karel interpreter.
 *
 * @author Spencer Hubbard
 */
class KarelTestCase extends TestCase {
  /*
   * A robot at origin, facing north, with a given number of beepers.
   */
  def originBot(b: Int): Robot = Robot(Pos(0, 0), North, b)

  /*
   * A world were each square is clear and contains no beepers.
   */
  val w1: World = Function.const(Some(0))

  /*
   * TODO: comment
   */
  val w2: World =
    _ match {
      case Pos(0, 0) => Some(1)
      case _ => Some(0)
    }

  /*
   * A world where each square is a wall.
   */
  val w3: World = Function.const(None)

  /*
   * TODO: comment
   */
  val r1: Robot = originBot(0)

  /*
   * TODO: comment
   */
  val r2: Robot = originBot(1)

  // test move statement
  def testMove() {
    prog((Map.empty, Move), w1, r1) match {
      case Okay(_, r) =>
        assertEquals(r.p, Pos(0, 1))
        assertEquals(r.f, r1.f)
        assertEquals(r.b, r1.b)
      case _ =>
        fail("wrong result type")
    }
  }

  // test turn statement
  def testTurn() {
    prog((Map.empty, Turn(Left)), w1, r1) match {
      case Okay(_, r) =>
        assertEquals(r.p, r1.p)
        assertEquals(r.f, West)
        assertEquals(r.b, r1.b)
      case _ =>
        fail("wrong result type")
    }
  }

  // test pick-beeper statement
  def testPickBeeper() {
    prog((Map.empty, Pick), w2, r1) match {
      case Okay(w, r) =>
        assertEquals(r.p, r1.p)
        assertEquals(r.f, r1.f)
        assertEquals(r.b, 1)
        assertEquals(w(r.p), Some(0))
      case _ =>
        fail("wrong result type")
    }
  }

  // test put-beeper statement
  def testPutBeeper() {
    prog((Map.empty, Put), w1, r2) match {
      case Okay(w, r) =>
        assertEquals(r.p, r1.p)
        assertEquals(r.f, r1.f)
        assertEquals(r.b, 0)
        assertEquals(w(r.p), Some(1))
      case _ =>
        fail("wrong result type")
    }
  }

  // TODO: implement more tests
}
