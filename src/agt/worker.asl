{ include("common-ant.asl") }

/* Initial beliefs and rules */
max_age(1800).

/* Initial goals */
!life.

+!foodpicking:
	location(CurrLevel, X, Y) & lvlknow(CurrLevel, W, H)
<-
	.print("Foodpicking!");
	!go_outside;
	!look_for_food;
	!extract_food;
	!bring_to_anthill;
	!foodpicking;
	.

+!foodpicking:
	true
<-
	.wait(500);
	!foodpicking;
	.	
		
	
+!go_outside:
	true
<-
	!move(location(0, _, _));
	.
	
+!look_for_food:
	location(CurrLevel, X, Y) & lvlknow(CurrLevel, X, Y, "STATE", State) & State == "FOOD"
<-
	.print("Food!");
	.
	
+!look_for_food:
	true
<-
	!move_random;
	!look_for_food;
	.
	
+!extract_food:
	true
<-
	.print("Extracting...");
	+food(10);
	anthill.actions.wait(0.8);
	.
	
+!bring_to_anthill:
	lvlknow(Level, X, Y, "STATE", "SUGAR")
<-

	.print("Bringing...");
	
	-path(_, _);
	
	!pheromone;
	!return;
	!move(location(Level, X, Y));
	.

+!return:
	found & location(CurrLevel, X, Y) & lvlknow(CurrLevel, X, Y, "STATE", State) & State == "HOLE_DOWN"
<-
	-path(_, _);
	!pheromone;
	!down;
	.

+!return:
	found & location(CurrLevel, X, Y) & lvlknow(CurrLevel, X, Y, "STATE", State) & State \== "HOLE_DOWN"
<-
	+found;
	!move_random;
	!pheromone;
	!return;
	.

+!return:
	not found
<-
	-path(_, _);
	anthill.actions.findclosest("HOLE_DOWN", Locs);
	for( .member(Loc, Locs) ) {
		!move(Loc);
		!pheromone;
	}	
	+found;
	!down;
	.
	
+!pheromone:
	true
<-
	pheromone(1.0) [artifact_id(Anthill)];
	.