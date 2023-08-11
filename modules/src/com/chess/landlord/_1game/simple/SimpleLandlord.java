package com.chess.landlord._1game.simple;

import com.chess.Final;
import com.chess.landlord.GeneralLandlord;
import com.chess.landlord.Poker;
import com.chess.landlord._1game.Config;
import com.chess.landlord._1game.StateHandler;
import com.chess.landlord._1game.StateSuperContext;
import com.chess.landlord._2state.simple.*;

import java.io.PrintStream;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Peasants vs Landlord
 * 抽象斗地主的流程
 * 开局->洗牌->发牌->抢地主-> 游戏中->结束
 *       ^         /
 *        \-------v
 *          多次弃牌
 */
public class SimpleLandlord
        implements GeneralLandlord
        , StartingContext
        , ShufflingContext
        , DealingContext
        , LandingContext
        , GamingContext
        , FinishingContext
{
    protected static final Runnable NULL = ()->{};
    protected State state0;
    protected Runnable ticker;
    protected Runnable timeoutTrigger;
    @Final
    protected Queue<Runnable> post;
    protected PrintStream log;
    protected StateHandler stateHandler;
    protected long last;
    protected long duration;
    protected SimpleConfig config;
//    protected 如果用此修饰符修饰 则包外的子类可以获取到改对象的引用
    @Final
    Data data;
    public SimpleLandlord(Config c){
        config = (SimpleConfig)c;
        log = System.out;
        state0 = State.NULL;
        ticker = NULL;
        duration = 0;
        stateHandler = null;
        post = new ConcurrentLinkedQueue<>();
        this.data = new Data().init(this);
        last = now();
    }
    protected void post(Runnable r){
        if(Objects.nonNull(r)){
            if(config.isDebug()){
                log.println("post runnable");
            }
            if(!post.offer(r)){
                log.println("queue.offer == false");
            }
        }
    }

    @Override
    public long last() {
        return this.last;
    }
    @Override
    public long duration() {
        return this.duration;
    }
    protected void _init(){
        log.println("initializing ...");
        this.onStarting();
    }
    @Override
    public void onInit() {
        log.println("post init");
        post(this::_init);
    }
    protected void _destroy(){
        switch (state0){
            case FINISHED:
                //通知客户端状态 async(null);
                state0 = State.DONE;
                break;
            case GAMING:
                log.println("warning:game is running,but on destroy,ignore action");
                break;
            default:
                throw new IllegalStateException();
        }
    }
    @Override
    public void onDestroy() {
        post(this::_destroy);
    }
    protected void _start(){
        switch (state0){
            case NULL:
                final StartingContext ctx = this;
                stateHandler = new Starting();
                stateHandler.onInit(ctx);
                ticker = ()-> stateHandler.onTimer(ctx);
                //通知客户端状态 async(null);
                state0 = State.STARTING;
                break;
            default:
                throw new IllegalStateException();
        }
    }
    @Override
    public void onStarting() {
        post(this::_start);
    }
    protected void _shuffling(){
        switch (state0){
            case STARTING:{
                final StartingContext sc = this;
                final Starting state = (Starting)stateHandler;
                timeoutTrigger = null;
                state.onDestroy(sc);
            }break;
            case LANDING:{
                final LandingContext sc = this;
                final Landing state = (Landing)stateHandler;
                state.onDestroy(sc);
            }break;
            default:
                throw new IllegalStateException();
        }
        final ShufflingContext ctx = this;
        stateHandler = new Shuffling();
        stateHandler.onInit(ctx);
        ticker = ()-> stateHandler.onTimer(ctx);
        //超时设置; timeout =
        //通知客户端状态 async(null);
        state0 = State.SHUFFLING;
    }
    @Override
    public void onShuffling() {
        post(this::_shuffling);
    }
    protected void _dealing(){
        switch (state0){
            case SHUFFLING:{
                final StartingContext sc = this;
                stateHandler.onDestroy(sc);
                final DealingContext ctx = this;
                stateHandler = new Dealing();
                stateHandler.onInit(ctx);
                ticker = ()-> stateHandler.onTimer(ctx);
                //通知客户端状态 async(null);
                state0 = State.DEALING;
            }break;
            default:
                throw new IllegalStateException();
        }

    }
    @Override
    public void onDealing() {
        post(this::_dealing);
    }
    protected void _landing(){
        switch (state0){
            case DEALING:
                final ShufflingContext sc = this;
                stateHandler.onDestroy(sc);
                final DealingContext ctx = this;
                stateHandler = new Landing();
                stateHandler.onInit(ctx);
                ticker = ()-> stateHandler.onTimer(ctx);
                //超时设置; timeout =
                //通知客户端状态 async(null);
                state0 = State.LANDING;
                break;
            default:
                throw new IllegalStateException();
        }

    }
    @Override
    public void onLanding() {
        post(this::_landing);
    }
    protected void _gaming(){
        switch (state0){
            case LANDING:
                final LandingContext dc = this;
                stateHandler.onDestroy(dc);
                final GamingContext ctx = this;
                stateHandler = new Gaming();
                stateHandler.onInit(ctx);
                ticker = ()-> stateHandler.onTimer(ctx);
                timeoutTrigger = null;
                //通知客户端状态 async(null);
                break;
            default:
                throw new IllegalStateException();
        }
    }
    @Override
    public void onGaming() {
        post(this::_gaming);
    }
    protected void _finishing(){
        switch (state0){
            case LANDING:
                final GamingContext dc = this;
                stateHandler.onDestroy(dc);
                final FinishingContext ctx = this;
                stateHandler = new Finishing();
                stateHandler.onInit(ctx);
                ticker = ()-> stateHandler.onTimer(ctx);
                timeoutTrigger = null;
                //通知客户端状态 async(null);
                break;
            default:
                throw new IllegalStateException();
        }
    }
    @Override
    public void onFinishing() {
        post(this::_finishing);
    }
    protected void _action(StateSuperContext ctx, StateHandler handler, Object... params){
        handler.onAction(ctx,params);
    }
    @Override
    public void onAction(Object... params) {
        post(()->_action(this,this.stateHandler,params));
    }
    @Override
    public void onTimer() {
        final long tmp = now();
        duration = tmp-last;
        last = tmp;
        int maxPerTickCount = config.getTickMaxPostRunnable();
        final Queue<Runnable> queue = this.post;
        while(maxPerTickCount>0){
            final Runnable poll = queue.poll();
            if(Objects.isNull(poll))
                break;
            try{
                poll.run();
            }catch(Throwable t){
                t.printStackTrace(log);
            }
            maxPerTickCount--;
        }
        final Runnable ticker = this.ticker;
        if(Objects.isNull(ticker)){
            if(config.isDebug())
                log.println("ticker is null");
        }else{
            try{
                ticker.run();
            }catch(Throwable t){
                t.printStackTrace(log);
            }
        }
        final Runnable trigger = this.timeoutTrigger;
        if(Objects.nonNull(trigger))
            trigger.run();
    }
    @Override
    public void onLandingFailure(StateHandler state) {
        final Landing landing = (Landing) state;
        this.onShuffling();
    }

    @Override
    public void onLandingSuccess(StateHandler state) {
        final Landing landing = (Landing) state;
        this.onGaming();
    }
    @Override
    public void onGamingOver(StateHandler state) {
        final Gaming gaming = (Gaming) state;
        this.onFinishing();
    }
    @Override
    public void onStartingSuccess(StateHandler state) {
        final Starting starting =  (Starting)state;
        this.onShuffling();
    }
    @Override
    public void onDealingSuccess(StateHandler state) {
        final Dealing dealing = (Dealing) state;
        final List<Poker>[] result = dealing.result();
        data.one = result[0];
        data.two = result[1];
        data.three = result[2];
        data.goal = result[3];
        this.onLanding();
    }

    @Override
    public List<Poker> shuffledPokers() {
        return data.shuffledPokers;
    }

    @Override
    public void onShufflingSuccess(StateHandler state) {
        final Shuffling shuffling = (Shuffling) state;
        data.shuffledPokers = shuffling.result();
        this.onDealing();
    }

    @Override
    public void info() {
        log.println("current state:"+state0);
        log.println("current handler:"+stateHandler.getClass());
    }
}
