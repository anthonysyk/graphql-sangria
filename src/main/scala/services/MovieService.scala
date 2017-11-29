package services

import models._

object MovieService {

  def getAllMovies: Vector[Movie] = MockedData.movies

  def updateMovie(inputMovie: Movie, outputMovie: Movie): Boolean = {
    val index = getAllMovies.indexOf(inputMovie)
    MockedData.movies = MockedData.movies.updated(index, outputMovie)

    if (MockedData.movies.contains(outputMovie)) {
      println("Successfuly updated movie")
      true
    } else {
      println("Failed to update movie")
      false
    }
  }

}
