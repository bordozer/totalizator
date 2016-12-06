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

When User User1 logged in
Then User1 does not see any games on portal page

When Admin logged in
And Admin creates categories and teams
And User User1 logged in
Then User1 still does not see any games on portal page

When Admin logged in
And Admin creates cups
And User User1 logged in
Then User1 still does not see any games on portal page

When Admin logged in
And Admin creates games on Date before
And User User1 logged in
Then User1 still does not see any games on portal page

When User User1 logged in
And User1 defines one favorite category
Then User1 still does not see any games on portal page but can see favorite category cup statistic

When Admin logged in
And Admin creates games on Date after
And User User1 logged in
Then User1 can see only favorite category statistics

When Admin logged in
And Admin adds one game on Portal Page Date in User1's favorite category
And User User1 logged in
Then User1 can see the one cup of favorite category and cup's short statistics on Portal page

When Admin logged in
And Admin adds one more game on Portal Page Date in User1's favorite category
And User User1 logged in
Then User1 can see the one cup of favorite category and cup's short statistics on Portal page

When Admin logged in
And Admin adds one more game on Portal Page Date in category not favorite of User1
And User User1 logged in
Then User1 can see the one cup of favorite category and cup's short statistics on Portal page and one cup in Another section

When Another user User2 logged in
Then User2 does not see cups in match section and cup statistics section

When Admin logged in
And Admin adds games on Portal Page Date in private cup of favorite category
And User User1 logged in
Then User1 can see the one cup of favorite category and cup's short statistics on Portal page and one cup in Another section

When Admin logged in
And Admin adds games on Portal Page Date in private cup of NOT favorite category
And User User1 logged in
Then User1 can see the one cup of favorite category and cup's short statistics on Portal page and one cup in Another section

When Admin logged in
And Admin adds games on Portal Page Date in another public cup
And User User1 logged in
Then User1 can see the one cup of favorite category and cup's short statistics on Portal page and two cups in Another section

When User User1 adds another category to favorites
Then User1 can see the one cup of favorite category and cup's short statistics on Portal page and one cup in Another section

When Admin logged in
And Admin finish current cup from favorite category
And User User1 logged in
Then User1 can not see finished cup in statistics section and can see it in favorite categories cups section
