package br.pucpr.carsrent.users

import br.pucpr.carsrent.exceptions.BadRequestException
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContainIgnoringCase
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class SortDirectoryTest {
    @Test
    fun `findOrThrow throws BadRequestException for invalid values`() {
        val error = assertThrows<BadRequestException> {
            SortDirectory.findOrThrow("INVALID")
        }

        error.message shouldContainIgnoringCase "invalid"
    }

    @ParameterizedTest
    @ValueSource(strings = ["asc", "ASC", "aSc"])
    fun `findOrThrow returns SortDir for valid ASC values ignoring case`(dir: String) {
        SortDirectory.findOrThrow(dir) shouldBe SortDirectory.ASC
    }

    @ParameterizedTest
    @ValueSource(strings = ["desc", "DESC", "dESC"])
    fun `findOrThrow returns SortDir for valid DESC values ignoring case`(dir: String) {
        SortDirectory.findOrThrow(dir) shouldBe SortDirectory.DESC
    }
}