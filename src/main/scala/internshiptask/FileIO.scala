package internshiptask
import io.circe.parser.decode
import io.circe.{Decoder, Error}

import java.io.PrintWriter
import scala.io.{BufferedSource, Source}

object Help {
  def unapply(s: String): Boolean = s == "-h" || s == "--help"
}

object FileInArg {
  def unapply(s: String): Option[BufferedSource] = {
    if (new java.io.File(s).exists) {
      return Some(Source.fromFile(s))
    }
    None
  }
}

object FileOutArg {
  def unapply(s: String): Option[PrintWriter] = Some(new PrintWriter(s))
}

object FileIO {
  private val defaultOutputFile = "output.json"

  private def printHelp(): Unit = {
    println("Help:")
    println("Arguments: [--help] <locations_file_path> <regions_file_path> [output_file_path]")
  }

  private def printInvalidUsage(): Unit = {
    println("Invalid usage or input files cannot be opened!")
    printHelp()
  }

  def parseArgs(args: Array[String]): Option[(BufferedSource, BufferedSource, PrintWriter)] = args match {
    case Array(_ @ Help(), _*) =>
      printHelp()
      None
    case Array(FileInArg(locationSource), FileInArg(regionSource)) =>
      Some((locationSource, regionSource, new PrintWriter(defaultOutputFile)))
    case Array(FileInArg(locationSource), FileInArg(regionSource), FileOutArg(output)) =>
      Some((locationSource, regionSource, output))
    case _ =>
      printInvalidUsage()
      None
  }

  def readJsonFileToGeoList[A <: GeoType](src: BufferedSource)(implicit decoder: Decoder[A]) =
    decode[List[A]](src.getLines().mkString)
}