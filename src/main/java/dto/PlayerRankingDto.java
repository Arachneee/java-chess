package dto;

import domain.result.WinStatusSummary;

public record PlayerRankingDto(
        String playerName,
        WinStatusSummary winStatusSummary
) {

}
