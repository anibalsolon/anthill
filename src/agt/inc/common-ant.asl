{ include("$jacamoJar/templates/common-cartago.asl") }

/* Goals */
+!life
<-
	+age(0);
	
	.my_name(N);
	.concat("lifeisabitch_", N, A);
	
	makeArtifact( A , "anthill.LifeTickTack", [], Clock );
	focus( Clock );
	startLife;
		
	!!startReasoning;
	
	.

@deathIsNear [atomic]
+deathIsNear: age(N) & max_age(M)
  <-
	-+age( N + 1 );		
	.random( R );	
	if(M <  N * ( R + 0.5 )){
		.my_name(NAME);		
		// TODO place dead body		
		.kill_agent(NAME);		
	}
	
	.