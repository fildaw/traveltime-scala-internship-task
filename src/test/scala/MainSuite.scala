import internshiptask.{Coord, Location, Main, Polygon, PolygonError, Region}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers.contain
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class MainSuite extends AnyFunSuite {
  test("polygon with crossing edges") {
    val caught = intercept[PolygonError] {
      List(
        Region("crossed edges", List(
          Polygon(List(
            Coord(21.360484214690757, -3.309063746833246),
            Coord(21.19015513364417, -4.730892314196595),
            Coord(23.36771570550738, -4.713339678791314),
            Coord(23.600558965317532, -2.0727730560644204),
            Coord(24.725131316810405, -3.589725405031274),
            Coord(21.360484214690757, -3.309063746833246),
          ))
        )
      ))
    }
    caught.getMessage shouldBe "Loop 0: Edge 2 crosses edge 4"
  }
  test("polygon with degenerate edge") {
    val caught = intercept[PolygonError] {
      List(
        Region("degenerate", List(
          Polygon(List(
            Coord(-17.578125, -83.2364265),
            Coord(129.0234375, -83.7155443),
            Coord(129.0234375, -83.7155443),
            Coord(66.09375, -72.8160737),
            Coord(-17.578125, -83.2364265)
          ))
        ))
      )
    }
    caught.getMessage shouldBe "Loop 0: Edge 1 is degenerate (duplicate vertex)."
  }

  test("polygon and points near south pole") {
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
    result.head.locations should contain theSameElementsAs Set(locations(0), locations(2), locations(4))
  }

  test("polygon crossing dateline") {
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
    result.head.locations should contain theSameElementsAs Set(locations(0), locations(1), locations(2), locations(5))
  }

  test("region with multiple polygons") {
    val regions = List(
      Region("bory_tucholskie", List(
        Polygon(List(
          Coord(17.83255356035076, 54.113263890982125),
          Coord(17.795452693995202, 54.02579830288022),
          Coord(17.74990772923971, 53.93873091663022),
          Coord(18.0726316744757, 53.84985082823309),
          Coord(18.30621001515354, 53.78381152974012),
          Coord(18.26005444680112, 53.92340158940269),
          Coord(18.04030450061913, 54.028749419977316),
          Coord(17.998325231424843, 54.094894457634155),
          Coord(17.83255356035076, 54.113263890982125)
        )),
        Polygon(List(
          Coord(17.446081519786787, 53.885876286371314),
          Coord(17.466045794468016, 53.73833867219909),
          Coord(17.694912495793687, 53.75581313064666),
          Coord(17.73700434073797, 53.82123216564551),
          Coord(17.56648787290615, 53.89511892541117),
          Coord(17.446081519786787, 53.885876286371314),
        ))
      ))
    )
    val locations = List(
      Location("Location 1", Coord(18.04777597110123, 53.931943242940264)), // should be matched
      Location("Location 2", Coord(17.55344865541619, 53.7702198592585)), // should be matched
      Location("Location 3", Coord(17.84555741202533, 54.08518088704679)), // should be matched
      Location("Location 4", Coord(17.590412921065422, 53.903982576424795)),
    )
    val result = Main.matchRegionsWithLocations(regions, locations)
    result.head.locations should contain theSameElementsAs Set(locations(0), locations(1), locations(2))
  }

  test("test locations and overlapping regions") {
    val regions = List(
      Region("tatry_slovakia", List(
        Polygon(List(
          Coord(19.67847490452553, 49.37351801413155),
          Coord(19.304812334103275, 49.23803996442288),
          Coord(19.328347447593416, 49.097478621327554),
          Coord(19.547539671978996, 49.16226149904949),
          Coord(19.795878887634984, 49.125183242944075),
          Coord(19.67847490452553, 49.37351801413155),
        ))
      )),
      Region("tatry_poland_slovakia", List(
        Polygon(List(
          Coord(19.855860471519293, 49.31803102546846),
          Coord(19.70351226362419, 49.250310004550215),
          Coord(19.757887614103993, 49.17191660346475),
          Coord(20.25482071219585, 49.15403200004263),
          Coord(20.332680386606796, 49.26147108985464),
          Coord(19.855860471519293, 49.31803102546846),
        ))
      ))
    )

    val locations = List(
      Location("Location 1", Coord(19.726640710592307, 49.24340413142335)), // should be matched in both regions
      Location("Location 2", Coord(19.36788978252892, 49.232581877359536)), // should be matched in region 1
      Location("Location 3", Coord(20.219267732042425, 49.24476375835607)), // should be matched in region 2
      Location("Location 4", Coord(19.561924809724104, 49.399912837692284)), // shouldn't be matched
    )

    val result = Main.matchRegionsWithLocations(regions, locations)

    result.length shouldEqual 2
    val region1 = result.head
    val region2 = result(1)
    region1.name shouldEqual "tatry_slovakia"
    region2.name shouldEqual "tatry_poland_slovakia"

    region1.locations should contain theSameElementsAs Set(locations(0), locations(1))
    region2.locations should contain theSameElementsAs Set(locations(0), locations(2))
  }
}
