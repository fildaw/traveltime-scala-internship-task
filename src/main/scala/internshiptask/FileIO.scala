package internshiptask

import scala.util.{Failure, Success, Try, Using}
import io.circe.parser.decode
import io.circe.syntax._
import io.circe.{Decoder, Error, ParsingFailure}
import scopt.OParser

import java.io.{File, PrintWriter}
import scala.io.{BufferedSource, Source}

case class AppConfig(
  locationsFile: BufferedSource = null,
  regionsFile: BufferedSource = null,
  outputWriter: File = new File("output.json"))

object FileIO {
  private val builder = OParser.builder[AppConfig]
  private val cliParser = {
    import builder._
    OParser.sequence(
      programName("traveltime-scala-internship-task"),
      arg[File]("<locations_file_path>")
        .required()
        .action((x, config) => config.copy(locationsFile = Source.fromFile(x))),
      arg[File]("<regions_file_path>")
        .required()
        .action((x, config) => config.copy(regionsFile = Source.fromFile(x))),
      arg[File]("<output_file_path>")
        .optional()
        .action((x, config) => config.copy(outputWriter = x))
    )
  }

  def parseArgs(args: Array[String]): Option[AppConfig] = {
    OParser.parse(cliParser, args, AppConfig())
  }

  def readJsonFileToGeoList[A <: GeoType](src: BufferedSource)(implicit decoder: Decoder[A]): Either[String, List[A]] = {
    Try(decode[List[A]](src.getLines().mkString)) match {
      case Failure(geoError) => Left(geoError.getMessage)
      case Success(circeEither) => circeEither.left.map(_.getMessage)
    }
  }

  def writeResultsToOutput(res: List[RegionWithLocations], output: File): Unit =
    Using.resource(new PrintWriter(output))(_.write(res.asJson.toString))
}