package com.codeoftheweb.salvo.dtos;

import com.codeoftheweb.salvo.Classes.Salvo;

import java.util.List;

public class SalvoDTO {
    private int turn;
    private List<String> locations;
    private Long player;

    public SalvoDTO() {
    }

    public SalvoDTO (Salvo salvo){
        this.turn = salvo.getTurn();
        this.locations = salvo.getSalvoLocations();
        this.player= salvo.getGamePlayer().getPlayer().getId();
    }

    public int getTurn() {
        return turn;
    }
    public void setTurn(int turn) {
        this.turn = turn;
    }

    public List<String> getLocations() {
        return locations;
    }
    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public Long getPlayer() {
        return player;
    }
    public void setPlayer(Long player) {
        this.player = player;
    }
}
