package Schema

case class ReviewsData(ImdbId: String,
                       _id: String,
                       reviews: Seq[Reviews])
