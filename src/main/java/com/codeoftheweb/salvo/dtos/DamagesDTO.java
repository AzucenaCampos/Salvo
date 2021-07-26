package com.codeoftheweb.salvo.dtos;

import com.codeoftheweb.salvo.Classes.Salvo;
import com.codeoftheweb.salvo.Classes.Ship;

import java.util.List;
import java.util.stream.Collectors;

public class DamagesDTO {
    private int carrierHits;
    private int battleshipHits;
    private int submarineHits;
    private int destroyerHits;
    private int patrolboatHits;
    private int carrier;
    private int battleship;
    private int submarine;
    private int destroyer;
    private int patrolboat;

 public DamagesDTO() {
 }

 public DamagesDTO(Salvo salvo){
        this.carrierHits= carrierHits;
        this.battleshipHits= battleshipHits;
        this.submarineHits= submarineHits;
        this.destroyerHits= destroyerHits;
        this.patrolboatHits= patrolboatHits;

        this.carrier= carrier;
        this.battleship=battleship;
        this.submarine= submarine;
        this.destroyer= destroyer;
        this.patrolboat= patrolboat;
    }

    public int getCarrierHits() {
        return carrierHits;
    }
    public void setCarrierHits(int carrierHits) {
        this.carrierHits = carrierHits;
    }

    public int getBattleshipHits() {
        return battleshipHits;
    }
    public void setBattleshipHits(int battleshipHits) {
        this.battleshipHits = battleshipHits;
    }

    public int getSubmarineHits() {
        return submarineHits;
    }
    public void setSubmarineHits(int submarineHits) {
        this.submarineHits = submarineHits;
    }

    public int getDestroyerHits() {
        return destroyerHits;
    }
    public void setDestroyerHits(int destroyerHits) {
        this.destroyerHits = destroyerHits;
    }

    public int getPatrolboatHits() {
        return patrolboatHits;
    }
    public void setPatrolboatHits(int patrolboatHits) {
        this.patrolboatHits = patrolboatHits;
    }

    public int getCarrier() {
        return carrier;
    }
    public void setCarrier(int carrier) {
        this.carrier = carrier;
    }

    public int getBattleship() {
        return battleship;
    }
    public void setBattleship(int battleship) {
        this.battleship = battleship;
    }

    public int getSubmarine() {
        return submarine;
    }
    public void setSubmarine(int submarine) {
        this.submarine = submarine;
    }

    public int getDestroyer() {
        return destroyer;
    }
    public void setDestroyer(int destroyer) {
        this.destroyer = destroyer;
    }

    public int getPatrolboat() {
        return patrolboat;
    }
    public void setPatrolboat(int patrolboat) {
        this.patrolboat = patrolboat;
    }
}
