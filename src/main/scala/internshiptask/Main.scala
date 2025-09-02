package internshiptask

object Main {
  def matchRegionsWithLocations(regions: List[Region], locations: List[Location]): List[RegionWithLocations] = {
    for (region <- regions) yield RegionWithLocations.matchWithLocations(region, locations)
  }

  def main(args: Array[String]): Unit = {
    FileIO.parseArgs(args) match {
      case Some(config) =>
        val read = List(
          (FileIO.readJsonFileToGeoList[Location](config.locationsFile), "locations file"),
          (FileIO.readJsonFileToGeoList[Region](config.regionsFile), "regions file"),
        )
        val res: Either[List[(String, String)], (List[Location], List[Region])] = read match {
          case (Right(l: List[Location]), _) :: (Right(r: List[Region]), _) :: Nil => Right((l, r))
          case _ =>
            val errors = read.collect {
              case (Left(err), context) => (err, context)
            }
            Left(errors)
        }

        res match {
          case Right((locations, regions)) =>
            val matches = matchRegionsWithLocations(regions, locations)
            FileIO.writeResultsToOutput(matches, config.outputWriter)
          case Left(errorsWithContexts) =>
            for {
              (err, context) <- errorsWithContexts
            } println(f"Error: $err in $context")
        }
      case _ => println("Cannot parse arguments!")
    }

  }
}
