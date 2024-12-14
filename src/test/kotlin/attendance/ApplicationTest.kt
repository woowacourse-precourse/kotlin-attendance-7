package attendance

import camp.nextstep.edu.missionutils.test.Assertions.assertNowTest
import camp.nextstep.edu.missionutils.test.NsTest
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate

class ApplicationTest : NsTest() {
    @Test
    fun `잘못된 형식 예외 테스트`() {
        assertNowTest(
            {
                val exception = assertThrows<IllegalArgumentException> { run("1", "짱수", "33:71") }
                assertThat(exception.message).contains("[ERROR] 잘못된 형식을 입력하였습니다.")
            },
            LocalDate.of(2024, 12, 13).atStartOfDay()
        )
    }

    @Test
    fun `등록되지 않은 닉네임 예외 테스트`() {
        assertNowTest(
            {
                val exception = assertThrows<IllegalArgumentException> { run("1", "빈봉") }
                assertThat(exception.message).contains("[ERROR] 등록되지 않은 닉네임입니다.")
            },
            LocalDate.of(2024, 12, 13).atStartOfDay()
        )
    }

    @Test
    fun `주말 또는 공휴일 예외 테스트`() {
        assertNowTest(
            {
                val exception = assertThrows<IllegalArgumentException> { run("1") }
                assertThat(exception.message).contains("[ERROR] 12월 14일 토요일은 등교일이 아닙니다.")
            },
            LocalDate.of(2024, 12, 14).atStartOfDay()
        )
    }

    @Test
    fun `출석 확인 기능 테스트`() {
        assertNowTest(
            {
                runException("1", "짱수", "08:00")
                assertThat(output()).contains("12월 13일 금요일 08:00 (출석)")
            },
            LocalDate.of(2024, 12, 13).atStartOfDay()
        )
    }

    @Test
    fun `출석 수정 및 크루별 출석 기록 확인 기능 테스트`() {
        assertNowTest(
            {
                runException("2", "짱수", "12", "10:31", "3", "짱수")
                assertThat(output()).contains(
                    "12월 12일 목요일 10:00 (출석) -> 10:31 (결석) 수정 완료!",
                    "12월 02일 월요일 13:00 (출석)",
                    "12월 03일 화요일 10:00 (출석)",
                    "12월 04일 수요일 10:00 (출석)",
                    "12월 05일 목요일 10:00 (출석)",
                    "12월 06일 금요일 10:00 (출석)",
                    "12월 09일 월요일 13:00 (출석)",
                    "12월 10일 화요일 10:00 (출석)",
                    "12월 11일 수요일 --:-- (결석)",
                    "12월 12일 목요일 10:31 (결석)",
                    "출석: 7회",
                    "지각: 0회",
                    "결석: 2회",
                    "경고 대상자",
                )
            },
            LocalDate.of(2024, 12, 13).atStartOfDay()
        )
    }

    override fun runMain() {
        main()
    }
}
