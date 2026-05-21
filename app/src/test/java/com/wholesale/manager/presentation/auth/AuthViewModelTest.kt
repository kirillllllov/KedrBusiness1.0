package com.wholesale.manager.presentation.auth

import com.wholesale.manager.data.local.AppDatabase
import com.wholesale.manager.di.UserUseCases
import com.wholesale.manager.domain.model.User
import com.wholesale.manager.domain.usecase.user.CountDirectorsUseCase
import com.wholesale.manager.domain.usecase.user.CreateUserUseCase
import com.wholesale.manager.domain.usecase.user.DeleteUserUseCase
import com.wholesale.manager.domain.usecase.user.GetAllUsersUseCase
import com.wholesale.manager.domain.usecase.user.GetUserByUsernameUseCase
import com.wholesale.manager.domain.usecase.user.UpdateUserUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {
    private val getAllUsersUseCase = mockk<GetAllUsersUseCase>()
    private val getUserByUsernameUseCase = mockk<GetUserByUsernameUseCase>()
    private val createUserUseCase = mockk<CreateUserUseCase>()
    private val updateUserUseCase = mockk<UpdateUserUseCase>()
    private val deleteUserUseCase = mockk<DeleteUserUseCase>()
    private val countDirectorsUseCase = mockk<CountDirectorsUseCase>()

    private val useCases = UserUseCases(
        getAll = getAllUsersUseCase,
        getByUsername = getUserByUsernameUseCase,
        create = createUserUseCase,
        update = updateUserUseCase,
        delete = deleteUserUseCase,
        countDirectors = countDirectorsUseCase
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun login_success_updatesCurrentUser() = runTest {
        val user = User(
            id = UUID.randomUUID().toString(),
            username = "director",
            passwordHash = AppDatabase.hashPassword("director123"),
            role = User.ROLE_DIRECTOR,
            displayName = "Директор"
        )
        coEvery { getUserByUsernameUseCase.invoke("director") } returns user

        val viewModel = AuthViewModel(useCases)
        viewModel.login("director", "director123")
        advanceUntilIdle()

        assertTrue(viewModel.isLoggedIn)
        assertEquals(User.ROLE_DIRECTOR, viewModel.currentRole)
        assertEquals(user, viewModel.state.value.currentUser)
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun login_failure_setsErrorMessage() = runTest {
        coEvery { getUserByUsernameUseCase.invoke("wrong") } returns null

        val viewModel = AuthViewModel(useCases)
        viewModel.login("wrong", "bad")
        advanceUntilIdle()

        assertFalse(viewModel.isLoggedIn)
        assertEquals("Неверный логин или пароль", viewModel.state.value.errorMessage)
        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun logout_clearsCurrentUser() = runTest {
        val user = User(
            id = UUID.randomUUID().toString(),
            username = "director",
            passwordHash = AppDatabase.hashPassword("director123"),
            role = User.ROLE_DIRECTOR,
            displayName = "Директор"
        )
        coEvery { getUserByUsernameUseCase.invoke("director") } returns user

        val viewModel = AuthViewModel(useCases)
        viewModel.login("director", "director123")
        advanceUntilIdle()
        viewModel.logout()

        assertFalse(viewModel.isLoggedIn)
        assertEquals(null, viewModel.state.value.currentUser)
    }
}
