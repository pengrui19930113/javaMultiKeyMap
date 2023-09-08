package com.test;

public class StateMachine {
    interface State{
        void onExit(Object...params);
        void onEnter(Object...params);
        void onTimer(Object...params);
    };
    interface StateContext{}
    interface States{
        State findCurrentWorkingState(StateContext stateCtx);
    }
    boolean running;

    boolean stateChange;
    States states;
    StateContext stateCtx;
    State cur;
    void run(){
        while(running){
            if(stateChange){
                final State old = cur;
                cur = states.findCurrentWorkingState(stateCtx);
                old.onExit();
                cur.onEnter();
                stateChange = false;
            }
            cur.onTimer();
        }
    }
}
