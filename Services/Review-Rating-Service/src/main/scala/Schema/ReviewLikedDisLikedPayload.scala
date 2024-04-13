package Schema

case class ReviewLikedDisLikedPayload(movieId: String,
                               reviewId: String,
                              isLiked : Boolean,
                              isDisLiked: Boolean,
                                      userId:String)
