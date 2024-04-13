package Schema

case class Reviews(
                    likesCount : Int,
                    dislikesCount : Int,
                    full_review: String,
                    rating_value: String,
                    review_date: String,
                    reviewer_name: String,
                    reviewer_url: String,
                    short_review: String,
                    reviewId : String)