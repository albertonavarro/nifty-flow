Nifty-flow
==========

Nifty meets flow feature.

Abstract
--------
I found the need for this library, or anything similar, when I found that it was difficult to 
make reusable screens (like a library of screens that know little about what's the next and previous screens in
a flow.

The concept of flow itself was missing from nifty-gui library, templating (that will be eventually coming) was another
must have for my needs, and could only be reached by not-so-nice java code.

The solution you see here is actually extracted from my own game, and it could be biased, of course, to my needs, but
I've done the hardest effort to separate and abstract concepts.

I opted for a solution that didn't imply to modify core nifty libraries, and after checking the acceptance or the need,
maybe it can be included as part of core.

Main concepts:
--------------
**Screen**: is a uniique name, a flow it belongs to, a ScreenController (from 
Nifty-gui) and a ScreenGenerator (that actually builds the screen when required).

**Flow**: is a sequence of screen instances with a meaning together, they are supposed to be explored 
in a given order. It requires the screens to be defined first.

There's only one root flow, with absent parent, any other makes reference a screen of another flow.

**Redirector**: is an special controller, declared in Nifty aside flows, and it will decide which is 
the next screen to visit after every screen.

**ScreenUniqueId**: flowName:screenName, it must be globally unique.

**Hint**: is a String that helps the redirector to take the right decision to the next screen. It can be 
a protected word like *NEXT*, *PREV* or *POP*, it could be a screen name (to jump within the flow) or a 
screenUniqueId to jump to another flow.

**Instance resolutor**: is the way of telling Nifty-flow how to retrieve instances, as they could exist 
in a Guice or Spring environment, or being pure new instances, up to the consumer. This interface will ease 
integration with other systems.

**Links**: are the more predefined paths between screens, they are created automatically with different types 
like:
 
 * *implicit*: NEXT and PREV between sequential.
 * *module*: between parent and child, with module name in the link.
 * *exit*: between child and parent, with PREV name.

Setting up flow:
----------------
I won't say it could be simpler, but it's worthy ;)
In my scenario, nifty, the controllers and everything is in Spring, so I start preparing a instance resolutor 
(not yet included) to load beans from Spring:

`//Main structures
DefaultInstanceResolutor resolutorChain = new DefaultInstanceResolutor(); //or ctx.getBean(DefaultInstanceResolutor.class);
resolutorChain.addResolutor("spring", new SpringBeanResolutor(ctx)); //implementation not include, see above for example
flowManager = new ScreenFlowManagerImpl(nifty, instanceResolutor);`

`//My first flow definition
screenFlowManager.addScreenDefinition(new ScreenDefinition("root", "spring:common.RootScreenController", "spring:common.RootScreenGenerator"));
screenFlowManager.addFlowDefinition("root", Optional.<String>absent(), newArrayList("root")); //parent is absent, this is root.`

`//Once I finished to load flows, I'm ready to start, mandatory name, mandatory implementation
nifty.addScreen("redirector", new ScreenBuilder("start", beanFactory.getBean(RedirectorScreenController.class)).build(nifty));
nifty.gotoScreen("redirector");`

Lifecycle from here:
--------------------
Once redirector is invoked, it calculates given the flows included, current screen and the hint, the 
following screen, that's invoked from the redirector.
 
That screen will start with the screenGenerator, the screen is build and loaded at this point from code 
or templated xml.

After screen has been loaded, normal nifty lifecycle is triggered, with startScreen, endScreen and normal 
controller calls.

From the controllers it's expected to have access to the flowManager, to be able to invoke:

`screenFlowManager.setNextScreenHint(hint); //hint can be NEXT, PREV, a child flow name, a screen in this flow, or a screenUniqueId.
nifty.gotoScreen("redirector");`

