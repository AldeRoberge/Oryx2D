package rotmg.account.core.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rotmg.account.core.WebAccount;
import rotmg.account.core.signals.CharListDataSignal;
import rotmg.account.securityQuestions.data.SecurityQuestionsModel;
import rotmg.account.web.view.MigrationDialog;
import rotmg.account.web.view.WebLoginDialog;
import rotmg.appengine.api.AppEngineClient;
import rotmg.core.model.PlayerModel;
import rotmg.lib.tasks.tasks.BaseTask;
import rotmg.parameters.Parameters;
import rotmg.util.TextKey;
import flash.XML;
import flash.consumer.SignalConsumer;
import flash.utils.timer.Timer;

public class GetCharListTask extends BaseTask {

    Logger log = LoggerFactory.getLogger(GetCharListTask.class);

    private static final int ONE_SECOND_IN_MS = 1000;

    private static final int MAX_RETRIES = 7;
    public static GetCharListTask instance;
    public WebAccount account;
    public AppEngineClient client;
    public PlayerModel model;
    public CharListDataSignal charListData;
    public SecurityQuestionsModel securityQuestionsModel;
    private Object requestData;
    private Timer retryTimer;
    private int numRetries = 0;
    private boolean fromMigration = false;


    public GetCharListTask() {
        super();
    }

    public static GetCharListTask getInstance() {
        if (instance == null) {
            instance = new GetCharListTask();
        }
        return instance;
    }

    protected void startTask() {
        log.info("GetUserDataTask start");
        this.requestData = this.makeRequestData();
        this.sendRequest();
        Parameters.sendLogin = false;
    }

    private void sendRequest() {
        //this.client.complete.addOnce(new SignalConsumer<>(this::onComplete));

        this.client.sendRequest("/char/list", this.requestData);
    }

    private void onComplete(boolean param1, String param2) {
        if (param1) {
            this.onListComplete(param2);
        } else {
            this.onTextError(param2);
        }
    }

    public Object makeRequestData() {
		/*var loc1:Object = {};
		loc1.game_net_user_id = this.account.gameNetworkUserId();
		loc1.game_net = this.account.gameNetwork();
		loc1.play_platform = this.account.playPlatform();
		loc1.do_login = Parameters.sendLogin;
		MoreObjectUtil.addToObject(loc1, this.account.getCredentials());
		return loc1;*/
        return null;
    }

    private void onListComplete(String param1) {
        double loc3 = 0;
        MigrationDialog loc4 = null;
        XML loc2 = new XML(param1);
        if (loc2.hasOwnProperty("MigrateStatus")) {
            loc3 = loc2.getDoubleValue("MigrateStatus");
            if (loc3 == 5) {
                this.sendRequest();
            }
            loc4 = new MigrationDialog(this.account, loc3);
            this.fromMigration = true;
            loc4.done.addOnce(new SignalConsumer<>(this::sendRequest));
            loc4.cancel.addOnce(new SignalConsumer<>(this::clearAccountAndReloadCharacters));
        } else {
            if (loc2.hasOwnProperty("Account")) {
                if (this.account instanceof WebAccount) {
                    this.account.userDisplayName = loc2.child("Account").getValue("Name");
                    this.account.paymentProvider = loc2.child("Account").getValue("PaymentProvider");
                    if (loc2.child("Account").hasOwnProperty("PaymentData")) {
                        this.account.paymentData = loc2.child("Account").getValue("PaymentData");
                    }
                }
                if (loc2.children("Account").get(0).hasOwnProperty("SecurityQuestions")) {
                    this.securityQuestionsModel.showSecurityQuestionsOnStartup = loc2.child("Account").child("SecurityQuestions").getValue("ShowSecurityQuestionsDialog").equals("1");
                    this.securityQuestionsModel.clearQuestionsList();
                    for (XML loc5 : loc2.child("Account").child("SecurityQuestions").child("SecurityQuestionsKeys").children("SecurityQuestionsKey")) {
                        this.securityQuestionsModel.addSecurityQuestion(loc5.toString());
                    }
                }
            }
            this.charListData.dispatch(param1);
            this.completeTask(true);
        }
        if (this.retryTimer != null) {
            this.stopRetryTimer();
        }
    }


    private void onTextError(String param1) {
        WebLoginDialog loc2 = null;
        if (param1.equals("Account credentials not valid")) {

            log.error("Credentials not valid.");

            if (this.fromMigration) {
                loc2 = new WebLoginDialog();
                loc2.setError(TextKey.WEB_LOGIN_DIALOG_PASSWORD_INVALID);
                loc2.setEmail(this.account.getUserId());
            }
            this.clearAccountAndReloadCharacters();
        } else if (param1.equals("Account is under maintenance")) {
            log.error("Banned!");

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            clearAccountAndReloadCharacters();
        } else {
            this.waitForASecondThenRetryRequest();
        }
    }

    private void clearAccountAndReloadCharacters() {
        log.info("GetUserDataTask invalid credentials");
        this.account.clear();
        //this.client.complete.addOnce(new SignalConsumer<>(this::onComplete));
        this.requestData = this.makeRequestData();
        this.client.sendRequest("/char/list", this.requestData);
    }

    private void waitForASecondThenRetryRequest() {
        log.info("GetUserDataTask error - retrying");
        this.retryTimer = new Timer(ONE_SECOND_IN_MS, 1);
        //this.retryTimer.addEventListener(TimerEvent.TIMER_COMPLETE, new EventConsumer<>(this::onRetryTimer));
        this.retryTimer.start();
    }

    private void stopRetryTimer() {
        this.retryTimer.stop();
        //this.retryTimer.removeEventListener(TimerEvent.TIMER_COMPLETE, new EventConsumer<>(this::onRetryTimer));
        this.retryTimer = null;
    }

    private void onRetryTimer() {
        this.stopRetryTimer();
        if (this.numRetries < MAX_RETRIES) {
            this.sendRequest();
            this.numRetries++;
        } else {
            this.clearAccountAndReloadCharacters();
            log.error("Too many fails");
        }
    }
}
