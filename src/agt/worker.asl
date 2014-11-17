{ include("common-ant.asl") }

/* Initial beliefs and rules */
max_age(1800).

/* Initial goals */
!life.

+!role_decision
	: location(CurrLevel, X, Y) & not lvlknow(CurrLevel, _, _)
<-
	.wait(500);
	!role_decision;
	.
	
+!role_decision
	: true
<-
	.print("Adopting role");
	adoptRole("pioneer");
	.
		
+!pioneering
	: true
<-
	!go_outside;
	!look_for_food;
	!extract_food;
	!bring_to_anthill;
	!pioneering;
	.
	
+!go_outside
	: true
<-
	!move(location(0, _, _));
	.
	
+!look_for_food
	: location(CurrLevel, X, Y) & lvlknow(CurrLevel, X, Y, "STATE", State) & State == "FOOD"
<-
	.print("Food!");
	.
	
+!look_for_food
	: true
<-
	!move_random;
	!look_for_food;
	.
	
+!extract_food
	: true
<-
	.print("Extracting");
	//.wait(1000);
	.
	
+!bring_to_anthill
	: true
<-

//	!calculate_steps(Steps);
//	.print("Found in ", Steps);
//	anthill.actions.optimizepath(Opt);
	
	-path(_, _);
//	for( .member(Loc, Opt) ) {
//		+Loc;
//	}

//	!calculate_steps(Steps);
//	.print("Optimized to ", Steps);
	
	!pheromone;
	!return
	
	.
	
//+!calculate_steps(Steps)
//	: true
//<-
//	.findall(Id, path(Id, _), Ids);
//	if(not .empty(Ids)){
//		.max(Ids, Steps);
//	}
//	.

+!return
	: found & location(CurrLevel, X, Y) & lvlknow(CurrLevel, X, Y, "STATE", State) & State == "HOLE_DOWN"
<-
	-path(_, _);
	.print("Home!");
	.print("Im in ", State);
	!pheromone;
	!down;
	.

+!return
	: found & location(CurrLevel, X, Y) & lvlknow(CurrLevel, X, Y, "STATE", State) & State \== "HOLE_DOWN"
<-
	+found;	
	.print("Im in ", State);
	!move_random;
	!pheromone;
	.

+!return
	: not found
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
	
//+!return
//	: .findall(Id, path(Id, _), Ids) & .max(Ids, MId) & path(MId, location(Level, X, Y)) & lvlknow(Level, X, Y, "STATE", State) & State == "GROUND"
//<-
//	.print(Ids);
//	-path(MId, _);
//	!move(location(Level, X, Y));
//	!pheromone;
//	!return
//	.
//	
//+!return
//	: .findall(Id, path(Id, _), Ids) & .max(Ids, MId) & path(MId, location(Level, X, Y)) & lvlknow(Level, X, Y, "STATE", State) & State == "HOLE_DOWN"
//<-
//	.print("Going down");
//	.print(MId);
//	-path(_, _);
//	!move(location(Level, X, Y));
//	!pheromone;
//	!return
//	.
//	
//+!return
//	: not path(_, _)
//<-
//	!down
//	.
	
+!pheromone
	: true
<-
	pheromone(1.0) [artifact_id(Anthill)];
	.