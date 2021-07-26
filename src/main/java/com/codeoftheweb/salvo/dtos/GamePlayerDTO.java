package com.codeoftheweb.salvo.dtos;

import com.codeoftheweb.salvo.Classes.GamePlayer;
import com.codeoftheweb.salvo.Classes.Player;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class GamePlayerDTO {

    private Long id;
    private PlayerDTO player;

    public GamePlayerDTO() { }

    public GamePlayerDTO(GamePlayer gamePlayer){
        this.id = gamePlayer.getId();
        this.player = new PlayerDTO(gamePlayer.getPlayer());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PlayerDTO getPlayer() { return player; }
    public void setPlayer(PlayerDTO player) {
 this.player = player;
    }
}
