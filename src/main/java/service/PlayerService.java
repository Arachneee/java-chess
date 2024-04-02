package service;

import domain.player.PlayerName;
import repository.PlayerDao;

import java.util.List;

public class PlayerService {

    private final PlayerDao playerDao;

    public PlayerService(final PlayerDao playerDao) {
        this.playerDao = playerDao;
    }

    public PlayerName roadPlayer(final String name) {
        final PlayerName playerName = new PlayerName(name);

        if (!playerDao.exist(playerName)) {
            playerDao.add(playerName);
        }
        return playerName;
    }

    public PlayerName findPlayerName(final String name) {
        return playerDao.findPlayerByName(name)
                .orElseThrow(() -> new IllegalArgumentException("플레이어를 찾을 수 없습니댜."))
                .getPlayerName();
    }

    public List<PlayerName> findAllPlayerNames() {
        return playerDao.findAllNames();
    }
}
