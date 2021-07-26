package com.codeoftheweb.salvo.dtos;

import com.codeoftheweb.salvo.Classes.Game;
import com.codeoftheweb.salvo.Classes.GamePlayer;
import com.codeoftheweb.salvo.Classes.Salvo;
import com.codeoftheweb.salvo.Classes.Score;
import com.codeoftheweb.salvo.Repositories.ScoreRepository;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MakeGameAuxDTO {
    private Long id;
    private Date created;
    private String gameState;
    private Set<GamePlayerDTO> gamePlayers = new HashSet<>();
    private Set<ShipDTO> ships = new HashSet<>();
    private Set<SalvoDTO> salvoes = new HashSet<>();
    private HitsDTO hits;

    public MakeGameAuxDTO() { }

    public MakeGameAuxDTO(GamePlayer gamePlayer){
        this.id= gamePlayer.getGame().getId();
        this.created =gamePlayer.getGame().getDate();
        this.gameState = stateGame(gamePlayer);
        this.gamePlayers= gamePlayer.getGame().getGamePlayers().stream().map(GamePlayerDTO::new).collect(Collectors.toSet());

        this.ships = gamePlayer.getShips().stream().map(ShipDTO::new).collect(Collectors.toSet());
        if(gamePlayer.getOpponentGameP().isPresent()){
            this.salvoes = gamePlayer.getGame().getGamePlayers()
                    .stream()
                    .map(gp -> gp.getSalvoes())
                    .flatMap(salvos -> salvos.stream())
                    .map(salvo -> new SalvoDTO(salvo))
                    .collect(Collectors.toSet());
        }else{
            this.salvoes= new HashSet<>();
        }

        this.hits = new HitsDTO(gamePlayer);
    }

    public String stateGame(GamePlayer gamePlayer){
        if(gamePlayer.getShips().size() < 5){
            return "PLACESHIPS";
        }
        if(gamePlayer.getOpponentGameP().isEmpty() || gamePlayer.getOpponentGameP().get().getShips().size()<5){
            return "WAITINGFOROPP";
        }
        if(getFullHits(gamePlayer)==17 && getFullHits(gamePlayer.getOpponentGameP().get())==17 && gamePlayer.getSalvoes().size()==gamePlayer.getOpponentGameP().get().getSalvoes().size()){
                return "TIE";
        }
        if(getFullHits(gamePlayer)==17 && getFullHits(gamePlayer.getOpponentGameP().get())<17 && gamePlayer.getSalvoes().size()==gamePlayer.getOpponentGameP().get().getSalvoes().size()){
            return "WON";
        }
        if(getFullHits(gamePlayer.getOpponentGameP().get())==17 && getFullHits(gamePlayer)<17 && gamePlayer.getSalvoes().size()==gamePlayer.getOpponentGameP().get().getSalvoes().size()){
            return "LOST";
        }
        if(gamePlayer.getSalvoes().size()<=gamePlayer.getOpponentGameP().get().getSalvoes().size()){
            return "PLAY";
        }
        /*if(gamePlayer.getSalvoes().size()==0){
            return "PLAY";
        }*/
        if(gamePlayer.getSalvoes().size() > gamePlayer.getOpponentGameP().get().getSalvoes().size()){
            return "WAIT";
        }
        return "UNDEFINED";
    }

    public List<String> getHitsLocations(Salvo salvo) {
        GamePlayer opponent = salvo.getGamePlayer().getOpponentGameP().get();
        List<String> loc = opponent.getShips().stream().flatMap(ship ->ship.getShipLocations().stream()).collect(Collectors.toList());
        List<String>  hits = salvo.getSalvoLocations();
        return hits.stream().filter(loc::contains).collect(Collectors.toList());
    }

    public int getFullHits(GamePlayer gamePlayer){
        return gamePlayer.getSalvoes().stream().flatMap(salvo-> getHitsLocations(salvo).stream()).collect(Collectors.toList()).size();
    }




    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Date getCreated() { return created; }
    public void setCreated(Date created) { this.created = created; }

    public String getGameState() { return gameState; }
    public void setGameState(String gameState) { this.gameState = gameState; }

    public Set<GamePlayerDTO> getGamePlayers() { return gamePlayers; }
    public void setGamePlayers(Set<GamePlayerDTO> gamePlayers) { this.gamePlayers = gamePlayers; }

    public Set<ShipDTO> getShips() { return ships; }
    public void setShips(Set<ShipDTO> ships) { this.ships = ships; }

    public Set<SalvoDTO> getSalvoes() { return salvoes; }
    public void setSalvoes(Set<SalvoDTO> salvoes) { this.salvoes = salvoes; }

    public HitsDTO getHits() { return hits; }
    public void setHits(HitsDTO hits) { this.hits = hits; }
}
