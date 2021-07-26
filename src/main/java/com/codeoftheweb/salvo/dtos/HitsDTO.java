package com.codeoftheweb.salvo.dtos;

import com.codeoftheweb.salvo.Classes.GamePlayer;
import com.codeoftheweb.salvo.Classes.Salvo;
import com.codeoftheweb.salvo.Classes.Ship;

import java.util.*;
import java.util.stream.Collectors;

public class HitsDTO {
    private List<Map> self;
    private List<Map> opponent;

    public HitsDTO() {
    }

    public HitsDTO (GamePlayer gamePlayer){
        if(gamePlayer.getOpponentGameP().isPresent()){
            this.self= getDamages(gamePlayer.getOpponentGameP().get());
            this.opponent = getDamages(gamePlayer);
        }else{
            this.self=new ArrayList<>();
            this.opponent = new ArrayList<>();
        }
    }

    /*public Map<String, Object> getListHits(Salvo salvo){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("turn", salvo.getTurn());
        dto.put("hitLocations", this.getHitsLocations(salvo));
        dto.put("damages", this.getDamages(salvo));
        dto.put("missed", salvo.getSalvoLocations().size()-this.getHitsLocations(salvo).size());
        return dto;
    }*/

    public List<String> getHitsLocations(Salvo salvo) {
        GamePlayer opponent = salvo.getGamePlayer().getOpponentGameP().get();
        List<String> loc = opponent.getShips().stream().flatMap(ship ->ship.getShipLocations().stream()).collect(Collectors.toList());
        List<String>  hits = salvo.getSalvoLocations();
        return hits.stream().filter(l -> loc.contains(l)).collect(Collectors.toList());
    }

    public int getFullHits(GamePlayer gamePlayer){
        return gamePlayer.getSalvoes().stream().flatMap(salvo-> getHitsLocations(salvo).stream()).collect(Collectors.toList()).size();
    }

    public List<Map> getDamages(GamePlayer gamePlay){
        List<Map> self1 = new ArrayList<>();
        int carrier = 0;
        int battleship =0;
        int submarine = 0;
        int destroyer = 0;
        int patrolboat = 0;

        if(!gamePlay.getOpponentGameP().isPresent()){
            return null;
        }

        List<String> carrierLocation = getLocationByType(gamePlay.getOpponentGameP().get(), "carrier");
        List<String> battleshipLocation = getLocationByType(gamePlay.getOpponentGameP().get(), "battleship");
        List<String> submarineLocation = getLocationByType(gamePlay.getOpponentGameP().get(), "submarine");
        List<String> destroyerLocation = getLocationByType(gamePlay.getOpponentGameP().get(), "destroyer");
        List<String> patrolboatLocation = getLocationByType(gamePlay.getOpponentGameP().get(), "patrolboat");

        for(Salvo salvo : gamePlay.getSalvoes()){
            long carrierHits =0;
            long battleshipHits = 0;
            long submarineHits = 0;
            long destroyerHits =0;
            long patrolboatHits =0;
            long missed = salvo.getSalvoLocations().size();
            List<String> hitsList = new ArrayList<>();

            for(String salvoShot : salvo.getSalvoLocations()){
                if(carrierLocation.contains(salvoShot)){
                    carrierHits++;
                    missed--;
                    hitsList.add(salvoShot);
                    carrier++;
                }
                if(battleshipLocation.contains(salvoShot)){
                    battleshipHits++;
                    missed--;
                    hitsList.add(salvoShot);
                    battleship++;
                }
                if(submarineLocation.contains(salvoShot)){
                    submarineHits++;
                    missed--;
                    hitsList.add(salvoShot);
                    submarine++;
                }
                if(destroyerLocation.contains(salvoShot)){
                    destroyerHits++;
                    missed--;
                    hitsList.add(salvoShot);
                    destroyer++;
                }
                if(patrolboatLocation.contains(salvoShot)){
                    patrolboatHits++;
                    missed--;
                    hitsList.add(salvoShot);
                    patrolboat++;
                }
            }
            Map<String, Object> dto = new LinkedHashMap<>();
            dto.put("carrierHits", carrierHits);
            dto.put("battleshipHits", battleshipHits);
            dto.put("submarineHits",submarineHits);
            dto.put("destroyerHits",destroyerHits);
            dto.put("patrolboatHits", patrolboatHits);
            dto.put("carrier", carrier);
            dto.put("battleship", battleship);
            dto.put("submarine", submarine);
            dto.put("patrolboat", patrolboat);
            dto.put("destroyer", destroyer);

            Map<String,Object> dtos=new LinkedHashMap<>();
            dtos.put("turn",salvo.getTurn());
            dtos.put("hitLocations", hitsList);
            dtos.put("damages",dto);
            dtos.put("missed", missed);
            self1.add(dtos);
        }
        return self1;
    }
    private List<String> getLocationByType(GamePlayer gameP, String type){
        Ship locations = gameP.getShips().stream().filter(ty-> ty.getType().equals(type)).findFirst().orElse(null);
        if(locations!=null){
            return locations.getShipLocations();
        }
        return new ArrayList<>();
    }

    public List<Map> getSelf() {
        return self;
    }
    public void setSelf(List<Map> self) {
        this.self = self;
    }

    public List<Map> getOpponent() {
        return opponent;
    }
    public void setOpponent(List<Map> opponent) {
        this.opponent = opponent;
    }
}
