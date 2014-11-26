{ include("common-ant.asl") }

/* Initial beliefs and rules */
max_age(80).

/* Initial goals */
!life.

+!maintainingrole:
	true
<-
	true;
	.