package controllers

import models._
import services.MovieService

object MovieController {

  def getAllMovies: Vector[Movie] = MovieService.getAllMovies

  def addReview(movieId: String, review: Review): Option[Movie] = {

    val movieToUpdate = getAllMovies.find(_.id == movieId)
    val updatedMovie = movieToUpdate.map(movie => movie.copy(reviews = movie.reviews :+ review))

    (movieToUpdate, updatedMovie) match {
      case (Some(input), Some(output)) =>
        if (MovieService.updateMovie(input, output)) updatedMovie else None
      case _ => None
    }
  }

  def getMoviesByGenres(genres: Seq[Genre.Value]): Vector[Movie] = getAllMovies.filter{ movie =>
    val similarityByGenre = genres.foldLeft(0){ (acc, nextGenre) =>
      if(movie.genres.contains(nextGenre)) acc + 1 else acc
    }
    similarityByGenre == genres.size
  }

  def getMovieById(id: String): Option[Movie] = getAllMovies.find(_.id == id)

  def getMovieByTitle(title: String): Option[Movie] = getAllMovies.find(_.title.toLowerCase() == title.toLowerCase)
}
