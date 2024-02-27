package application;

public class Level {
    private int currentLevel;
    private int linesCleared;
    private int linesPerLevel;
    private double initialDropSpeed;
    private double speedIncreaseFactor;
    private boolean levelIncreased = false;


    public Level(int linesPerLevel, double initialDropSpeed, double speedIncreaseFactor) {
        this.currentLevel = 1;
        this.linesCleared = 0;
        this.linesPerLevel = linesPerLevel;
        this.initialDropSpeed = initialDropSpeed;
        this.speedIncreaseFactor = speedIncreaseFactor;
    }

    public void lineCleared() {
        linesCleared++;
        int oldLevel = currentLevel;
        levelIncreased = currentLevel > oldLevel;
        if (linesCleared % linesPerLevel == 0) {
            levelUp();
        }

    }

    public boolean checkLevelIncrease() {
        boolean increased = levelIncreased;
        levelIncreased = false;
        return increased;
    }

    private void levelUp() {
        currentLevel++;
    }

    public double getCurrentDropSpeed() {
        return initialDropSpeed * Math.pow(speedIncreaseFactor, currentLevel - 1);
    }

    public int getCurrentLevel() {
        return currentLevel;
    }
}
