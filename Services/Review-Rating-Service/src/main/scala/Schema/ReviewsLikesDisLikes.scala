package Schema

case class ReviewsLikesDisLikes(movieId: String,
                                userId: String,
                                likedDisLikedData: Seq[ReviewLikedDisLikedData])
