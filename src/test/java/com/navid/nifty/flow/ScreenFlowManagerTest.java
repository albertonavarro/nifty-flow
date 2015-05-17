package com.navid.nifty.flow;

import com.google.common.base.Optional;
import com.navid.nifty.flow.dto.FlowDefinition;
import com.navid.nifty.flow.dto.ScreenDefinition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.of;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class ScreenFlowManagerTest {

    private ScreenFlowManager tested;

    @Mock
    private InstanceResolutor instanceResolutor;

    @Before
    public void setUp() throws Exception {
        tested = new ScreenFlowManagerImpl(instanceResolutor);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void shouldTakeScreenDefinitions() {
        ScreenDefinition sc1 = new ScreenDefinition("screen1", "controllerClass", "interfaceConstructor");
        ScreenDefinition sc2 = new ScreenDefinition("screen2", "controllerClass2", "interfaceConstructor2");
        ScreenDefinition sc3 = new ScreenDefinition("screen3", "controllerClass", "interfaceConstructor2");
        tested.addScreenDefinition(sc1);
        tested.addScreenDefinition(sc2);
        tested.addScreenDefinition(sc3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotTakeRepeatedScreenDefinitions() throws Exception {
        ScreenDefinition sc1 = new ScreenDefinition("screen1", "controllerClass", "interfaceConstructor");
        ScreenDefinition sc2 = new ScreenDefinition("screen2", "controllerClass2", "interfaceConstructor2");
        ScreenDefinition sc3 = new ScreenDefinition("screen1", "controllerClass3", "interfaceConstructor3");
        tested.addScreenDefinition(sc1);
        tested.addScreenDefinition(sc2);
        tested.addScreenDefinition(sc3);
    }

    @Test
    public void shouldCreateRootFlow() {
        ScreenDefinition sc1 = new ScreenDefinition("screen1", "controllerClass", "interfaceConstructor");
        ScreenDefinition sc2 = new ScreenDefinition("screen2", "controllerClass2", "interfaceConstructor2");
        ScreenDefinition sc3 = new ScreenDefinition("screen3", "controllerClass", "interfaceConstructor2");
        tested.addScreenDefinition(sc1);
        tested.addScreenDefinition(sc2);
        tested.addScreenDefinition(sc3);

        tested.addFlowDefinition("root", Optional.<String>absent(), newArrayList("screen1", "screen2"));
        assertEquals(tested.nextScreen(), "root:screen1");
        tested.setNextScreenHint(ScreenFlowManager.NEXT);
        assertEquals(tested.nextScreen(), "root:screen2");
        tested.setNextScreenHint(ScreenFlowManager.PREV);
        assertEquals(tested.nextScreen(), "root:screen1");
    }

    @Test(expected = IllegalStateException.class)
    public void shouldFailIfRootFlowDoesntExist() {
        ScreenDefinition sc1 = new ScreenDefinition("screen1", "controllerClass", "interfaceConstructor");
        ScreenDefinition sc2 = new ScreenDefinition("screen2", "controllerClass2", "interfaceConstructor2");
        ScreenDefinition sc3 = new ScreenDefinition("screen3", "controllerClass", "interfaceConstructor2");
        tested.addScreenDefinition(sc1);
        tested.addScreenDefinition(sc2);
        tested.addScreenDefinition(sc3);

        tested.addFlowDefinition("flow1", of("unexisting:unexisting"), newArrayList("screen1", "screen2"));
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailIfRootFormatIsIncorrect() {
        ScreenDefinition sc1 = new ScreenDefinition("screen1", "controllerClass", "interfaceConstructor");
        ScreenDefinition sc2 = new ScreenDefinition("screen2", "controllerClass2", "interfaceConstructor2");
        ScreenDefinition sc3 = new ScreenDefinition("screen3", "controllerClass", "interfaceConstructor2");
        tested.addScreenDefinition(sc1);
        tested.addScreenDefinition(sc2);
        tested.addScreenDefinition(sc3);

        tested.addFlowDefinition("flow1", of("wrongFormat"), newArrayList("screen1", "screen2"));
        fail();
    }

    @Test
    public void testNextScreen() throws Exception {

    }
}