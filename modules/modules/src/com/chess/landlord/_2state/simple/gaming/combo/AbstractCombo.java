package com.chess.landlord._2state.simple.gaming.combo;

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
