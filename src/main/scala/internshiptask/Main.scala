package internshiptask

object Main {
  def matchRegionsWithLocations(regions: List[Region], locations: List[Location]): List[RegionWithLocations] = {
    for (region <- regions) yield RegionWithLocations.matchWithLocations(region, locations)
  }

  def main(args: Array[String]): Unit = {
    val (locationSrc, regionSrc, outputWriter) = FileIO.parseArgs(args) match {
      case Some(f) => f
      case _ => return
    }
    val (locations, regions) = Array(
      (FileIO.readJsonFileToGeoList[Location](locationSrc), "locations file"),
      (FileIO.readJsonFileToGeoList[Region](regionSrc), "regions file"),
    ) flatMap {
      case (Right(l), _) => Some(l)
      case (Left(e), context) =>
        println(f"Error: ${e.getMessage} in $context")
        None
    } match {
      case Array(l: List[Location], r: List[Region]) => (l, r)
      case _ => return
    }

    val matches = matchRegionsWithLocations(regions, locations)
    FileIO.writeResultsToOutput(matches, outputWriter)
  }
}
