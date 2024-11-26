package ru.tinkoff.favouritepersons

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.http.Fault
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import ru.tinkoff.favouritepersons.presentation.activities.MainActivity
import ru.tinkoff.favouritepersons.room.PersonDataBase
import ru.tinkoff.favouritepersons.rules.LocalhostPreferenceRule
import ru.tinkoff.favouritepersons.screens.MainScreenPage
import ru.tinkoff.favouritepersons.screens.NewStudentPage
import ru.tinkoff.favouritepersons.utils.AppStubber
import ru.tinkoff.favouritepersons.utils.StudentData
import ru.tinkoff.favouritepersons.utils.fileToString
import java.time.LocalDate

class ApplicationTest : TestCase(kaspressoBuilder) {

    @Before
    fun createDb() {
        db = PersonDataBase.getDBClient(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    @After
    fun clearDB() {
        db.personsDao().clearTable()
    }

    @get: Rule
    val ruleChain: RuleChain = RuleChain.outerRule(LocalhostPreferenceRule())
        .around(WireMockRule(5000))
        .around(ActivityScenarioRule(MainActivity::class.java))

    //"Кейс 1. Проверка скрытия сообщения об отсутствии студентов"
    @Test
    fun textNoPersonsIsNotDisplayedTest() {
        stubber.stubForApi("random_person.json", studentAge.toString(), studentInfo)
        MainScreenPage {
            noPersonsTitle.isDisplayed()
            addPersonButton.click()
            addRandomPersonButton.click()
            noPersonsTitle.isNotDisplayed()
        }
    }

    //Кейс 2. Проверка удаления студента"
    @Test
    fun deleteStudentTest() {
        stubber.stubForApi("random_person.json", studentAge.toString(), studentInfo)
        MainScreenPage {
            addPersonButton.click()
            addRandomPersonButton.click()
            addRandomPersonButton.click()
            addRandomPersonButton.click()
            studentCard {
                hasSize(3)
            }
            deleteStudent()
            studentCard {
                hasSize(2)
            }
        }
    }

    @Test
    //Кейс 3. Проверка выбора по умолчанию в окне сортировки
    fun checkDefaultValueTest() {
        MainScreenPage {
            sortButton.click()
            sortDefault.isChecked()
        }
    }

    @Test
    //Кейс 4. Проверка сортировки по возрасту
    fun checkSortByAgeTest() {
        stubFor(
            get(urlEqualTo("/api/"))
                .inScenario("sortByAge")
                .whenScenarioStateIs(STARTED)
                .willSetStateTo("Add Second Person")
                .willReturn(
                    ok(
                        fileToString("random_person.json")
                            .replace("\"STUDENT_AGE\"", "15")
                            .replace("STUDENT_NAME", studentInfo.name)
                            .replace("STUDENT_SURNAME", studentInfo.surname)
                            .replace("STUDENT_GENDER", "M")
                            .replace("STUDENT_BDATE", studentInfo.birthdate)
                    )
                )
        )
        stubFor(
            get(urlEqualTo("/api/"))
                .inScenario("sortByAge")
                .whenScenarioStateIs("Add Second Person")
                .willSetStateTo("Add Third Person")
                .willReturn(
                    ok(
                        fileToString("random_person.json")
                            .replace("\"STUDENT_AGE\"", "38")
                            .replace("STUDENT_NAME", studentInfo.name)
                            .replace("STUDENT_SURNAME", studentInfo.surname)
                            .replace("STUDENT_GENDER", "M")
                            .replace("STUDENT_BDATE", studentInfo.birthdate)
                    )
                )
        )
        stubFor(
            get(urlEqualTo("/api/"))
                .inScenario("sortByAge")
                .whenScenarioStateIs("Add Third Person")
                .willReturn(
                    ok(
                        fileToString("random_person.json")
                            .replace("\"STUDENT_AGE\"", "20")
                            .replace("STUDENT_NAME", studentInfo.name)
                            .replace("STUDENT_SURNAME", studentInfo.surname)
                            .replace("STUDENT_GENDER", "M")
                            .replace("STUDENT_BDATE", studentInfo.birthdate)
                    )
                )
        )
        MainScreenPage {
            addPersonButton.click()
            addRandomPersonButton.click()
            addRandomPersonButton.click()
            addRandomPersonButton.click()
            sortButton.click()
            sortAge.click()
            checkPrivateInfoText(0, "Male, 38")
            checkPrivateInfoText(1, "Male, 20")
            checkPrivateInfoText(2, "Male, 15")
        }
    }

    @Test
    //Кейс 5. Проверка открытия второго экрана с данными пользователя
    fun checkStudentInfoTest() {
        stubber.stubForApi("random_person.json", studentAge.toString(), studentInfo)
        MainScreenPage {
            addPersonButton.click()
            addRandomPersonButton.click()
            studentCard {
                firstChild<MainScreenPage.CardView> {
                    click()
                }
            }
        }
        NewStudentPage {
            checkStudentInfoFields(
                studentInfo.name,
                studentInfo.surname,
                studentInfo.gender,
                studentInfo.birthdate
            )
        }
    }

    @Test
    //Кейс 6. Проверка редактирования студента
    fun editStudentInfoTest() {
        stubber.stubForApi("random_person.json", studentAge.toString(), studentInfo)
        MainScreenPage {
            addPersonButton.click()
            addRandomPersonButton.click()
            studentCard {
                firstChild<MainScreenPage.CardView> {
                    click()
                }
            }
        }
        NewStudentPage {
            nameField.replaceText(NEW_STUDENT_NAME)
            submitButton.click()
        }
        MainScreenPage {
            checkName(0, NEW_STUDENT_NAME, studentInfo.surname)
        }
    }

    @Test
    //Кейс 7. Проверка добавления студента
    fun addNewStudentTest() {
        MainScreenPage {
            addPersonButton.click()
            addPersonManuallyButton.click()
        }
        NewStudentPage {
            fillStudentInfo(studentInfo)
        }
        MainScreenPage {
            checkStudentCard(0, studentInfo, studentAge)
        }
    }

    @Test
    //Кейс 8. Проверка отображения сообщения об ошибке
    fun checkGenderFieldErrorTest() {
        MainScreenPage {
            addPersonButton.click()
            addPersonManuallyButton.click()
        }
        NewStudentPage {
            submitButton.click()
            genderFieldError.hasText("Поле должно быть заполнено буквами М или Ж")
        }
    }

    @Test
    //Кейс 9. Проверка скрытия сообщения об ошибке при вводе данных в поле
    fun checkHidingGenderFieldErrorTest() {
        MainScreenPage {
            addPersonButton.click()
            addPersonManuallyButton.click()
        }
        NewStudentPage {
            genderField.replaceText("3")
            submitButton.click()
            genderFieldError.hasText("Поле должно быть заполнено буквами М или Ж")
            genderField.replaceText("")
            genderFieldError.isNotDisplayed()
        }
    }

    @Test
    //Кейс 10. Проверка отображения сообщения об ошибке интернет-соединения
    fun checkInternetError() = run {

        stubFor(
            get("/api/")
                .willReturn(
                    aResponse()
                        .withFault(Fault.CONNECTION_RESET_BY_PEER)
                )
        )

        MainScreenPage {
            addPersonButton.click()
            addRandomPersonButton.click()
            step("Проверка отображения снекбара") {
                checkSnackbarIsDisplayed()
            }
        }
    }

    private companion object {
        private val stubber = AppStubber()
        private lateinit var db: PersonDataBase
        private val studentAge = (14..99).random()
        private val studentInfo = StudentData(
            name = "Ivan",
            surname = "Fedorov",
            gender = "М",
            birthdate = LocalDate.now().minusYears(studentAge.toLong()).toString(),
            email = "IvanFedorov@gmail.com",
            phone = "78789978",
            address = "Москва, Ленина 1",
            image = "https://kartinki.pics/uploads/posts/2022-02/1645199493_1-kartinkin-net-p-milie-kartinki-kotikov-2.jpg",
            score = "87"
        )
        const val NEW_STUDENT_NAME = "NEWAnastasiia"
    }
}