package ext.de.lessvoid.nifty.flow;

import de.lessvoid.nifty.screen.ScreenController;

public final class ScreenFlowUnit {

    private String screenName;
    private ScreenGenerator interfaceConstructor;
    private ScreenController controller;

    public ScreenFlowUnit(String screenName, ScreenGenerator interfaceConstructor, ScreenController controller) {
        this.screenName = screenName;
        this.interfaceConstructor = interfaceConstructor;
        this.controller = controller;
    }

    public ScreenFlowUnit(ScreenConfiguration modScreenConfiguration, BeanSupplier beanFactory) {
        this.interfaceConstructor = beanFactory.getBean(modScreenConfiguration.getInterfaceConstructor(), ScreenGenerator.class);
        this.controller = beanFactory.getBean(modScreenConfiguration.getController(), ScreenController.class);
        this.screenName = modScreenConfiguration.getScreenName();
    }

    /**
     * @return the screenName
     */
    public String getScreenName() {
        return screenName;
    }

    /**
     * @param screenName the screenName to set
     */
    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    /**
     * @return the interfaceConstructor
     */
    public ScreenGenerator getInterfaceConstructor() {
        return interfaceConstructor;
    }

    /**
     * @param interfaceConstructor the interfaceConstructor to set
     */
    public void setInterfaceConstructor(ScreenGenerator interfaceConstructor) {
        this.interfaceConstructor = interfaceConstructor;
    }

    /**
     * @return the controller
     */
    public ScreenController getController() {
        return controller;
    }

    /**
     * @param controller the controller to set
     */
    public void setController(ScreenController controller) {
        this.controller = controller;
    }

}
