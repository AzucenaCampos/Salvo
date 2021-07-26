package com.codeoftheweb.salvo.Classes;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Salvo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private int turn;

    @ElementCollection
    @Column(name="Location")
    private List<String> salvoLocations = new ArrayList<>();

    public Salvo(){
    }

    public Salvo(int turn, List<String> salvoLocations, GamePlayer gamePlayer) {
        this.turn = turn;
        this.salvoLocations = salvoLocations;
        this.gamePlayer = gamePlayer;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="GamePlayer_id")
    private GamePlayer gamePlayer;

    public Long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public int getTurn() { return turn; }
    public void setTurn(int turn) { this.turn = turn; }

    public List<String> getSalvoLocations() { return salvoLocations; }
    public void setSalvoLocations(List<String> salvoLocations) { this.salvoLocations = salvoLocations; }

    public GamePlayer getGamePlayer() { return gamePlayer; }
    public void setGamePlayer(GamePlayer gamePlayer) { this.gamePlayer = gamePlayer; }

}
