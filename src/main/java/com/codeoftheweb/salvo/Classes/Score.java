package com.codeoftheweb.salvo.Classes;

import com.codeoftheweb.salvo.Classes.Game;
import com.codeoftheweb.salvo.Classes.Player;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private double score;
    private Date finishDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player players;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="games_id")
    private Game game;

    public Score() {}

    public Score(Player players, double score, Date finishDate,  Game game) {
        this.players = players;
        this.score = score;
        this.finishDate = finishDate;
        this.game = game;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Player getPlayers() {return players;}
    public void setPlayers(Player players) { this.players = players; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public Date getFinishDate() { return finishDate; }
    public void setFinishDate(Date finishDate) { this.finishDate = finishDate;}

    public Game getGame() { return game; }
    public void setGame(Game game) { this.game = game;}

    public Long getPlayerId(){
        return players.getId();
    }

    public Long getGameId() {
        return game.getId();
    }
}

