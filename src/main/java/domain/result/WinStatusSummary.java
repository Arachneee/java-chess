package domain.result;

import domain.WinStatus;

import java.util.List;
import java.util.Objects;

public class WinStatusSummary {

    private final int winCount;
    private final int loseCount;
    private final int drawCount;

    public WinStatusSummary(final int winCount, final int loseCount, final int drawCount) {
        this.winCount = winCount;
        this.loseCount = loseCount;
        this.drawCount = drawCount;
    }

    public static WinStatusSummary of(final List<WinStatus> blackWinStatuses, final List<WinStatus> whiteWinStatuses) {
        final int countWin = countWinStatus(blackWinStatuses, WinStatus.BLACK_WIN)
                + countWinStatus(whiteWinStatuses, WinStatus.WHITE_WIN);
        final int countLose = countWinStatus(blackWinStatuses, WinStatus.WHITE_WIN)
                + countWinStatus(whiteWinStatuses, WinStatus.BLACK_WIN);
        final int countDraw = countWinStatus(blackWinStatuses, WinStatus.DRAW)
                + countWinStatus(blackWinStatuses, WinStatus.DRAW);

        return new WinStatusSummary(countWin, countLose, countDraw);
    }

    private static int countWinStatus(final List<WinStatus> winStatuses, final WinStatus winStatus) {
        return (int) winStatuses.stream()
                .filter(status -> status == winStatus)
                .count();
    }

    public int getWinCount() {
        return winCount;
    }

    public int getLoseCount() {
        return loseCount;
    }

    public int getDrawCount() {
        return drawCount;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final WinStatusSummary that = (WinStatusSummary) o;
        return winCount == that.winCount && loseCount == that.loseCount && drawCount == that.drawCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(winCount, loseCount, drawCount);
    }
}
