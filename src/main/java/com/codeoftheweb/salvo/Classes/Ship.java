package com.codeoftheweb.salvo.Classes;

import com.codeoftheweb.salvo.Classes.GamePlayer;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.List;

@Entity
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String type;
    private int damage;

    @ElementCollection
    @Column(name="shipLocation")
    private List<String> shipLocations;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayers;

    public Ship(){}
    public Ship(String type, List<String> locations, GamePlayer gp) {
        this.type = type;
        this.shipLocations = locations;
        this.gamePlayers = gp;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public List<String> getShipLocations() { return shipLocations; }
    public void setShipLocations(List<String> locations) {this.shipLocations = locations; }

    public GamePlayer getGamePlayers() { return gamePlayers; }
    public void setGamePlayers(GamePlayer gamePlayers) { this.gamePlayers = gamePlayers; }

    public int getDamage() {
        return damage;
    }
    public void setDamage(int damage) {
        this.damage = damage;
    }
}
