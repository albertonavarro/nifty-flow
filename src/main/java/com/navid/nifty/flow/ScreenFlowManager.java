package com.navid.nifty.flow;

import com.google.common.base.Optional;
import com.navid.nifty.flow.dto.ScreenDefinition;
import com.navid.nifty.flow.dto.ScreenInstance;

import java.util.List;

public interface ScreenFlowManager {
    String NEXT = "next";
    String PREV = "prev";
    String POP = "pop";

    void addScreenConfiguration(ScreenDefinition screenDefinition);

    void addFlowDefinition(String flowName, final Optional<ScreenInstance> screenNameFrom, List<String> flowDefinition);

    String nextScreen();

    void setNextScreenHint(String nextScreenHint);
}
