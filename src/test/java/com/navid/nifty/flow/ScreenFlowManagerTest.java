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

import java.util.Collection;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.of;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.*;

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
    public void shouldFailIfScreenIsRepeatedInTheSameFlow() {
        ScreenDefinition sc1 = new ScreenDefinition("screen1", "controllerClass", "interfaceConstructor");
        ScreenDefinition sc2 = new ScreenDefinition("screen2", "controllerClass2", "interfaceConstructor2");
        ScreenDefinition sc3 = new ScreenDefinition("screen3", "controllerClass", "interfaceConstructor2");
        tested.addScreenDefinition(sc1);
        tested.addScreenDefinition(sc2);
        tested.addScreenDefinition(sc3);

        tested.addFlowDefinition("root", Optional.<String>absent(), newArrayList("screen1", "screen1"));
        fail();
    }

    @Test
    public void shouldNotMoveWithoutHints() {
        ScreenDefinition sc1 = new ScreenDefinition("screen1", "controllerClass", "interfaceConstructor");
        ScreenDefinition sc2 = new ScreenDefinition("screen2", "controllerClass2", "interfaceConstructor2");
        ScreenDefinition sc3 = new ScreenDefinition("screen3", "controllerClass", "interfaceConstructor2");
        tested.addScreenDefinition(sc1);
        tested.addScreenDefinition(sc2);
        tested.addScreenDefinition(sc3);

        tested.addFlowDefinition("root", Optional.<String>absent(), newArrayList("screen1", "screen2", "screen3"));
        assertEquals(tested.nextScreen(), "root:screen1");
        tested.setNextScreenHint(ScreenFlowManager.NEXT);
        assertEquals(tested.nextScreen(), "root:screen2");
        //If next screen is invoked, without hints, we expect the same screen.
        assertEquals(tested.nextScreen(), "root:screen2");
    }

    @Test
    public void shouldNotMoveWithWrongHints() {
        ScreenDefinition sc1 = new ScreenDefinition("screen1", "controllerClass", "interfaceConstructor");
        ScreenDefinition sc2 = new ScreenDefinition("screen2", "controllerClass2", "interfaceConstructor2");
        ScreenDefinition sc3 = new ScreenDefinition("screen3", "controllerClass", "interfaceConstructor2");
        tested.addScreenDefinition(sc1);
        tested.addScreenDefinition(sc2);
        tested.addScreenDefinition(sc3);

        tested.addFlowDefinition("root", Optional.<String>absent(), newArrayList("screen1", "screen2", "screen3"));
        assertEquals(tested.nextScreen(), "root:screen1");
        tested.setNextScreenHint(ScreenFlowManager.PREV);
        assertEquals(tested.nextScreen(), "root:screen1");
    }

    @Test(expected = IllegalStateException.class)
    public void shouldFailIfParentFlowDoesntExist() {
        ScreenDefinition sc1 = new ScreenDefinition("screen1", "controllerClass", "interfaceConstructor");
        ScreenDefinition sc2 = new ScreenDefinition("screen2", "controllerClass2", "interfaceConstructor2");
        ScreenDefinition sc3 = new ScreenDefinition("screen3", "controllerClass", "interfaceConstructor2");
        tested.addScreenDefinition(sc1);
        tested.addScreenDefinition(sc2);
        tested.addScreenDefinition(sc3);

        tested.addFlowDefinition("flow1", of("parent:screen2"), newArrayList("screen1", "screen2"));
        fail();
    }

    @Test
    public void shouldCreateChildFlowAndWalkIt() {
        ScreenDefinition sc1 = new ScreenDefinition("screen1", "controllerClass", "interfaceConstructor");
        ScreenDefinition sc2 = new ScreenDefinition("screen2", "controllerClass2", "interfaceConstructor2");
        ScreenDefinition sc3 = new ScreenDefinition("screen3", "controllerClass", "interfaceConstructor2");
        tested.addScreenDefinition(sc1);
        tested.addScreenDefinition(sc2);
        tested.addScreenDefinition(sc3);

        tested.addFlowDefinition("root", Optional.<String>absent(), newArrayList("screen1", "screen2"));
        tested.addFlowDefinition("child1", of("root:screen2"), newArrayList("screen1", "screen2"));
        tested.addFlowDefinition("child2", of("root:screen1"), newArrayList("screen1", "screen2"));

        assertEquals(tested.nextScreen(), "root:screen1");
        tested.setNextScreenHint(ScreenFlowManager.NEXT);
        assertEquals(tested.nextScreen(), "root:screen2");
        tested.setNextScreenHint("child1");
        assertEquals(tested.nextScreen(), "child1:screen1");
        tested.setNextScreenHint(ScreenFlowManager.NEXT);
        assertEquals(tested.nextScreen(), "child1:screen2");
        tested.setNextScreenHint(ScreenFlowManager.PREV);
        assertEquals(tested.nextScreen(), "child1:screen1");
        tested.setNextScreenHint(ScreenFlowManager.PREV);
        assertEquals(tested.nextScreen(), "root:screen2");
        tested.setNextScreenHint(ScreenFlowManager.PREV);
        assertEquals(tested.nextScreen(), "root:screen1");
        tested.setNextScreenHint("child2");
        assertEquals(tested.nextScreen(), "child2:screen1");
        tested.setNextScreenHint(ScreenFlowManager.NEXT);
        assertEquals(tested.nextScreen(), "child2:screen2");
        tested.setNextScreenHint(ScreenFlowManager.PREV);
        assertEquals(tested.nextScreen(), "child2:screen1");
        tested.setNextScreenHint(ScreenFlowManager.PREV);
        assertEquals(tested.nextScreen(), "root:screen1");
    }

    @Test
    public void shouldReturnEmptyChildren() {
        ScreenDefinition sc1 = new ScreenDefinition("screen1", "controllerClass", "interfaceConstructor");
        ScreenDefinition sc2 = new ScreenDefinition("screen2", "controllerClass2", "interfaceConstructor2");
        tested.addScreenDefinition(sc1);
        tested.addScreenDefinition(sc2);

        tested.addFlowDefinition("root", Optional.<String>absent(), newArrayList("screen1", "screen2"));
        tested.addFlowDefinition("child1", of("root:screen2"), newArrayList("screen1", "screen2"));

        Collection<String> children = tested.getChildren();
        assertTrue(children.isEmpty());
    }

    @Test
    public void shouldReturnChildren() {
        ScreenDefinition sc1 = new ScreenDefinition("screen1", "controllerClass", "interfaceConstructor");
        ScreenDefinition sc2 = new ScreenDefinition("screen2", "controllerClass2", "interfaceConstructor2");
        tested.addScreenDefinition(sc1);
        tested.addScreenDefinition(sc2);

        tested.addFlowDefinition("root", Optional.<String>absent(), newArrayList("screen1", "screen2"));
        tested.addFlowDefinition("child1", of("root:screen2"), newArrayList("screen1", "screen2"));
        tested.setNextScreenHint(ScreenFlowManager.NEXT);
        assertEquals(tested.nextScreen(), "root:screen2");

        Collection<String> children = tested.getChildren();
        assertFalse(children.isEmpty());
    }

}