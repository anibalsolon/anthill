{ include("common-ant.asl") }
{ include("maintainer-gardening.asl") }
{ include("maintainer-nursing.asl") }

/* Initial beliefs and rules */
max_age(80).

/* Initial goals */
!life.

+!maintainingrole:
	location(CurrLevel, X, Y) & lvlknow(CurrLevel, W, H)
<-
	.print("Maintaining!");
	adoptRole("gardener");
	.

+!maintainingrole:
	true
<-
	.wait(500);
	!maintainingrole;
	.