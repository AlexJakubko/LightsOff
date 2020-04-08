package sk.tuke.gamestudio.game.core;

public class Dot {
    private DotState state = DotState.DONTSHINE;

    public DotState getState() {
        return state;
    }
    void setState(DotState state) {
        this.state  = state;
    }
}
