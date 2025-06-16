package ru.tinkoff.favourite_persons.utils

import com.github.javafaker.Faker
import ru.tinkoff.favouritepersons.data.network.Coordinates
import ru.tinkoff.favouritepersons.data.network.Dob
import ru.tinkoff.favouritepersons.data.network.ID
import ru.tinkoff.favouritepersons.data.network.Location
import ru.tinkoff.favouritepersons.data.network.Login
import ru.tinkoff.favouritepersons.data.network.Name
import ru.tinkoff.favouritepersons.data.network.PersonDto
import ru.tinkoff.favouritepersons.data.network.Picture
import ru.tinkoff.favouritepersons.data.network.Street
import ru.tinkoff.favouritepersons.data.network.Timezone
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Locale
import java.util.UUID

object FakePersonFactory {
    private val faker = Faker(Locale.ENGLISH)

    private fun calculateAge(birthDateInstant: Instant): Long {
        val birthDate = birthDateInstant.atZone(ZoneId.systemDefault()).toLocalDate()
        val today = LocalDate.now()
        return ChronoUnit.YEARS.between(birthDate, today)
    }

    fun createPerson(): PersonDto {
        val gender = faker.demographic().sex().lowercase()
        val genderPath = if (gender == "male") "men" else "women"

        val birthDateInstant = faker.date().birthday(20, 80).toInstant()
        val age = calculateAge(birthDateInstant)

        val registeredDateInstant = faker.date().past(1000, java.util.concurrent.TimeUnit.DAYS).toInstant()
        val registeredAge = calculateAge(registeredDateInstant)

        return PersonDto(
            gender = gender,
            name = Name(
                title = faker.name().prefix(),
                first = faker.name().firstName(),
                last = faker.name().lastName()
            ),
            location = Location(
                street = Street(
                    number = faker.number().numberBetween(1, 9999).toLong(),
                    name = faker.address().streetName()
                ),
                city = faker.address().city(),
                state = faker.address().state(),
                country = faker.address().country(),
                postcode = faker.address().zipCode(),
                coordinates = Coordinates(
                    latitude = faker.address().latitude(),
                    longitude = faker.address().longitude()
                ),
                timezone = Timezone(
                    offset = "+${faker.number().numberBetween(1, 12)}:00",
                    description = "Fake TZ"
                )
            ),
            email = faker.internet().emailAddress(),
            login = Login(
                uuid = UUID.randomUUID().toString(),
                username = faker.name().username(),
                password = faker.internet().password(),
                salt = faker.lorem().characters(8),
                md5 = faker.crypto().md5(),
                sha1 = faker.crypto().sha1(),
                sha256 = faker.crypto().sha256()
            ),
            dob = Dob(
                date = birthDateInstant.toString(),
                age = age
            ),
            registered = Dob(
                date = registeredDateInstant.toString(),
                age = registeredAge
            ),
            phone = faker.phoneNumber().phoneNumber(),
            cell = faker.phoneNumber().cellPhone(),
            id = ID("BSN", faker.idNumber().valid()),

            picture = Picture(
                large = "https://randomuser.me/api/portraits/$genderPath/$age.jpg",
                medium = "https://randomuser.me/api/portraits/med/$genderPath/$age.jpg",
                thumbnail = "https://randomuser.me/api/portraits/thumb/$genderPath/$age.jpg"
            ),
            nat = faker.nation().nationality()
        )
    }
}
