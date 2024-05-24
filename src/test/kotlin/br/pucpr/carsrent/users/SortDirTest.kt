package br.pucpr.carsrent.users

import br.pucpr.carsrent.exceptions.BadRequestException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class SortDirTest {
    @Test
    fun `findOrThrow throws BadRequestException for invalid values`() {
        val error = assertThrows<BadRequestException> {
            SortDir.findOrThrow("INVALID")
        }

        assert(error.message!!.contains("invalid", ignoreCase = true))
    }

    @ParameterizedTest
    @ValueSource(strings = ["asc", "ASC", "aSc"])
    fun `findOrThrow returns SortDir for valid ASC values ignoring case`(dir: String) {
        assert(SortDir.findOrThrow(dir) == SortDir.ASC)
    }

    @ParameterizedTest
    @ValueSource(strings = ["desc", "DESC", "dESC"])
    fun `findOrThrow returns SortDir for valid DESC values ignoring case`(dir: String) {
        assert(SortDir.findOrThrow(dir) == SortDir.DESC)
    }
}