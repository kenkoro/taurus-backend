package com.kenkoro.taurus.api.client.models

import com.kenkoro.taurus.api.client.models.enums.UserProfile
import com.kenkoro.taurus.api.client.models.util.PGEnum
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.jetbrains.exposed.sql.Table

@Serializable(with = SaltWrapperSerializer::class)
@JvmInline
value class SaltWrapper(val salt: String)

object SaltWrapperSerializer : KSerializer<SaltWrapper> {
  override fun deserialize(decoder: Decoder): SaltWrapper {
    return SaltWrapper(decoder.decodeString())
  }

  override val descriptor: SerialDescriptor
    get() = PrimitiveSerialDescriptor("Salt", PrimitiveKind.STRING)

  override fun serialize(
    encoder: Encoder,
    value: SaltWrapper,
  ) {
    encoder.encodeString(value.salt)
  }
}

@Serializable
data class User(
  @SerialName("user_id") val userId: Int,
  val subject: String,
  val password: String,
  val image: String,
  @SerialName("first_name") val firstName: String,
  @SerialName("last_name") val lastName: String,
  val email: String,
  val profile: UserProfile,
  @SerialName("salt") val saltWrapper: SaltWrapper,
)

@Serializable
data class NewUser(
  val subject: String,
  val password: String,
  val image: String,
  @SerialName("first_name") val firstName: String,
  @SerialName("last_name") val lastName: String,
  val email: String,
  val profile: UserProfile,
)

fun NewUser.setHashedPassword(password: String): NewUser =
  this.copy(
    subject = subject,
    password = password,
    image = image,
    firstName = firstName,
    lastName = lastName,
    email = email,
    profile = profile,
  )

object Users : Table() {
  override val primaryKey: PrimaryKey
    get() = PrimaryKey(userId)
  val userId = integer("user_id").autoIncrement()
  val subject = varchar("subject", 32)
  val password = varchar("password", 255)
  val image = varchar("image", 128)
  val firstName = varchar("first_name", 32)
  val lastName = varchar("last_name", 32)
  val email = varchar("email", 128)
  val profile =
    customEnumeration(
      "profile",
      "user_profile",
      fromDb = { value -> UserProfile.valueOf(value as String) },
      toDb = { PGEnum("user_profile", it) },
    )
  val salt = varchar("salt", 128)
}
