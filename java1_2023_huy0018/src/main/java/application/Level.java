package application;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import jakarta.persistence.Transient;

@NoArgsConstructor
@Getter
@Setter
@Entity

public class Level {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Transient
    private int currentLevel;
    @Transient
    private int linesCleared;
    private int linesPerLevel;
    private double initialDropSpeed;
    private double speedIncreaseFactor;
    @Transient
    private boolean levelIncreased = false;
    @ManyToOne
    private Player player;

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
    
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
    	this.player = player;
    }
}
