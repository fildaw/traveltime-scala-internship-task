package internshiptask

object Main {
  def main(args: Array[String]): Unit = {
    val (locationSrc, regionSrc, outputWriter) = FileIO.parseArgs(args) match {
      case Some(f) => f
      case _ => return
    }
    val (locations, regions) = Seq(
      (FileIO.readJsonFileToGeoList[Location](locationSrc), "locations file"),
      (FileIO.readJsonFileToGeoList[Region](regionSrc), "regions file"),
    ) flatMap {
      case (Right(l), _) => l
      case (Left(e), context) =>
        println(f"Error: ${e.getMessage} in $context")
        None
    } match {
      case Seq(l, r) => (l, r)
      case _ => return
    }
    println(locations, regions)
    /*val coords = List((-58.01147830609524,
      -63.7528771350633),(-102.98181712768798,
      -74.97433056385762),(-145.16633200976563,
      -75.5085867202767),(-195.97527000678406,
      -77.92148270066508),(170.16580882190863,
      -71.70372657405005),(140.19622268406562,
      -66.76316650730789),(96.6002704104597,
      -66.10449331882775),(74.20101106230811,
      -69.88060682080707),(33.89741504178102,
      -68.90011597177835),(0.27350477251502525,
      -69.87531992370273),(-36.59056620184094,
      -78.0530914008727))

    val polygon = new S2Polygon(
      new S2Loop(
        (coords map { case (lon,lat) => new S2LatLng(S1Angle.degrees(lat), S1Angle.degrees(lon)).toPoint }).asJava
      )
    )
    println(polygon.contains(new S2LatLng(S1Angle.degrees(-67.2462), S1Angle.degrees(-79.5515)).toPoint))*/
  }
}
