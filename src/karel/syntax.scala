package karel

/**
 * Abstract syntax for the Karel language.
 *
 * @author Spencer Hubbard
 */
object syntax {
  // macro name
  type Macro = String

  // macro definitions
  type Defs = Map[Macro, Stmt]

  // sequence of statements
  type Stmts = List[Stmt]

  // program
  type Prog = (Defs, Stmt)

  // cardinal directions
  sealed trait Card
  case object North extends Card
  case object South extends Card
  case object East extends Card
  case object West extends Card

  // relative directions
  sealed trait Dir
  case object Front extends Dir
  case object Back extends Dir
  case object Left extends Dir
  case object Right extends Dir

  // tests
  sealed trait Test
  case object Beeper extends Test
  case object Empty extends Test
  case class Not(t: Test) extends Test
  case class Facing(c: Card) extends Test
  case class Clear(d: Dir) extends Test

  // statements
  sealed trait Stmt
  case object Shutdown extends Stmt
  case object Move extends Stmt
  case object Pick extends Stmt
  case object Put extends Stmt
  case class Turn(d: Dir) extends Stmt
  case class Call(m: Macro) extends Stmt
  case class Iterate(i: Int, s: Stmt) extends Stmt
  case class If(t: Test, s1: Stmt, s2: Stmt) extends Stmt
  case class While(t: Test, s: Stmt) extends Stmt
  case class Block(ss: Stmts) extends Stmt
}
