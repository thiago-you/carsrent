package br.pucpr.carsrent.users

import br.pucpr.carsrent.bookings.BookingRepository
import br.pucpr.carsrent.exceptions.BadRequestException
import br.pucpr.carsrent.roles.RoleRepository
import br.pucpr.carsrent.security.Jwt
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.checkUnnecessaryStub
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserServiceTest {
    private val jwt = mockk<Jwt>()
    private val userRepository = mockk<UserRepository>()
    private val roleRepository = mockk<RoleRepository>()
    private val bookingRepository = mockk<BookingRepository>()

    private val service = UserService(userRepository, roleRepository, bookingRepository, jwt)

    @BeforeEach
    fun setup() = clearAllMocks()

    @AfterEach
    fun cleanUp() = checkUnnecessaryStub()

    @Test
    fun `insert throws BadRequestException if an user with same e-mail is found`() {
        val user = _user(id = null)

        every { userRepository.findByEmail(user.email) } returns _user(id = 1)

        assertThrows<BadRequestException> {
            service.insert(user)
        } shouldHaveMessage  "User with e-mail ${user.email} already exists!"
    }

    @Test
    fun `insert must return the saved user if it is inserted`() {
        val user = _user(id = null)
        every { userRepository.findByEmail(user.email) } returns null

        val saved = _user(id = 1)
        every { userRepository.save(user) } returns saved

        service.insert(user) shouldBe saved
    }
}