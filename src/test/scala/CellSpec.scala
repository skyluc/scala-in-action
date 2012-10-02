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

import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers

class CellSpec extends WordSpec with MustMatchers {

  "Calling neighbours" should {
    "return the surrounding Cells" in {
      Cell(0, 0).neighbours must be === Set(
        Cell(-1, -1), Cell(0, -1), Cell(1, -1),
        Cell(-1, 0), Cell(1, 0),
        Cell(-1, 1), Cell(0, 1), Cell(1, 1)
      )
    }
  }
}
