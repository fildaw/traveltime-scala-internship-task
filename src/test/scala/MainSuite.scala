import internshiptask.{Coord, Location, Main, Polygon, Region}
import org.scalatest.funsuite.AnyFunSuite

class MainSuite extends AnyFunSuite {
  test("south_pole") {
    val regions = List(
      Region("south_pole", List(
        Polygon(List(
          Coord(-17.578125, -83.2364265),
          Coord(129.0234375, -83.7155443),
          Coord(66.09375, -72.8160737),
          Coord(-17.578125, -83.2364265)
        ))
      ))
    )
    val locations = List(
      Location("Location 1", Coord(55.1074219, -81.3215926)),
      Location("Location 2", Coord(126.2109375, -80.2979271)),
      Location("Location 3", Coord(74.1796875, -82.6313329)),
      Location("Location 4", Coord(-125.5078125, -82.0700282)),
      Location("Location 5", Coord(66.09375, -74.4964131))
    )
    val result = Main.matchRegionsWithLocations(regions, locations)
    assert(result.head.locations.toList == locations(0) :: locations(2) :: locations(4) :: Nil)
  }

  test("dateline_crossing") {
    val regions = List(
      Region("dateline_crossing", List(
        Polygon(List(
          Coord(179.45182047167452, 1.9729085791280596),
          Coord(221.5167493379924, 10.166318036315872),
          Coord(203.57476142603832, 33.674251105897056),
          Coord(185.73842360857066, 25.923204491260876),
          Coord(193.75697160392338, 12.993861779456438),
          Coord(179.45182047167452, 1.9729085791280596)
        ))
      ))
    )
    val locations = List(
      Location("Location 1", Coord(192.3397, 15.9375)),
      Location("Location 2", Coord(209.0477, 14.3281)),
      Location("Location 3", Coord(202.5000, 30.0071)),
      Location("Location 4", Coord(189.1316, 15.4924)),
      Location("Location 5", Coord(-179.4427, 14.9922)),
      Location("Location 6", Coord(182.4744, 3.6359))
    )
    val result = Main.matchRegionsWithLocations(regions, locations)
    assert(result.head.locations.toList == locations(0) :: locations(1) :: locations(2) :: locations(5) :: Nil)
  }
}
