package com.navid.nifty.flow;

import com.google.common.base.Optional;
import com.navid.nifty.flow.dto.ScreenDefinition;

import java.util.Collection;
import java.util.List;

public interface ScreenFlowManager {
    String NEXT = "next";
    String PREV = "prev";
    String POP = "pop";

    void addScreenDefinition(ScreenDefinition screenDefinition);

    void addFlowDefinition(String flowName, final Optional<String> screenNameFrom, List<String> flowDefinition);

    String nextScreen();

    void setNextScreenHint(String nextScreenHint);

    Collection<String> getChildren();
}
