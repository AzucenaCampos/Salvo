package com.codeoftheweb.salvo.dtos;

import com.codeoftheweb.salvo.Classes.Player;

public class PlayerDTO {
    private Long id;
    private String email;

    public PlayerDTO(Player player) {
        this.id= player.getId();
        this.email = player.getEmail();
    }

    public PlayerDTO() {
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
