package karel

import syntax._

/**
 * State of a running Karel program, which consists of a world and a robot.
 *
 * The world is a grid of squares and a square is either a wall or clear. A
 * clear square contains zero or more beepers.
 *
 * The robot has a position, facing, and bag of beepers. The position of the
 * robot is the square in the grid where the robot is located. The facing of the
 * robot is the cardinal direction that the robot is facing. The bag of beepers
 * contains zero or more beepers.
 *
 * The robot may move into a square that is adjacent to its position if and only
 * if the square is clear. The robot may also pick-up and place beepers on the
 * square where it is located. The robot may only pick-up as many beepers as the
 * square contains and place as many beepers as the bag contains.
 *
 * @author Spencer Hubbard
 */
object state {
  /**
   * Finds the cardinal direction in a given relative direction to a given
   * cardinal direction.
   *
   * @param d the given relative direction.
   * @param c the given cardinal direction.
   * @return the cardinal direction relative to `d` and `c`.
   */
  def cardTurn(d: Dir, c: Card): Card =
    (d, c) match {
      case (Front, _) => c
      case (Back, North) => South
      case (Back, South) => North
      case (Back, East) => West
      case (Back, West) => East
      case (Left, North) => West
      case (Left, South) => East
      case (Left, East) => North
      case (Left, West) => South
      case (Right, North) => East
      case (Right, South) => West
      case (Right, East) => South
      case (Right, West) => North
    }

  /* 
   * World state is implemented as a function that takes a position and returns
   * `None` if the square is a wall and the number of beepers that the square
   * contains if it is clear.
   */
  type World = Pos => Option[Int]

  // position
  case class Pos(val x: Int, val y: Int) {
    /**
     * Finds the neighbor of the position in a given direction.
     *
     * @param c the given direction.
     * @return the neighbor of the position in the direction `c`.
     */
    def neighbor(c: Card): Pos =
      c match {
        case North => Pos(x, y + 1)
        case South => Pos(x, y - 1)
        case East => Pos(x + 1, y)
        case West => Pos(x - 1, y)
      }

    /**
     * Checks if the square is clear in a given world.
     *
     * @param w the given world.
     * @return `true` if and only if the square is clear in the world `w`.
     */
    def isClear(w: World): Boolean =
      w(this) != None

    /**
     * Checks if the square has a beeper in a given world.
     *
     * @param w the given world.
     * @return `true` if and only if the square has a beeper in the world `w`.
     */
    def hasBeeper(w: World): Boolean =
      w(this) map { _ > 0 } getOrElse { false }

    /**
     * Increments the number of beepers by one for a given world.
     *
     * @param w the given world.
     * @return world where the square has beepers incremented.
     */
    def incBeeper(w: World): World =
      (p: Pos) =>
        if (this == p)
          w(p) match {
            case None => Some(1)
            case Some(i) => Some(i + 1)
          }
        else
          w(p)

    /**
     * Decrements the number of beepers by one for a given world.
     *
     * @param w the given world.
     * @return world where the square has beepers decremented.
     */
    def decBeeper(w: World): World =
      (p: Pos) =>
        if (this == p)
          w(p) map { _ - 1 }
        else
          w(p)
  }

  // robot state
  case class Robot(p: Pos, f: Card, b: Int) {
    /**
     * Finds the position next to the robot in a given direction.
     *
     * @param d the given direction.
     * @return position next to the robot in the direction `d`.
     */
    def relativePos(d: Dir): Pos =
      p neighbor cardTurn(d, f)

    /**
     * Checks if the bag of beepers is empty.
     *
     * @return `true` if and only if the bag of beepers is empty.
     */
    def isEmpty: Boolean =
      b <= 0

    /**
     * Increments the number of beepers in the bag by one.
     *
     * @return robot with beepers incremented.
     */
    def incBag: Robot =
      Robot(p, f, b + 1)

    /**
     * Decrements the number of beepers in the bag by one.
     *
     * @return robot with beepers decremented.
     */
    def decBag: Robot =
      Robot(p, f, b - 1)
  }

  /*
   * The result of executing a statement is `Done`, `Okay`, or `Error`. The
   * result of executing the `Shutdown` statement is `Done`. The result of
   * executing any other statement is `Okay` if the statement does not contain a
   * error, otherwise `Error`.
   */
  sealed trait Result {
    /**
     * Applies a given function if the result is `Okay`.
     *
     * @param f the given function.
     * @return application of the function `f` to the result if it is `Okay`.
     */
    def onOkay(f: (World, Robot) => Result): Result =
      this match {
        case Okay(w, r) => f(w, r)
        case _ => this
      }

    override def toString(): String =
      this match {
        case Done(r) => s"Done: $r"
        case Okay(_, r) => s"Okay: $r"
        case Error(s) => s"Error: $s"
      }
  }

  case class Done(r: Robot) extends Result

  case class Okay(w: World, r: Robot) extends Result

  case class Error(s: String) extends Result
}
