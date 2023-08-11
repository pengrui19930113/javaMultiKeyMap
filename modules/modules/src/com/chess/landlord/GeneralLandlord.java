package com.chess.landlord;

import com.chess.ParentCallChild;

/**
 * Peasants vs Landlord
 * 抽象斗地主的流程
 * 开局->洗牌->发牌->抢地主-> 游戏中->结束
 *       ^         /
 *        \-------v
 *          多次弃牌
 */
public interface GeneralLandlord {
    default void info(){};
    enum State{
        NULL,STARTING,SHUFFLING,DEALING,LANDING,GAMING,FINISHED,DONE
    }
    /** lifecycle start **/
    @ParentCallChild
    void onInit();
    @ParentCallChild
    void onDestroy();
    /** lifecycle start **/

    /** action start **/
    /**
     void onActivePlayerAction();
     void onSystemManagerAction();
     void onConnAction();
     */
    @ParentCallChild
    void onAction(Object... params);
    @ParentCallChild
    void onTimer();
    /** action end **/

    /** state start **/
    void onStarting();
    //Collections.shuffle();
    void onShuffling();
    void onDealing();
    void onLanding();
    void onGaming();
    void onFinishing();
    /** state end **/


}
