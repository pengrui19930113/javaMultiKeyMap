package com.chess.landlord._3data.gaming.combo;

import com.chess.landlord.Poker;

public class Sequence extends AbstractCombo{
    protected Sequence() {
        super(ComboType.SEQUENCE);
    }

    public static int minSize() {
        return 5;
    }

    public static int maxSize() {
        return (Poker.__2-Poker._3);
    }
}
