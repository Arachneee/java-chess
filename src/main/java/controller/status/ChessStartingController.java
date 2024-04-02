package controller.status;

import domain.Team;
import domain.player.PlayerName;
import domain.result.WinStatusSummary;
import dto.ChessGameDto;
import dto.PlayerRankingDto;
import service.ChessGameService;
import service.PlayerService;
import view.InputView;
import view.OutputView;
import view.format.Command;

import java.sql.SQLException;
import java.util.List;

public class ChessStartingController implements ChessControllerStatus {

    private final PlayerService playerService;
    private final ChessGameService chessGameService;

    public ChessStartingController(final PlayerService playerService, final ChessGameService chessGameService) {
        this.playerService = playerService;
        this.chessGameService = chessGameService;
    }

    @Override
    public Command readCommand() {
        return InputView.readCommand();
    }

    @Override
    public ChessControllerStatus execute(final Command command) throws SQLException {
        return switch (command.getCommandFormat()) {
            case START -> startNewGame();
            case CONTINUE -> continueGame();
            case RECORD -> printPlayerRecord();
            case RANKING -> printRanking();
            default -> throw new IllegalArgumentException("잘못된 커맨드입니다.");
        };
    }

    private ChessControllerStatus startNewGame() throws SQLException {
        final PlayerName blackPlayer = roadPlayer(Team.BLACK);
        final PlayerName whitePlayer = roadPlayer(Team.WHITE);

        final int gameId = chessGameService.createNewGame(blackPlayer, whitePlayer);

        final ChessGameDto chessGame = chessGameService.getGameDto(gameId);
        OutputView.printStartGame(chessGame);

        return new ChessRunningController(gameId, playerService, chessGameService);
    }

    private PlayerName roadPlayer(final Team team) {
        while (true) {
            try {
                final String name = InputView.readTeamPlayerName(team);
                return playerService.roadPlayer(name);
            } catch (final IllegalArgumentException e) {
                OutputView.printError(e.getMessage());
            }
        }
    }

    private ChessControllerStatus continueGame() {
        final List<Integer> runningGameNumbers = chessGameService.findRunningGameNumbers();
        final int gameNumber = readGameNumber(runningGameNumbers);

        final ChessGameDto chessGame = chessGameService.getGameDto(gameNumber);
        OutputView.printStartGame(chessGame);

        return new ChessRunningController(chessGame.gameNumber(), playerService, chessGameService);
    }

    private int readGameNumber(final List<Integer> runningGameNumbers) {
        while (true) {
            try {
                final int input = InputView.readContinueGame(runningGameNumbers);
                if (runningGameNumbers.contains(input)) {
                    return input;
                }
                throw new IllegalArgumentException("실행중이지 않는 게임번호입니다.");
            } catch (final IllegalArgumentException e) {
                OutputView.printError(e.getMessage());
            }
        }
    }

    private ChessControllerStatus printPlayerRecord() {
        final PlayerName player = readPlayer();
        final WinStatusSummary winStatusSummary = chessGameService.findGameRecord(player);
        OutputView.printGameRecord(winStatusSummary);

        return new ChessStartingController(playerService, chessGameService);
    }

    private PlayerName readPlayer() {
        while (true) {
            try {
                final String name = InputView.readPlayerName();
                return playerService.findPlayerName(name);
            } catch (final IllegalArgumentException e) {
                OutputView.printError(e.getMessage());
            }
        }
    }

    private ChessControllerStatus printRanking() {
        final List<PlayerName> playerNames = playerService.findAllPlayerNames();
        final List<PlayerRankingDto> rankings = chessGameService.getRanking(playerNames);

        OutputView.printRanking(rankings);

        return new ChessStartingController(playerService, chessGameService);
    }
}
