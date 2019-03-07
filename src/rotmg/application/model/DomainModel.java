package rotmg.application.model;

import flash.Vector;

public class DomainModel {

    private final String LOCALHOST = "localhost";

    private final Vector<String> PRODUCTION_WHITELIST = new Vector<>("www.realmofthemadgod.com", "realmofthemadgodhrd.appspot.com", "realmofthemadgod.appspot.com");

    private final Vector<String> TESTING_WHITELIST = new Vector<>("testing.realmofthemadgod.com", "rotmgtesting.appspot.com", "rotmghrdtesting.appspot.com");

    private final Vector<String> TESTING2_WHITELIST = new Vector<>("realmtesting2.appspot.com");

    private final Vector<String> TRANSLATION_WHITELIST = new Vector<>("xlate.kabam.com");

    private final Vector<String> WHITELIST = this.PRODUCTION_WHITELIST.concat(this.TESTING_WHITELIST).concat(this.TRANSLATION_WHITELIST).concat(this.TESTING2_WHITELIST);

    private String localDomain;

    public DomainModel() {
        super();
    }


    public boolean isLocalDomainProduction() {
        String loc1 = this.getLocalDomain();
        return this.PRODUCTION_WHITELIST.contains(loc1);
    }

    private boolean isLocalDomainInWhiteList() {
        String loc1 = this.getLocalDomain();
        boolean loc2 = loc1.equals(this.LOCALHOST);
        for (String loc3 : this.WHITELIST) {
            loc2 = loc2 || loc1.equals(loc3);
        }
        return loc2;
    }

    private String getLocalDomain() {
        return this.localDomain;
    }
}
