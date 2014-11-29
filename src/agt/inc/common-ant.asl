{ include("common-cartago.asl") }
{ include("common-moise.asl") }
{ include("common-ant-move.asl") }

initial_roles(worker, picker).
initial_roles(queen, queen).
initial_roles(maintainer, maintainer).
initial_roles(wingedmale, male).

/* Goals */
+!life:
	true
<-
	+age(0);
	
	anthill.actions.my_type(Type);
	.print("I am ", Type);
	
	lookupArtifact("life", Clock);
	focus(Clock);
	startLife [artifact_id(Clock)];

	lookupArtifact("anthill", Anthill);
	focus(Anthill);
	
	if(Type \== larva){
		
		initAnt [artifact_id(Anthill)];
		
		?initial_roles(Type, Role);
		.print("I must assume ", Role);
		adoptRole(Role);
			
	}
	
	.print("Im alive!");
	.
	
+!die:
	true
<-
	lookupArtifact("life", Clock);
	stopFocus(Clock);

	lookupArtifact("anthill", Anthill);
	stopFocus(Anthill);
	
	anthill.actions.my_type(Type);
	if(Type \== larva){
		die [artifact_id(Anthill)];
	}

	.my_name(Name);
	.kill_agent(Name);
	.

waiting(Level) :- not lvlknow(Level, _, _).

+location(Level, X, Y)[source(A)]:
	A \== self
<-
	-location(_, _, _);
	+location(Level, X, Y);
	.
	
+location(Level, X, Y)[source(A)]:
	A == self
<-
	anthill.actions.position(location(Level, X, Y))
	.
	
// Wait for current location	
+lvlknow(Level, W, H)[source(A)]:
	A \== self & not location(CurrLevel, _, _)
<-
	+lvlknow(Level, W, H)[source(A)];
	.wait(1000);
	.	
	
// Level size
+lvlknow(Level, W, H)[source(A)]:
	A \== self & location(CurrLevel, _, _) & CurrLevel == Level
<-
	if(anthill.actions.knowledge(Level, W, H)){
		.print("Level ", Level, " is ", W, "x", H);
		+lvlknow(Level, W, H);
	} else {
		+lvlknow(Level, W, H)[source(A)];
	}
	.wait(1000);
	.

// Wait for level size
+lvlknow(Level, X, Y, Type, State)[source(A)]:
	A \== self & location(CurrLevel, _, _) & CurrLevel == Level & not lvlknow(Level, _, _)
<-
	+lvlknow(Level, X, Y, Type, State)[source(A)];
	.wait(1000);
	.

// Location info (type and pheromones)
+lvlknow(Level, X, Y, Type, State)[source(A)]:
	A \== self & location(CurrLevel, _, _) & CurrLevel == Level & lvlknow(Level, W, H)
<-
	if(anthill.actions.knowledge(Level, X, Y, Type, State)){
		+lvlknow(Level, X, Y, Type, State);
	} else {
		+lvlknow(Level, X, Y, Type, State)[source(A)];
	}
	.wait(1000);
	.
	
// Landmarks
+lvlknow(Level, X, Y, Type, State)[source(A)]:
	A \== self & anthill.actions.is_landmark(State)
<-
	+lvlknow(Level, X, Y, Type, State);
	.
	
+tick(WorldAge):
	age(N) & max_age(M)
<-
	-age(_);
	+age( N + 1 );

	.random( R );

	if(M < N * ( R + 0.5 )){
		!die;
	}
	.