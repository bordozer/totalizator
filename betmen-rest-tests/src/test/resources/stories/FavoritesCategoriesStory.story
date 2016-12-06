Meta:
@category Favirite categories

Narrative:
As a user
I want to get category list
So that I can add some of them to favorites

Scenario: admin creates some categories and user retreviews them

Given The system has no categories
When New user registered and logged in
Then User requests categories and gets an empty list

When Admin logged in
And Creates one sport with two categories, another one with three ones and another one without ones
And New user registered and logged in
And User1 adds two categories to favorites
Then User1 requests categories and gets not empty list of system categories

When New user registered and logged in
And User2 adds two categories to favorites
Then User2 requests categories and gets not empty list of system categories