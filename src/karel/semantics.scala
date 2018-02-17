package karel

import syntax._
import state._

/**
 * Denotational semantics for Karel language.
 *
 * @author Spencer Hubbard
 */
object semantics {
  /**
   * Valuation function for `Test`.
   *
   * @param t the test to interpret.
   * @param w the world state.
   * @param r the robot state.
   * @return the denotation of test `t`.
   */
  def test(t: Test, w: World, r: Robot): Boolean =
    t match {
      case Beeper => r.p hasBeeper w
      case Empty => r.isEmpty
      case Not(t) => !test(t, w, r)
      case Facing(c) => r.f == c
      case Clear(d) => (r relativePos d) isClear w
    }

  /**
   * Valuation function for `Stmt`. Note that this valuation function is not
   * compositional.
   *
   * @param s the statement to interpret.
   * @param ms the macro definitions for the program.
   * @param w the world state.
   * @param r the robot state.
   * @return the denotation of statement `s`.
   */
  def stmt(s: Stmt, ms: Defs)(w: World, r: Robot): Result =
    s match {
      case Shutdown => Done(r)
      case Move =>
        val p = r relativePos Front
        if (p isClear w)
          Okay(w, Robot(p, r.f, r.b))
        else
          Error(s"Blocked at $p")
      case Pick =>
        if (r.p hasBeeper w)
          Okay(r.p decBeeper w, r.incBag)
        else
          Error(s"No beeper to pick at ${r.p}")
      case Put =>
        if (r.isEmpty)
          Error("No beeper to put")
        else
          Okay(r.p incBeeper w, r.decBag)
      case Turn(d) => Okay(w, Robot(r.p, cardTurn(d, r.f), r.b))
      case Call(m) => ms get m match {
        case None => Error(s"Undefined macro $m")
        case Some(s) => stmt(s, ms)(w, r)
      }
      case Iterate(i, s) => stmts(List.fill(i)(s), ms)(w, r)
      case If(t, s1, s2) =>
        val s = if (test(t, w, r)) s1 else s2
        stmt(s, ms)(w, r)
      case temp @ While(t, s) =>
        if (test(t, w, r))
          stmt(s, ms)(w, r) onOkay stmt(temp, ms)
        else
          Okay(w, r)
      case Block(ss) => stmts(ss, ms)(w, r)
    }

  /**
   * Valuation function for `Prog`.
   *
   * @param p the program to interpret.
   * @param w the world state.
   * @param r the robot state.
   * @return the denotation of program `p`.
   */
  def prog(p: Prog, w: World, r: Robot): Result =
    stmt(p._2, p._1)(w, r)

  // helper method
  private def stmts(ss: Stmts, ms: Defs)(w: World, r: Robot): Result =
    ss match {
      case Nil => Okay(w, r)
      case s :: ss => stmt(s, ms)(w, r) onOkay stmts(ss, ms)
    }
}
