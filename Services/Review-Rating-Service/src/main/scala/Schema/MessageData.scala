package Schema

import upickle.default._

case class MessageData(eventType:String, payload: Payload)

