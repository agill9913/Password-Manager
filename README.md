# Password Manager

## What will it do?

This application will save usernames, passwords and other credentials for websites for different user.

## Who Will use it?

People who have a lot of passwords to remember or who simply 
have trouble remembering will use it as an easy way to store this information

## Why is this project of interest to you

Cybersecurity has been something that I've been quite interested in recently. I took a Cybersecurity course at 
Langara previously so wanted to do something that could incorporate what I learned in that course with.

## User stories

*Phase 1*
- As a user, I want to be able to login in **securely** before I access my data
- As a user, I want to be able to add multiple sites and their corresponding data to my account **only**
- As a user, I want to be able to view what sites and data are saved to my account
- As a user, I want to be able to edit the site information saved to my account
- As a user, I want to be able to delete any credentials and sites that I have saved to my account
- As a user, I want to be able to have more than one account (for cases like family shared computers)

*Phase 2*
- As a user, I want to have my data save and encrypt when I log out
- As a user, I want to have my data load automatically and decrypt when I log in
- As a user, I want to be able to save my data to another file whenever
- As a user, I want to be able to load a different password manager file

*Phase 3*
- As a user, I want to be able to remove my account
- As a user, I want to see how many users are saved and the hashes

*Phase 4: Task 2*

Example of adding user, logging out, logging in to different user and then removing account:

Fri Nov 26 04:24:31 PST 2021
Creating user from previous account

Fri Nov 26 04:24:31 PST 2021
User added

Fri Nov 26 04:24:37 PST 2021
User logged in

Fri Nov 26 04:24:57 PST 2021
Adding data to site

Fri Nov 26 04:25:04 PST 2021
Editing existing data

Fri Nov 26 04:25:11 PST 2021
Removing Site

Fri Nov 26 04:25:18 PST 2021
Converting manager to JSON

Fri Nov 26 04:25:18 PST 2021
Converting account to JSON

Fri Nov 26 04:25:34 PST 2021
User removed

Fri Nov 26 04:28:32 PST 2021
Creating user from previous account

Fri Nov 26 04:28:32 PST 2021
User added

Fri Nov 26 04:28:37 PST 2021
Creating new user

Fri Nov 26 04:28:37 PST 2021
User added

Fri Nov 26 04:28:39 PST 2021
Converting manager to JSON

Fri Nov 26 04:28:39 PST 2021
Converting account to JSON

Fri Nov 26 04:28:39 PST 2021
Converting account to JSON

*Phase 4: Task  3*

- More robust with exceptions instead of if statements to check user input
- Adding some existing methods to DataOperations interface
- Use the singleton pattern in classes to make sure only one is created like with hashing