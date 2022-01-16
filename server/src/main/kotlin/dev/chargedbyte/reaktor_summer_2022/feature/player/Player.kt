package dev.chargedbyte.reaktor_summer_2022.feature.player

import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId

data class Player(@BsonId val id: Id<Player> = newId(), val name: String)
