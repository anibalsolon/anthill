{ include("common-ant.asl") }

/* Initial beliefs and rules */
max_age(80).

/* Initial goals */
!life.

+!reproducting:
	true
<-
	!go_outside;
	!invoke_males;
	!match_flight;
	!place_larvae;
//	.create_agent(Worker, "worker.asl");
	!reproducting;
	.
	
+!go_outside:
	true
<-
	!move(location(0, _, _));
	.
	
+!invoke_males:
	true
<-
	.print("Call them!");
	.
	
+!match_flight:
	true
<-
	.print("Match!");
	// TODO kill male?
	.

+!place_larvae:
	true
<-
	.create_agent("larva", "larva.asl");
	.