# Java written recommendation system

This project is a prototype, to allow me discover recommendation systems.

It is currently in his first step : a memory written recommendation system. You have no fancy technologies yet,
 no complicated databases, nor advanced computation systems. The project for the moment is just about testing the algorithm.

It also has the goal of testing the **CQRS** pattern. 

What is it like to evolve in ?

I will write a **Cassandra** backend to store data, write the event system over **Kafka** and I am planning of using **Storm** to handle
 heavy calculations.

## About the recommendation system ?

The recommendation system I built is a pure collaborative filtering approach. Hence it is limited ( Slow start problem ).

It is based on the assumption that you will be friend with people that are already friend of your friends. The most you
 have friends in common with someone, the most this people is likely of becoming your friend.

This quite basic recommendation system is based on a score ( between 0 and 1 ). 1 means really likely recommendation ,
 whereas 0 means a very unlikely recommendation.

To build this score, we simply calculate the intersection cardinal of the friendship direct network of two users, divided by 
 the union cardinal of the same two ensembles.

What you can notice is that the recommendation result is symmetric (same value). We can calculate it once, and use it for the two
 users, saving extra calculation. This is extremely important, as it means we can recalculate recommendations only for users
 who changed their friendship network. Consequences of these actions will be applied on other users recommendations using this
 symmetry property.

Now comes CQRS. We want to be as fast as possible to answer user action ( for instance become friend ), but near real time 
 is acceptable for updating friend recommendation ( eg : 10 seconds ). So we can uncouple the recommendation engine from 
 the CRUD operation of the user (as it is slow). I implemented an event system, and friendship related are emitted threw it.
 You then have an other component that is responsible of updating recommendations.

This project is the implementation of a recommendation system similar to Amazon one ( people who buy this object also bought... ).