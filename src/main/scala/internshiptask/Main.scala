package internshiptask

import cats.Applicative
import cats.data.Validated.{Invalid, Valid}
import cats.data.{Validated, ValidatedNel}

object Main {
  def matchRegionsWithLocations(regions: List[Region], locations: List[Location]): List[RegionWithLocations] = {
    for (region <- regions) yield RegionWithLocations.matchWithLocations(region, locations)
  }

  def main(args: Array[String]): Unit = {
    FileIO.parseArgs(args) match {
      case Some(config) =>
        type ErrorWithContext = (String, String)
        type ErrorOr[A] = ValidatedNel[ErrorWithContext, A]

        val errorOrLocations = Validated.fromEither(FileIO.readJsonFileToGeoList[Location](config.locationsFile))
          .leftMap((_, "locations file")).toValidatedNel
        val errorOrRegions = Validated.fromEither(FileIO.readJsonFileToGeoList[Region](config.regionsFile))
          .leftMap((_, "regions file")).toValidatedNel
        val errorsOrResults: ErrorOr[List[RegionWithLocations]] =
          Applicative[ErrorOr].map2(errorOrRegions, errorOrLocations)(matchRegionsWithLocations)

        errorsOrResults match {
          case Valid(results) =>
            FileIO.writeResultsToOutput(results, config.outputWriter)
          case Invalid(errorsWithContext) =>
            for {
              (err, context) <- errorsWithContext.iterator
            } println(f"Error: $err in $context")
            sys.exit(1)
        }
      case _ =>
        println("Cannot parse arguments!")
        sys.exit(1)
    }

  }
}
