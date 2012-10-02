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

class Generation(val aliveCells: Set[Cell] = Set.empty) {

  def next: Generation =
    new Generation(stayingAlive ++ born)

  def stayingAlive =
    aliveCells filter canStayAlive

  def born =
    aliveCells flatMap deadNeighbours filter canBeBorn

  def canStayAlive(cell: Cell) =
    Set(2, 3) contains aliveNeighbours(cell).size

  def canBeBorn(cell: Cell) =
    aliveNeighbours(cell).size == 3

  def aliveNeighbours(cell: Cell) =
    cell.neighbours intersect aliveCells

  def deadNeighbours(cell: Cell) =
    cell.neighbours diff aliveCells
}
