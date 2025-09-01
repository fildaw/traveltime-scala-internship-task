# Internship task

Link to the description: [TravelTime GitHub repo](https://github.com/traveltime-dev/internship-task)

## How to run
1. Install Scala Build Tool from [Scala official website](https://www.scala-lang.org/download/)
2. Run commands below:

```console
filip@pc:~$ cd traveltime-scala-internship-task
filip@pc:~/traveltime-scala-internship-task$ sbt
sbt:traveltime-scala-internship-task> run <locations_file_path> <regions_file_path> [output_file_path]
```

   The `locations_file_path` and `regions_file_path` are paths to json files with locations and regions, respectively. \
   The `output_file_path` is optional, if left blank, the output of the program would be saved to `output.json` in current working directory.

## How to run tests
```console
filip@pc:~$ cd traveltime-scala-internship-task
filip@pc:~/traveltime-scala-internship-task$ sbt
sbt:traveltime-scala-internship-task> test
```
Unit tests are implemented with the help of [ScalaTest](https://www.scalatest.org/) library.

## Docker
There is a `Dockerfile` along with `docker-compose`, so you can build an image and run it using standard Docker commands:
```console
filip@pc:~$ cd traveltime-scala-internship-task
filip@pc:~/traveltime-scala-internship-task$ docker compose build
filip@pc:~/traveltime-scala-internship-task$ docker compose up
```
The container defined in `docker-compose` has a bind mount to provide a folder on a host machine for the app to read input files 
(`locations.json` and `regions.json`) and store the output file (`output.json`). \
By default the folder is set to `./json_files`. 
To change it, move the input files to desired location and edit the `FILES_FOLDER` variable in `.env` file to a new path.

## Libraries used
- [S2 Geometry](http://s2geometry.io) - for spherical calculations
- [Circe](https://github.com/circe/circe) - for JSON parsing
- [ScalaTest](https://www.scalatest.org/) - for unit tests
