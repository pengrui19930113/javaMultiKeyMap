package com.chess.landlord._3data.gaming.combo;

import java.util.Objects;

public abstract class AbstractCombo implements Combo{

    protected final ComboType type;
    protected AbstractCombo(ComboType type){
        this.type = Objects.requireNonNull(type);
    }
    @Override
    public ComboType type() {
        return type;
    }
}
