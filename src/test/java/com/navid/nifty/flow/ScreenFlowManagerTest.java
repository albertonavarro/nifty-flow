package com.navid.nifty.flow;

import com.google.common.base.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.google.common.base.Optional.absent;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Optional.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class ScreenFlowManagerTest {

    private ScreenFlowManager tested;

    @Mock
    private InstanceResolutor instanceResolutor;

    @Before
    public void setUp() throws Exception {
        tested = new ScreenFlowManager(instanceResolutor);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void shouldTakeScreenConfigurations() {
        ScreenConfiguration sc1 = new ScreenConfiguration("screen1", "controllerClass", "interfaceConstructor");
        ScreenConfiguration sc2 = new ScreenConfiguration("screen2", "controllerClass2", "interfaceConstructor2");
        ScreenConfiguration sc3 = new ScreenConfiguration("screen3", "controllerClass", "interfaceConstructor2");
        tested.addScreenConfiguration(sc1);
        tested.addScreenConfiguration(sc2);
        tested.addScreenConfiguration(sc3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotTakeRepeatedScreenConfigurations() throws Exception {
        ScreenConfiguration sc1 = new ScreenConfiguration("screen1", "controllerClass", "interfaceConstructor");
        ScreenConfiguration sc2 = new ScreenConfiguration("screen2", "controllerClass2", "interfaceConstructor2");
        ScreenConfiguration sc3 = new ScreenConfiguration("screen1", "controllerClass3", "interfaceConstructor3");
        tested.addScreenConfiguration(sc1);
        tested.addScreenConfiguration(sc2);
        tested.addScreenConfiguration(sc3);
    }

    @Test
    public void shouldCreateRootFlow() {
        ScreenConfiguration sc1 = new ScreenConfiguration("screen1", "controllerClass", "interfaceConstructor");
        ScreenConfiguration sc2 = new ScreenConfiguration("screen2", "controllerClass2", "interfaceConstructor2");
        ScreenConfiguration sc3 = new ScreenConfiguration("screen3", "controllerClass", "interfaceConstructor2");
        tested.addScreenConfiguration(sc1);
        tested.addScreenConfiguration(sc2);
        tested.addScreenConfiguration(sc3);

        FlowDefinition flowDefinition = new FlowDefinition("root", java.util.Optional.<String>empty(), newArrayList("screen1", "screen2"));
        tested.addFlowDefinition(flowDefinition);
        assertEquals(tested.nextScreen(), "root:screen1");
        tested.setNextScreenHint(ScreenFlowManager.NEXT);
        assertEquals(tested.nextScreen(), "root:screen2");
        tested.setNextScreenHint(ScreenFlowManager.PREV);
        assertEquals(tested.nextScreen(), "root:screen1");
    }

    @Test(expected = IllegalStateException.class)
    public void shouldFailIfRootFlowDoesntExist() {
        ScreenConfiguration sc1 = new ScreenConfiguration("screen1", "controllerClass", "interfaceConstructor");
        ScreenConfiguration sc2 = new ScreenConfiguration("screen2", "controllerClass2", "interfaceConstructor2");
        ScreenConfiguration sc3 = new ScreenConfiguration("screen3", "controllerClass", "interfaceConstructor2");
        tested.addScreenConfiguration(sc1);
        tested.addScreenConfiguration(sc2);
        tested.addScreenConfiguration(sc3);

        FlowDefinition flowDefinition = new FlowDefinition("flow1", of("unexisting"), newArrayList("screen1", "screen2"));
        tested.addFlowDefinition(flowDefinition);
        fail();
    }

    @Test
    public void testNextScreen() throws Exception {

    }
}