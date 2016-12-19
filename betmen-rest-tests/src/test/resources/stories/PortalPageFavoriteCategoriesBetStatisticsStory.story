Meta:
@category Portal Page

Title User portal page bets statistics on matches of favorite categories

Narrative:
As a user
I want to see statistics about made bets on the matches of the favorite categories to avoid missing bets

Scenario: Portal page bets flow

Given There are no games in the system

When Admin logged in
Then Admin is seeing an empty statistics

When Admin creates two games - yesterday's and tomorrow's
Then Admin is seeing an empty statistics

When Admin adds the category to favorites
Then Admin is seeing an empty statistics

When Admin creates one today's game
Then Admin is seeing the category in statistics with one game on date but no bet

When Admin makes bet on today's game
Then Admin is seeing the category in statistics with one game on date and one bet

When Admin creates another today's game
Then Admin is seeing the category in statistics with two game on date and one bet

When Admin makes bet on another today's game
Then Admin is seeing the category in statistics with two game on date and two bet



