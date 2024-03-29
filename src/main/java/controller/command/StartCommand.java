package controller.command;

import controller.status.ChessProgramStatus;
import controller.status.RunningStatus;
import domain.Team;
import domain.chessboard.ChessBoard;
import domain.game.ChessGame;
import domain.player.Player;
import service.ChessGameService;
import service.PlayerService;
import view.InputView;
import view.OutputView;

import java.sql.SQLException;
import java.util.List;

public class StartCommand implements Command {

    private final PlayerService playerService;
    private final ChessGameService chessGameService;

    public StartCommand(final PlayerService playerService, final ChessGameService chessGameService) {
        this.playerService = playerService;
        this.chessGameService = chessGameService;
    }

    @Override
    public ChessProgramStatus executeStart() throws SQLException {
        final Player blackPlayer = roadPlayer(Team.BLACK);
        final Player whitePlayer = roadPlayer(Team.WHITE);
        final ChessGame chessGame = chessGameService.createNewGame(blackPlayer, whitePlayer);

        OutputView.printGameOption(chessGame.getId(), blackPlayer.getName(), whitePlayer.getName());

        final ChessBoard chessBoard = chessGame.getChessBoard();
        OutputView.printChessBoard(chessBoard.getPieceSquares());

        return new RunningStatus(chessGame);
    }

    @Override
    public ChessProgramStatus executePlay(final List<String> command, final int gameId) {
        throw new UnsupportedOperationException("사용할 수 없는 기능입니다.");
    }

    private Player roadPlayer(final Team team) {
        while (true) {
            try {
                final String name = InputView.readTeamPlayerName(team);
                return playerService.roadPlayer(name);
            } catch (final IllegalArgumentException e) {
                OutputView.printError(e.getMessage());
            }
        }
    }
}
