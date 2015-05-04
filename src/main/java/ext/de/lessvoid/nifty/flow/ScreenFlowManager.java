package ext.de.lessvoid.nifty.flow;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import static com.google.common.collect.Lists.newArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ScreenFlowManager {

    private final Map<String, ScreenFlowUnit> mapScreenDeclarations = new HashMap<String, ScreenFlowUnit>();

    private final ScreenFlowState screenFlowState = new ScreenFlowState();

    private ScreenFlowGraph root = null;


    private final Map<String, ScreenFlowGraph> screenFlowGraph = new HashMap<String, ScreenFlowGraph>();

    public ScreenFlowGraph addRootFlowGraph(String name) {
        root = new ScreenFlowGraph();
        screenFlowGraph.put(name, root);
        for (ScreenFlowGraph current : screenFlowGraph.values()) {
            current.addParentFlow(root);
        }
        return root;
    }

    public ScreenFlowGraph addFlowGraph(String name) {
        ScreenFlowGraph result = new ScreenFlowGraph();
        result.addParentFlow(root);
        screenFlowGraph.put(name, result);
        return result;
    }

    public List<String> getModuleNames() {
        List<String> modules = newArrayList(Iterables.filter(screenFlowGraph.keySet(), new Predicate<String>() {

            @Override
            public boolean apply(String t) {
                return !t.equals("root");
            }
        }));

        Collections.sort(modules);
        return modules;
    }

    public String nextScreen() {
        if (screenFlowState.getCurrentFlow() == null) {
            screenFlowState.setCurrentFlow(screenFlowState.getScreenCommand());
            screenFlowState.setCurrentScreen(screenFlowGraph.get(screenFlowState.getCurrentFlow()).getStartScreenName());
        } else {
            screenFlowState.setCurrentScreen(screenFlowGraph.get(screenFlowState.getCurrentFlow()).getNextScreenName(screenFlowState));
        }

        ScreenFlowUnit nextScreenConfig
                = screenFlowGraph.get(screenFlowState.getCurrentFlow()).getScreenConfiguration(screenFlowState.getCurrentScreen());

        nextScreenConfig.getInterfaceConstructor().buildScreen();

        return screenFlowState.getCurrentScreen();
    }

    public void changeFlow(String moduleName) {
        screenFlowState.setCurrentFlow(moduleName);
        screenFlowState.setCurrentScreen(null);
        screenFlowState.setScreenCommand(null);
    }

    public void changeNextScreen() {
        screenFlowState.setScreenCommand("next");
    }

    public void changePreviousScreen() {
        screenFlowState.setScreenCommand("back");
    }

    public void changeNextScreen(String nextScreen) {
        screenFlowState.setScreenCommand(nextScreen);
    }

    public ScreenFlowUnit getScreen(String screenName) {
        return mapScreenDeclarations.get(screenName);
    }

    public void addScreenDeclaration(ScreenFlowUnit screenFlowUnit) {
        mapScreenDeclarations.put(screenFlowUnit.getScreenName(), screenFlowUnit);
    }

}
