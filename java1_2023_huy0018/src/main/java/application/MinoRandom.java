package application;

import java.util.Random;

public class MinoRandom {
    private final Random random;
    private final Mino.MinoType[] nextMinos = new Mino.MinoType[2];

    public MinoRandom() {
        this.random = new Random();
        nextMinos[0] = getRandomMinoType();
        nextMinos[1] = getRandomMinoType();
    }

    private Mino.MinoType getRandomMinoType() {
        Mino.MinoType[] minoTypes = Mino.MinoType.values();
        return minoTypes[random.nextInt(minoTypes.length)];
    }

    public Mino createRandomMino(double fallSpeed) {
        nextMinos[0] = nextMinos[1];
        Mino newMino = new Mino(nextMinos[0], 50, fallSpeed);
        nextMinos[1] = getRandomMinoType();
        return newMino;
    }

    public Mino.MinoType getNextMinoType() {
        return nextMinos[1];
    }
}
