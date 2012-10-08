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

import org.apache.http.client.methods.HttpGet
import org.apache.http.client.utils.URIBuilder
import org.apache.http.impl.client.{ BasicResponseHandler, DefaultHttpClient }
import scala.util.control.Exception
import scala.xml.XML

object Weather {

  private val locations = List(
    "Berlin,Germany",
    "Cologne,Germany",
    "Hamburg,Germany",
    "Munich,Germany",
    "Nuremberg,Germany",
    "Passau,Germany",
    "Stuttgart,Germany",
    "Bath,England",
    "London,England",
    "Manchester,England",
    "Dublin,Ireland",
    "Lille,France",
    "Marseille,France",
    "Paris,France",
    "Boston,USA",
    "Chicago,USA",
    "Dallas,USA",
    "Denver,USA",
    "Los Angeles,USA",
    "Miami,USA",
    "New Orleans,USA",
    "New York,USA",
    "San Francisco,USA",
    "Seattle,USA"
  )

  def main(args: Array[String]): Unit = {

    val isParallel = args.headOption map (_ == "par") getOrElse false
    val time0 = System.currentTimeMillis
    val currentTemperatures =
      (if (isParallel) locations.par else locations) flatMap currentTemperature
    val time1 = System.currentTimeMillis
    val averageTemperature = currentTemperatures.sum / currentTemperatures.size
    println(
      s"Calculated avg. temperature of about $averageTemperature °C out of ${currentTemperatures.size} values in ${time1 - time0} ms."
    )
  }

  def currentTemperature(location: String): Option[Int] = {
    val uri = (new URIBuilder)
      .setScheme("http")
      .setHost("free.worldweatheronline.com")
      .setPath("/feed/weather.ashx")
      .setParameter("key", "7b984acc78180346120210")
      .setParameter("format", "xml")
      .setParameter("num_of_days", "1")
      .setParameter("q", location)
      .build
    val httpClient = new DefaultHttpClient
    try {
      val response = XML loadString httpClient.execute(new HttpGet(uri), new BasicResponseHandler)
      println(s"Got response for $location")
      for {
        temperatureNode <- (response \\ "current_condition" \ "temp_C").headOption
        temperature <- Exception.allCatch opt temperatureNode.text.toInt
      } yield temperature
    } finally httpClient.getConnectionManager.shutdown()
  }

  def currentTemperatureFallback(location: String): Option[Int] = {
    Thread.sleep(200)
    println(s"Got response for $location")
    Some(scala.util.Random.nextInt(35))
  }
}
