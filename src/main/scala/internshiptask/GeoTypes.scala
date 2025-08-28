package internshiptask

import io.circe.Decoder

trait GeoType

case class Coord(lon: Double, lat: Double) extends GeoType
case class Location(name: String, coord: Coord) extends GeoType
case class Polygon(ring: List[Coord]) extends GeoType
case class Region(name: String, polygons: List[Polygon]) extends GeoType

object Coord {
  implicit val decodeCoord: Decoder[Coord] = Decoder[(Double, Double)].map(p => Coord(p._1, p._2))
}

object Location {
  implicit val decodeLocation: Decoder[Location] = Decoder.instance(a =>
    for {
      n <- a.downField("name").as[String]
      c <- a.downField("coordinates").as[Coord]
    } yield Location(n, c)
  )
}

object Polygon {
  implicit val decodePolygon: Decoder[Polygon] = Decoder[List[Coord]].map(p => Polygon(p))
}

object Region {
  implicit val decodeRegion: Decoder[Region] = Decoder.instance(a =>
    for {
      n <- a.downField("name").as[String]
      c <- a.downField("coordinates").as[List[Polygon]]
    } yield Region(n, c)
  )
}
