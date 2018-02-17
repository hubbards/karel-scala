import karel.syntax._
import karel.state._
import karel.semantics.prog

/**
 * TODO: comment
 *
 * @author Spencer Hubbard
 */
object KarelApp extends App {
  /*
   * A world that contains a room and line of beepers illustrated below:
   * 
   * xxxxxxxxxxxx
   * x          x
   * x          x
   * x          x
   * x @  2 3 4 x
   * x          x
   * xxxxxxxxxxxx
   * 
   * The symbol @ marks position (1, 1).
   * 
   * A square at positions (x,y) is clear if 0 <= x <= 9 and 0 <= y <= 4,
   * otherwise it is a wall. The squares at positions (4,1), (6,1), and (8,1)
   * contain 2, 3, and 4 beepers, respectively. All other squares contain no
   * beepers.
   */
  val demoWorld: World =
    _ match {
      case Pos(4, 1) => Some(2)
      case Pos(6, 1) => Some(3)
      case Pos(8, 1) => Some(4)
      case Pos(x, y) if 0 <= x && x <= 9 && 0 <= y && y <= 4 => Some(0)
      case _ => None
    }

  /*
   * A robot at position (1,1), facing east, with a bag containing one beeper.
   */
  val demoRobot: Robot = Robot(Pos(1, 1), East, 1)

  /**
   * Generate a Karel program that does the following a given number of times:
   * move in a straight line until a beeper is found, pick up the beeper, and
   * return to original position and facing.
   *
   * @param i the number of times to iterate.
   * @return the abstract syntax for the Karel program.
   */
  def fetcher(i: Int): Prog = {
    val fetch: Stmt = Block(List(
      While(Not(Beeper), If(Clear(Front), Move, Shutdown)),
      Pick))
    val main: Stmt = Block(List(
      Iterate(i, Block(List(
        Put,
        Move,
        Call("fetch"),
        Turn(Back),
        Move,
        Call("fetch"),
        Turn(Back)))),
      Shutdown))
    (Map("fetch" -> fetch), main)
  }

  /**
   * Generates a Karel statement that moves the robot in a rectangle with the
   * given dimensions.
   *
   * @param w the width of the rectangle.
   * @param h the height of the rectangle.
   * @return the abstract syntax for the Karel statement.
   */
  def rectangle(w: Int, h: Int): Stmt =
    Block(List(
      While(Not(Facing(North)), Turn(Right)),
      Iterate(h, Move),
      Turn(Right),
      Iterate(w, Move),
      Turn(Right),
      Iterate(h, Move),
      Turn(Right),
      Iterate(w, Move),
      Turn(Right)))

  // demos
  println(prog(fetcher(4), demoWorld, demoRobot))
  println(prog(fetcher(9), demoWorld, demoRobot))
  println(prog(fetcher(10), demoWorld, demoRobot))
  println(prog((Map.empty, rectangle(8, 3)), demoWorld, demoRobot))
  println(prog((Map.empty, rectangle(3, 8)), demoWorld, demoRobot))
}
