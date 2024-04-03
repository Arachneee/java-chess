package domain.result;

import domain.WinStatus;

import java.util.List;
import java.util.Objects;

public class WinStatusSummary implements Comparable<WinStatusSummary> {

    private final int winCount;
    private final int loseCount;
    private final int drawCount;

    public WinStatusSummary(final int winCount, final int loseCount, final int drawCount) {
        this.winCount = winCount;
        this.loseCount = loseCount;
        this.drawCount = drawCount;
    }

    public static WinStatusSummary of(final List<WinStatus> blackStatuses, final List<WinStatus> whiteStatuses) {
        final int countWin = countStatus(blackStatuses, WinStatus.BLACK_WIN)
                + countStatus(whiteStatuses, WinStatus.WHITE_WIN);
        final int countLose = countStatus(blackStatuses, WinStatus.WHITE_WIN)
                + countStatus(whiteStatuses, WinStatus.BLACK_WIN);
        final int countDraw = countStatus(blackStatuses, WinStatus.DRAW)
                + countStatus(whiteStatuses, WinStatus.DRAW);

        return new WinStatusSummary(countWin, countLose, countDraw);
    }

    private static int countStatus(final List<WinStatus> winStatuses, final WinStatus winStatus) {
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
    public int compareTo(final WinStatusSummary o) {
        if (this.winCount == o.winCount) {
            if (this.loseCount == o.loseCount) {
                return Integer.compare(this.drawCount, o.drawCount);
            }
            return Integer.compare(o.loseCount, this.loseCount);
        }
        if (this.loseCount == o.loseCount) {
            return Integer.compare(this.drawCount, o.drawCount);
        }
        return Integer.compare(this.winCount, o.winCount);
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
