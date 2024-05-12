package application;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Score{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private int num;
    private int score;

    private String name;
    
    @ManyToOne
    private Player player;
    
    public Score(int num, int score, String name) {
    	this.num = num;
        this.score = score;
        this.name = name;
    }
    

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public int getNum() {
        return num;
    }
    
    public void setNum(int num) {
        this.num = num;
    }
    
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
    	this.player = player;
    }

    @Override
    public String toString() {
        return "PLAYER: "+ name + " - " + "SCORE: " + score;
    }
}
