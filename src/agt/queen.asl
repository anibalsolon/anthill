{ include("common-ant.asl") }

/* Initial beliefs and rules */
max_age(80).

/* Initial goals */
!life.

+!startReasoning
	: true
<-
	!move(2, 4, 4);
	.