package domain.player;

import java.util.Objects;

public class Player {

    private final PlayerName name;

    public Player(final PlayerName name) {
        this.name = name;
    }

    public String getName() {
        return name.getName();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
