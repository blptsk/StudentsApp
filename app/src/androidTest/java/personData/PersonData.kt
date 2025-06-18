package personData

/**
 * @author a.m.sidenov
 */
data class PersonData(
    val name: String = "Иван",
    var surname: String = "Иванов",
    val gender: String = "М",
    val birthday: String = "2000-12-12",
    val email: String = "asdasd@mail.ru",
    val phone: String = "+79999999999",
    val address: String = "Улица пушкина",
    val photo: String = "https://randomuser.me/api/portraits/men/56.jpg",
    val score: String = "52"
)