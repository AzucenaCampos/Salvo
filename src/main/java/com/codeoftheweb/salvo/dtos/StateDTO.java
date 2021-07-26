package com.codeoftheweb.salvo.dtos;

import com.codeoftheweb.salvo.Classes.GamePlayer;
import com.codeoftheweb.salvo.Classes.Salvo;
import com.codeoftheweb.salvo.Classes.Score;
import com.codeoftheweb.salvo.Repositories.ScoreRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class StateDTO {
    private String state;

    public StateDTO() {
    }

    public StateDTO(String state) {
        this.state = state;
    }

    public String StateGame(GamePlayer gamePlayer){
        if(gamePlayer.getShips().size() < 5){
            return "PLACESHIPS";
        }
        if(gamePlayer.getOpponentGameP().isEmpty() || gamePlayer.getOpponentGameP().get().getShips().size()<5){
            return "WAITINGFOROPP";
        }
        if(gamePlayer.getShips().size() ==5 || gamePlayer.getOpponentGameP().get().getShips().size()==5){
            return "PLAY";
        }
        if(!(gamePlayer.getSalvoes().size() > gamePlayer.getOpponentGameP().get().getSalvoes().size())){
            return "WAIT";
        }
        if(getFullHits(gamePlayer)==17){
            if(getFullHits(gamePlayer.getOpponentGameP().get())==17){
                return "TIE";
            }
        }
        if(getFullHits(gamePlayer)==17){
            return "WON";
        }
        if(getFullHits(gamePlayer.getOpponentGameP().get())==17){
            return "LOST";
        }
        return "PLAY";
    }

    public List<String> getHitsLocations(Salvo salvo) {
        GamePlayer opponent = salvo.getGamePlayer().getOpponentGameP().get();
        List<String> loc = opponent.getShips().stream().flatMap(ship ->ship.getShipLocations().stream()).collect(Collectors.toList());
        List<String>  hits = salvo.getSalvoLocations();
        return hits.stream().filter(l -> loc.contains(l)).collect(Collectors.toList());
    }

    public int getFullHits(GamePlayer gamePlayer){
        return gamePlayer.getSalvoes().stream().flatMap(salvo-> getHitsLocations(salvo).stream()).collect(Collectors.toList()).size();
    }
}






