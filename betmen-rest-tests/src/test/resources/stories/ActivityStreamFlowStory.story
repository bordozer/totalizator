Meta:
@category Activity syteam

Narrative:
As a user
I want to perform different actions
So that I can be logged in Activity stream

Scenario: activity stream flow

Given User1 is registered user
And User2 is another registered user

Given Admin cleanup system and creates a match
When User User1 logged in the system
And User User1 makes a bet
Then User User1 is seeing the bet activity stream item with his bet details

When Another user User2 logged in the system
Then Another user User2 is seeing the bet activity stream item without bet details

When User was thinking a couple of seconds
And Another user User2 makes a bet
Then Another user User2 is seeing the bet activity stream item with his bet details

When User was thinking a couple of seconds
And Another user User2 change his bet
Then Another user User2 is seeing the a newly created bet activity with his bet details

When User User1 logged in the system
Then User User1 is seeing the change bet activity of User2 still without bet details

When Admin logged in
And Admin was thinking a couple of seconds
And Admin finished the match with User1 score
Then Admin is seeing match finished activity in match card
And Match finish activity is not shown on portal page and in user cards

When User User1 logged in the system
Then User1 is seeing match finished activity in match card
And User User1 is seeing his bet activity with bet details and match points
And User User1 is seeing User2 original bet activity with bet details and match points
And User User1 is seeing User2 changed bet activity with bet details and match points


