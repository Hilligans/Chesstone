package dev.hilligans.spring.chesstone;

import java.io.Serializable;

public class CreateGameData implements Serializable {

    public String id;

    public CreateGameData(String id) {
        this.id = id;
    }
}