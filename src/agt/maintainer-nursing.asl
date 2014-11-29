sugar(0).
max_carry(10).

+!nursing:
	lvlknow(_, _, _, "STATE", "FUNGUS") & lvlknow(_, _, _, "STATE", "LARVAE")
<-
	.print("Gardening!");
	!grow_larvae;
	!nursing;
	.
	
+!nursing:
	true
<-
	.wait(500);
	!nursing;
	.

+!grow_larvae:
	true
<-
	!check_larvae;
	!bring_fungus_to_larvae;
	.
	
+!checkLarvae:
	lvlknow(Level, X, Y, "STATE", "LARVAE")
<-
	!move(location(Level, X, Y));
	// TODO really check this
	!bornLarva
	.
	
+!bornLarva:
	true
<-
	true.
	
+!bring_fungus_to_larvae:
	lvlknow(Level, X, Y, "STATE", "FUNGUS")
<-

	!move(location(Level, X, Y));
	
	anthill.actions.wait(0.4);
	
	?fungus(Fungus);
	-fungus(_);
	
	?max_carry(Max);
	
	// TODO remove from fungus artifact
	
	.random(AmountCollected);
	AmountCollected = .floor(AmountCollected * 10);
	
	if( Fungus + AmountCollected > Max ){
		+sugar(Max);
	} else {
		+sugar(Fungus + AmountCollected);
	}
	.
	