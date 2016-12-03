Meta:
@category Portal Page

Title User portal page games

Narrative:
As a regular user
I want add leagues to favorites
So that get their games on portal page

Scenario: Portal page cups flow

Given user1 is registered user
And user2 is registered user

When User1 logged in
Then User1 does not see any games on portal page

When Admin logged in
And Admin creates categories and teams
And User1 logged in
Then User1 still does not see any games on portal page

When Admin logged in
And Admin creates cups
And User1 logged in
Then User1 still does not see any games on portal page

When User1 logged in
And User1 defines some favorite categories
Then User1 still does not see any games on portal page

When Admin logged in
And Admin creates games on Date before
And User1 logged in
Then User1 still does not see any games on portal page
