sugar(0).
max_carry(10).

+!gardening:
	lvlknow(_, _, _, "STATE", "FUNGUS") & lvlknow(_, _, _, "STATE", "SUGAR")
<-
	.print("Gardening!");
	!grow_fungus;
	!gardening;
	.
	
+!gardening:
	true
<-
	.wait(500);
	!gardening;
	.

+!grow_fungus:
	true
<-
	!check_fungus;
	!feed_fungus;
	.
	
+!check_fungus:
	lvlknow(Level, X, Y, "STATE", "FUNGUS")
<-
	!move(location(Level, X, Y));
	// TODO really check this
	!collect_fungus;
	.
	
+!collect_fungus:
	true
<-
	anthill.actions.wait(0.8);
	.
	
+!feed_fungus:
	true
<-
	!collect_sugar;
	!bring_to_fungus;
	.
	
+!collect_sugar:
	lvlknow(Level, X, Y, "STATE", "SUGAR")
<-

	!move(location(Level, X, Y));
	
	anthill.actions.wait(0.4);
	
	?sugar(Sugar);
	-sugar(_);
	
	?max_carry(Max);
	
	// TODO remove from sugar artifact
	
	.random(AmountCollected);
	AmountCollected = .floor(AmountCollected * 10);
	
	if( Sugar + AmountCollected > Max ){
		+sugar(Max);
	} else {
		+sugar(Sugar + AmountCollected);
	}
	
	.
	
+!bring_to_fungus:
	sugar(Amount) & Amount == 0
<-
	!collect_sugar;	
	.

+!bring_to_fungus:
	lvlknow(Level, X, Y, "STATE", "FUNGUS")
<-
	!move(location(Level, X, Y));
	// TODO add to fungus artifact
	-sugar(_);
	.