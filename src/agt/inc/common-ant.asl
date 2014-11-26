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
	
+!waitLevel: lvlknow(false) | not lvlknow(_) <- .print("Waiting level"); .wait(800); !waitLevel; .
+!waitLevel: lvlknow(true) <- .wait(800); .

+location(Level, X, Y)[source(A)]:
	A \== self
<-
	-location(_, _, _);
	+location(Level, X, Y);
	.print("I am at ", location(Level, X, Y));
	.
	
+location(Level, X, Y)[source(A)]:
	A == self
<-
	anthill.actions.position(location(Level, X, Y))
	.
	
+lvlknow(Init)[source(A)]:
	A \== self
<-
	-lvlknow(_);
	if(not Init){
		-lvlknow(_, _, _, _, _);
	}
	+lvlknow(Init);
	.
	
+lvlknow(Level, W, H)[source(A)]:
	A \== self & location(CurrLevel, _, _) & CurrLevel == Level
<-
//	.print(lvlknow(Level, W, H));
	+lvlknow(Level, W, H);
	anthill.actions.knowledge(Level, W, H);
	.
	
+lvlknow(Level, X, Y, Type, State)[source(A)]:
	A \== self & location(CurrLevel, _, _) & CurrLevel == Level & not lvlknow(Level, _, _)
<-
	+lvlknow(Level, X, Y, Type, State)[source(A)];
	.print("Waiting for level info");
	.wait(500);
	.

+lvlknow(Level, X, Y, Type, State)[source(A)]:
	A \== self & location(CurrLevel, _, _) & CurrLevel == Level 
<-
//	.print(lvlknow(Level, X, Y, Type, State));
	+lvlknow(Level, X, Y, Type, State);
	anthill.actions.knowledge(Level, X, Y, Type, State);
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