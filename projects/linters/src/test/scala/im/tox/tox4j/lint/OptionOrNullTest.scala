package im.tox.tox4j.lint

import org.scalatest.FunSuite

final class OptionOrNullTest extends FunSuite {

  test("Option#orNull cannot be used") {
    val result = WartRemoverTest(OptionOrNull) {
      def x = sys.env.get("some env").orNull
    }

    assert(result.errors == List(OptionOrNull.errorMessage))
  }

  test("OptionOrNull respects @SuppressWarnings") {
    val result = WartRemoverTest(OptionOrNull) {
      @SuppressWarnings(Array("im.tox.tox4j.lint.OptionOrNull"))
      def x = sys.env.get("another env").orNull
    }

    assert(result.errors == Nil)
  }

  // A method called "orNull" should probably be flagged, anyway,
  // but this checker only looks for Option#orNull.
  test("other orNull methods are not flagged") {
    val result = WartRemoverTest(OptionOrNull) {
      class X { def orNull: String = "not really null" }
      def x = new X().orNull
    }

    assert(result.errors == Nil)
  }

}
