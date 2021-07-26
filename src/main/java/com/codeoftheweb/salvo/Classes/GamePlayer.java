package com.codeoftheweb.salvo.Classes;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long Id;
    private Date date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player players;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="games_id")
    private Game game;

    @OneToMany(mappedBy="gamePlayers", fetch=FetchType.EAGER)
    Set<Ship> ships = new HashSet<>();

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    Set<Salvo> salvoes = new HashSet<>();

    public GamePlayer(){};
    public GamePlayer(Date date, Player player, Game game) {
        this.date = date;
        this.players = player;
        this.game = game;
    }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public Player getPlayer() { return players; }
    public void setPlayer(Player player) { this.players = player; }

    public Game getGame() { return game; }
    public void setGame(Game game) { this.game = game; }

    public Set<Ship> getShips() { return ships; }
    public void setShips(Set<Ship> ships) { this.ships = ships;}

    public Long getId() { return Id; }

    public Set<Salvo> getSalvoes() { return salvoes; }
    public void setSalvoes(Set<Salvo> salvoes) {
        this.salvoes = salvoes;
    }

    public Optional<Score> getScore(){
        return players.getScore(game);
    }

    public Optional<GamePlayer> getOpponentGameP(){
        return this.getGame().getGamePlayers()
                .stream()
                .filter(gp -> gp.getId() != this.getId())
                .findFirst();
    }
}
