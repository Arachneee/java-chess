package service;

import config.AppConfig;
import domain.Team;
import domain.WinStatus;
import domain.chessboard.ChessBoard;
import domain.game.ChessGame;
import domain.game.ChessGameStatus;
import domain.player.Player;
import domain.player.PlayerName;
import domain.result.WinStatusSummary;
import domain.square.Square;
import dto.ChessGameDto;
import dto.PlayerRankingDto;
import repository.ChessGameRepository;
import repository.ChessResultDao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public class ChessGameService {

    private final ChessGameRepository chessGameRepository;
    private final ChessResultDao chessResultDao;

    public ChessGameService(final ChessGameRepository chessGameRepository, final ChessResultDao chessResultDao) {
        this.chessGameRepository = chessGameRepository;
        this.chessResultDao = chessResultDao;
    }

    public int createNewGame(final PlayerName blackPlayer, final PlayerName whitePlayer) throws SQLException {
        final Connection connection = AppConfig.connection();
        try {
            connection.setAutoCommit(false);

            final int gameNumber = chessGameRepository.findMaxNumber() + 1;
            final ChessGame chessGame = ChessGame.ChessGameBuilder.builder()
                    .number(gameNumber)
                    .blackPlayer(new Player(blackPlayer))
                    .whitePlayer(new Player(whitePlayer))
                    .chessBoard(ChessBoard.create())
                    .status(ChessGameStatus.RUNNING)
                    .currentTeam(Team.WHITE)
                    .build();

            chessGameRepository.create(chessGame);

            connection.commit();
            return gameNumber;
        } catch (final Exception e) {
            connection.rollback();
            throw new RuntimeException("서버 오류입니다.");
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public List<Integer> findRunningGameNumbers() {
        final List<Integer> games = chessGameRepository.findNumbersByStatus(ChessGameStatus.RUNNING);
        if (games.isEmpty()) {
            throw new IllegalArgumentException("진행중인 게임이 없습니다.");
        }
        return games;
    }

    public void move(final int gameNumber, final Square source, final Square target) throws SQLException {
        final Connection connection = AppConfig.connection();
        try {
            connection.setAutoCommit(false);

            final ChessGame chessGame = chessGameRepository.findByNumberAndStatus(gameNumber, ChessGameStatus.RUNNING)
                    .orElseThrow(() -> new IllegalArgumentException("진행 중인 게임을 찾을 수 없습니다."));

            chessGame.move(source, target);

            chessGameRepository.update(chessGame);

            if (chessGame.isEnd()) {
                chessResultDao.create(chessGame.calculateResult(), gameNumber);
            }

            connection.commit();
        } catch (final Exception e) {
            connection.rollback();
            throw new IllegalArgumentException(e.getMessage());
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void endGame(final int gameNumber) throws SQLException {
        final Connection connection = AppConfig.connection();
        try {
            connection.setAutoCommit(false);

            final ChessGame chessGame = chessGameRepository.findByNumberAndStatus(gameNumber, ChessGameStatus.RUNNING)
                    .orElseThrow(() -> new IllegalArgumentException("진행 중인 게임을 찾을 수 없습니다."));

            chessGame.end();
            chessResultDao.create(chessGame.calculateResult(), gameNumber);

            chessGameRepository.updateStatus(chessGame);

            connection.commit();
        } catch (final Exception e) {
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public WinStatusSummary findGameRecord(final PlayerName player) {
        final List<WinStatus> blackWinStatuses = chessResultDao.findBlackWinStatus(player);
        final List<WinStatus> whiteWinStatuses = chessResultDao.findWhiteWinStatus(player);

        return WinStatusSummary.of(blackWinStatuses, whiteWinStatuses);
    }

    public ChessGameDto getGameDto(final int gameNumber) {
        final ChessGame chessGame = chessGameRepository.findByNumber(gameNumber)
                .orElseThrow(() -> new IllegalArgumentException("게임을 찾을 수 없습니다."));

        return ChessGameDto.from(chessGame);
    }

    public List<PlayerRankingDto> getRanking(final List<PlayerName> playerNames) {
        final Map<PlayerName, WinStatusSummary> playersWinStatus = playerNames.stream()
                .collect(toMap(Function.identity(), this::findGameRecord));

        return playersWinStatus.entrySet().stream()
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                .map(entry -> new PlayerRankingDto(entry.getKey().getName(), entry.getValue()))
                .toList();
    }
}
