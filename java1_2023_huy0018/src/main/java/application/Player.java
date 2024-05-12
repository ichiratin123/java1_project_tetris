package application;

import java.util.List;

import jakarta.persistence.*;

@Entity
public class Player {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false, unique = true, length = 50)
	private String username;
	
	@OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Score> scores;
	
	@OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Level> level;
	
	public Player() {
    }
	
	public Player(String username) {
        this.username = username;
    }
	public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
