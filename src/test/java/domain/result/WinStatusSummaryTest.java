package domain.result;

import domain.WinStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class WinStatusSummaryTest {

    @DisplayName("사용자의 전적을 집계한다.")
    @Test
    void summary() {
        // given
        final List<WinStatus> blackWinStatus = List.of(WinStatus.BLACK_WIN, WinStatus.WHITE_WIN,
                WinStatus.DRAW, WinStatus.BLACK_WIN);
        final List<WinStatus> whiteWinStatus = List.of(WinStatus.BLACK_WIN, WinStatus.WHITE_WIN,
                WinStatus.DRAW, WinStatus.BLACK_WIN, WinStatus.WHITE_WIN);

        // when
        final WinStatusSummary winStatusSummary = WinStatusSummary.of(blackWinStatus, whiteWinStatus);

        // then
        assertThat(winStatusSummary).isEqualTo(new WinStatusSummary(4, 3, 2));
    }
}
