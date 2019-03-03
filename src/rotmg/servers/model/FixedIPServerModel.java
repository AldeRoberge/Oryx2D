package rotmg.servers.model;

import utils.flash.Vector;
import rotmg.net.Server;
import rotmg.parameters.Parameters;
import rotmg.servers.api.ServerModel;

public class FixedIPServerModel implements ServerModel {

    public static FixedIPServerModel instance;
    private Server localhost;

    public FixedIPServerModel() {
        super();
        this.localhost = new Server().setName("localhost").setPort(Parameters.PORT);
    }

    public static FixedIPServerModel getInstance() {
        if (instance == null) {
            instance = new FixedIPServerModel();
        }
        return instance;
    }

    public FixedIPServerModel setIP(String param1) {
        this.localhost.setAddress(param1);
        return this;
    }

    @Override
    public Vector<Server> getServers() {
        return new Vector<>(this.localhost);
    }

    @Override
    public void setServers(Vector<Server> param1) {
    }

    @Override
    public Server getServer() {
        return this.localhost;
    }

    @Override
    public boolean isServerAvailable() {
        return true;
    }
}