package Schema

case class AddReviewPayload( movieId: String,
                             rating: Double,
                             review: String,
                             reviewer_name: String,
                             reviewId : String)
