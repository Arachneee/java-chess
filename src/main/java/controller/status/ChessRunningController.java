package controller.status;

import domain.game.ChessGameStatus;
import domain.square.Square;
import dto.ChessGameDto;
import service.ChessGameService;
import service.PlayerService;
import view.InputView;
import view.OutputView;
import view.format.Command;

import java.sql.SQLException;
import java.util.List;

public class ChessRunningController implements ChessControllerStatus {

    private static final int SOURCE_INDEX = 0;
    private static final int TARGET_INDEX = 1;
    private static final int MOVE_PARAMETER_SIZE = 2;

    private final int gameNumber;
    private final PlayerService playerService;
    private final ChessGameService chessGameService;

    ChessRunningController(final int gameNumber, final PlayerService playerService, final ChessGameService chessGameService) {
        this.gameNumber = gameNumber;
        this.playerService = playerService;
        this.chessGameService = chessGameService;
    }

    @Override
    public Command readCommand() {
        final ChessGameDto chessGameDto = chessGameService.getGameDto(gameNumber);

        return InputView.readGameCommand(chessGameDto.currentTeam(), chessGameDto.currentPlayerName());
    }

    @Override
    public ChessControllerStatus execute(final Command command) throws SQLException {
        return switch (command.getCommandFormat()) {
            case MOVE -> move(command.getParameters());
            case STATUS -> printStatus();
            case END -> endGame();
            default -> throw new IllegalArgumentException("잘못된 커맨드입니다.");
        };
    }

    private ChessControllerStatus move(final List<String> parameters) throws SQLException {
        validateMoveParameters(parameters);

        final Square source = Square.from(parameters.get(SOURCE_INDEX));
        final Square target = Square.from(parameters.get(TARGET_INDEX));

        chessGameService.move(gameNumber, source, target);

        final ChessGameDto chessGame = chessGameService.getGameDto(gameNumber);
        OutputView.printChessBoard(chessGame.board());

        if (chessGame.status() == ChessGameStatus.END) {
            OutputView.printStatus(chessGame.chessResult());
            return new ChessStartingController(playerService, chessGameService);
        }
        return new ChessRunningController(gameNumber, playerService, chessGameService);
    }

    private void validateMoveParameters(final List<String> parameters) {
        if (parameters.size() != MOVE_PARAMETER_SIZE) {
            throw new IllegalArgumentException("잘못된 커맨드입니다.");
        }
    }

    private ChessControllerStatus printStatus() {
        final ChessGameDto chessGame = chessGameService.getGameDto(gameNumber);

        OutputView.printStatus(chessGame.chessResult());

        return new ChessRunningController(gameNumber, playerService, chessGameService);
    }

    private ChessControllerStatus endGame() throws SQLException {
        chessGameService.endGame(gameNumber);

        final ChessGameDto chessGame = chessGameService.getGameDto(gameNumber);

        OutputView.printStatus(chessGame.chessResult());

        return new ChessStartingController(playerService, chessGameService);
    }
}
