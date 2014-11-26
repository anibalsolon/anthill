{ include("common-ant.asl") }

/* Initial beliefs and rules */
max_age(15).
timetoblossom(5).

food_req(worker, 10).
food_req(maintainer, 10).
food_req(wingedmale, 10).

class(worker).
food(5).

/* Initial goals */
!life.

+age(Age):
	timetoblossom(N) & N < Age & class(Class) & food_req(Class, Food) & food(ReceivedFood) & ReceivedFood >= Food 
<-
	.print("Blossom!");
	.create_agent(Worker, "worker.asl");
	!die;
	.
	
+age(Age):
	timetoblossom(N) & N < Age & class(Class) & food_req(Class, Food) & food(ReceivedFood) & ReceivedFood < Food 
<-
	.print("Just dying! More food needed (just ", Food, ").");
	!die;
	.
	
+food(Quantity)[source(A)]:
	A \== self & food(Current)
<-
	-food(_);
	+food(Current + Quantity);
	.print("Nhammy! Im with ", Current + Quantity, " of food.")
	.