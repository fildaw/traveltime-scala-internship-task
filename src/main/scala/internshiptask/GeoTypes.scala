package internshiptask

import com.google.common.geometry.{S1Angle, S2Error, S2LatLng, S2Loop, S2Point, S2Polygon}
import io.circe.{Decoder, Encoder, Json}

import scala.jdk.CollectionConverters._

class PolygonError(private val s2error: S2Error, private val cause: Throwable = None.orNull) extends Exception(s2error.text(), cause) {}

trait GeoType

case class Coord(lon: Double, lat: Double) extends GeoType {
  val s2Point: S2Point = new S2LatLng(S1Angle.degrees(lat), S1Angle.degrees(lon)).toPoint
}
case class Location(name: String, coord: Coord) extends GeoType
case class Polygon(ringIn: List[Coord]) extends GeoType {
  private val ring = if (ringIn.head == ringIn.last) ringIn.dropRight(1) else ringIn // s2 geometry assumes that last point is connected with the first
  val s2Polygon: S2Polygon = {
    val poly = new S2Polygon(new S2Loop(ring.map(_.s2Point).asJava))
    val error = new S2Error()
    if (poly.findValidationError(error)) throw new PolygonError(error)
    poly
  }
  def contains(loc: Location): Boolean = s2Polygon.contains(loc.coord.s2Point)
}
case class Region(name: String, polygons: List[Polygon]) extends GeoType {
  def contains(loc: Location): Boolean = polygons.exists(_.contains(loc))
}
case class RegionWithLocations(name: String, locations: Set[Location])

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

object RegionWithLocations {
  def matchWithLocations(region: Region, locations: List[Location]): RegionWithLocations = {
    val matched = for (l <- locations; if region.contains(l)) yield l
    RegionWithLocations(region.name, matched.toSet)
  }

  implicit val encode: Encoder[RegionWithLocations] = Encoder.instance(r => Json.obj(
    ("region", Json.fromString(r.name)),
    ("matched_locations", Json.fromValues(r.locations.map(l => Json.fromString(l.name))))
  ))
}
