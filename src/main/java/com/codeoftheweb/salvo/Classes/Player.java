package com.codeoftheweb.salvo.Classes;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    //private String userName;
    private String email;
    private String password;

    public Player(){}
    public Player(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() {return password; }
    public void setPassword(String password) { this.password = password; }

    /*public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }*/

    public Set<GamePlayer> getGamePlayers() { return gamePlayers; }
    public void addGamePlayer(GamePlayer gamePlayer) { }

    public Long getId() {
        return id;
    }

    public Optional<Score> getScore(Game game){
        return scores.stream().filter(score -> score.getGameId().equals(game.getId())).findFirst();
    }

    @OneToMany(mappedBy="players", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers = new HashSet<>();

    @OneToMany(mappedBy="players", fetch=FetchType.EAGER)
    Set<Score> scores = new HashSet<>();
}

