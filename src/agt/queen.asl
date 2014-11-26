{ include("common-ant.asl") }

/* Initial beliefs and rules */
max_age(80).

/* Initial goals */
!life.

+!reproducting:
	true
<-
	anthill.actions.wait(2);
	.create_agent(Worker, "worker.asl");
	!reproducting;
	.