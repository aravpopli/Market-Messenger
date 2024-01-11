**Test 1:** 
 1) User launches application.
 2) User selects whether they want to be a seller or customer. 
 3) User enters username via the keyboard.
 4) User selects the password textbox. 
 5) User selects the "Log in" button.

Expected result: Application verifies the user's username and password and loads their homepage automatically. 

Test Status: Passed. 

**Test 2:** 
 1) User has already created an account
 2) User tries logging in with known credentials

Expected result: Application verifies the user's username and password was correct and gets them into their account

Test Status: Passed. 

**Test 3:** 
 1) User has already created an account
 2) User tries logging in with known credentials
 3) User writes a message using the write message button
 4) User logs out
 5) Log into message recipients account
 6) Click view message
 7) Find the message sent

Expected result: Application populates the text message thread on recipient side 

Test Status: Passed. 

**Test 4:** 
 1) User has already created an account
 2) User tries logging in with known credentials
 3) User clicks export message thread
 4) Logs out to find exported csv

Expected result: Application populates a csv of text message thread in their directory

Test Status: Passed. 

**Test 5:** 
 1) User has already created an account
 2) User tries logging in with known credentials
 3) User clicks view statistics

Expected result: Application takes user through a series of simple GUI which represent the correct messaging stats regarding the user

Test Status: Passed. 






