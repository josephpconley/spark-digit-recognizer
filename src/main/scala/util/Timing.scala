package util

trait Timing {
  val timing = new StringBuffer
  def timed[T](label: String, code: => T): T = {
    val start = System.currentTimeMillis()
    val result = code
    val stop = System.currentTimeMillis()

    val msg = s"Processing $label took ${stop - start} ms.\n"
    println(msg)
    timing.append(msg)
    result
  }
}
