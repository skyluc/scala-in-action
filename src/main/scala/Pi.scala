/*
 * Copyright 2012 Heiko Seeberger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import scala.concurrent.{ Await, Future }
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

object Pi {

  def seqPi(n: Int): BigDecimal =
    logTime {
      series(gregoryLeibnitz)(0, n)
    }

  def parPi(n: Int): BigDecimal =
    logTime {
      val processors = Runtime.getRuntime.availableProcessors
      val chunkSize = n / processors
      val chunkSums = // Seq[Future[BigDecimal]
        0 until processors map { p =>
          val from = p * chunkSize
          val until = from + chunkSize
          Future(series(gregoryLeibnitz)(from, until))
        }
      val sum = Future sequence chunkSums map (_.sum) // Future[Seq[BigDecimal]]
      Await.result(sum, Duration.Inf)
    }

  def gregoryLeibnitz(n: Int): BigDecimal = {
    require(n >= 0, "n must not be negative!")
    4.0 * (1 - (n % 2) * 2) / (n * 2 + 1)
  }

  def series(term: Int => BigDecimal)(from: Int, until: Int): BigDecimal = {
    require(from < until, "from must be less than until!")
    var n = from
    var acc = BigDecimal(0.0)
    while (n < until) {
      acc += term(n)
      n += 1
    }
    acc
  }

  def logTime[A](block: => A): A = {
    val startTime = System.currentTimeMillis
    val result = block
    val duration = System.currentTimeMillis - startTime
    println(s"seqPi took $duration milliseconds.")
    result
  }
}
