package com.ics342.labs

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.util.UUID
import kotlin.random.Random

internal class NumbersRepositoryTest {

    @Test
    fun `If database does not have a number fetch it from the Api`() {
        // Setup
        val database = mockk<Database>()
        val api = mockk<Api>()
        val number = Number(UUID.randomUUID().toString(), Random.nextInt())
        val id = number.id

        every { database.getNumber(id) } returns null
        every { api.getNumber(id) } returns number

        // Act
        val repository = NumbersRepository(database, api)
        val result = repository.getNumber(id)

        // Assert
        assertNotNull(result)
        assertEquals(result, number)

        verify { database.getNumber(id) }
        verify { api.getNumber(id) }
    }

    @Test
    fun ifDatabaseIsEmptyShouldFetchNumbersFromApi() {
        val database = mockk<Database>()
        val api = mockk<Api>()

        val numbersFromApi = listOf(
            Number("1", 45),
            Number("2", 228),
            Number("3", 12194)
        )

        every { database.getAllNumbers() } returns emptyList()
        every { api.getNumbers() } returns numbersFromApi
        every { database.storeNumbers(numbersFromApi) } returns Unit

        val repository = NumbersRepository(database, api)
        val result = repository.fetchNumbers()

        assertEquals(numbersFromApi, result)

        verify(exactly = 1) { database.getAllNumbers() }
        verify(exactly = 1) { api.getNumbers() }
        verify(exactly = 1) { database.storeNumbers(numbersFromApi) }
    }
}
