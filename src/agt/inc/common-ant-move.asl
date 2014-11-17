+!move(location(LevelTo, XTo, YTo))
	: lvlknow(false) | not lvlknow(_)
<-
	.wait(300);
	!move(location(LevelTo, XTo, YTo));
	.

+!move(location(LevelTo, XTo, YTo))
	: location(LevelFrom, XFrom, YFrom) & LevelFrom == LevelTo &
	( ( XFrom == XTo & YFrom == YTo ) | not .ground(XTo) & not .ground(YTo) )
<-
	.print("Fuckin here :)");
	.

+!move(location(LevelTo, XTo, YTo))
	: location(LevelFrom, XFrom, YFrom) & LevelTo < LevelFrom
<-
	anthill.actions.findclosest("HOLE_UP", Locs);
	for( .member(Loc, Locs) ) {
		!move(Loc);
	}	
	!up;
	!move(location(LevelTo, XTo, YTo));
	.
	
+!move(location(LevelTo, XTo, YTo))
	: location(LevelFrom, XFrom, YFrom) & LevelTo > LevelFrom
<-
	anthill.actions.findclosest("HOLE_DOWN", Locs);
	for( .member(Loc, Locs) ) {
		!move(Loc);
	}	
	!down;	
	!move(location(LevelTo, XTo, YTo));
	.
	
+!move(location(LevelTo, XTo, YTo))
	: location(LevelFrom, XFrom, YFrom) & anthill.actions.is_neighbour(location(LevelFrom, XFrom, YFrom), location(LevelTo, XTo, YTo))
<-
	.print("Moving from ", location(LevelFrom, XFrom, YFrom), " to ", location(LevelTo, XTo, YTo));
	-location(_, _, _);
	+location(LevelTo, XTo, YTo);
	
	.wait(500);
	
	lookupArtifact("anthill", Anthill);
	move(XTo-XFrom, YTo-YFrom) [artifact_id(Anthill)];
	.	

+!move(location(LevelTo, XTo, YTo))
	: location(LevelFrom, XFrom, YFrom) & not anthill.actions.is_neighbour(location(LevelFrom, XFrom, YFrom), location(LevelTo, XTo, YTo))
<-
	anthill.actions.findclosest(location(LevelTo, XTo, YTo), Locs);
	for( .member(Loc, Locs) ) {
		!move(Loc);
	}
	!move(location(LevelTo, XTo, YTo));
	.
	
+!move_random
	: location(CurrLvl, CurrX, CurrY)
<-
	.findall(Id, path(Id, _), Ids);
	
	if(not .empty(Ids)){
		.max(Ids, Big);
		if(not .number(Big)){
			Big = -1;
		}
	} else {
		Big = -1;
	}
	
	+path(Big + 1, location(CurrLvl, CurrX, CurrY));

	anthill.actions.findnext(Loc);
	
	!move(Loc);
	.
	
+!move(location(LevelTo, XTo, YTo))
	: location(LevelFrom, XFrom, YFrom)
<-
	.print("I cant! I am at ", location(LevelFrom, XFrom, YFrom), " and cant go to ", location(LevelTo, XTo, YTo));
	.
	
+!move(location(LevelTo, _, _))
	: location(LevelFrom, XFrom, YFrom)
<-
	.print("I cant! I am at ", location(LevelFrom, _, _), " and cant go to ", location(LevelTo, XTo, YTo));
	.
	
+!up
	: location(CurrLevel, X, Y) & lvlknow(CurrLevel, X, Y, "STATE", State) & State == "HOLE_UP"
<-
	lookupArtifact("anthill", Anthill);
	up [artifact_id(Anthill)];
	.
	
+!down
	: location(CurrLevel, X, Y) & lvlknow(CurrLevel, X, Y, "STATE", State) & State == "HOLE_DOWN"
<-
	lookupArtifact("anthill", Anthill);
	down [artifact_id(Anthill)];
	.