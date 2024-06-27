This project was contributed by 4 members group of the Open University of Sri Lanka.
Splendy is an Android app that allows easier event management and connectivity. The app focused on two main parties attending the event: event organizers and attendees.

Splendy mobile app serves as a transformative solution to the 
challenges entrenched in traditional event management. 
For organizers, Splendy streamlines the event management process, offering features for 
creating, scheduling, managing, promoting, choosing the expected audiences, and efficiently 
communicating event details. Furthermore, the application facilitates timely notifications to 
participants, thereby eliminating confusion and enhancing overall event coordination.
Participants, on the other hand, benefit from a user-friendly interface that simplifies the 
discovery of events tailored to their interests. The app centralizes event information, providing 
participants with easy access to details, schedules, and any updates. The categorization of events 
as public or private ensures selective participation in private events, offering a personalized and 
secure experience. Moreover, the facility to contact through chats with the organizers will assist in 
resolving and clarifying any issues related with miscommunication of event information.

Functional Requirements
Functions
User Authentication and Access Control:
Description:
This functionality ensures secure access to the application and its features. Users will be 
required to authenticate themselves before accessing the app's functionalities. The system 
will implement access controls to restrict users' actions based on their roles and privileges.
Process Flow:
1. Users open the application and are presented with a login screen.
2. New users can sign up by providing their email address, a secure password.
3. Upon successful login, users are directed to the main dashboard, where they can 
access various functionalities based on their user role.

Event Creation
Description:
This functionality allows users to create new events by providing essential details. The user 
will fill out a form with information such as event type, date, venue, description, 
participants, sponsors, and more. They can also customize event settings, such as making 
the event public or private.
Process Flow:
1. The user accesses the "Create Event" feature from the app's main menu or bottom 
navigation bar event add function.
2. The app presents a form to the user, prompting them to enter event details.
3. The user fills out the required fields (e.g., event name, date, and time) and optional 
fields (e.g., description, sponsors).
4. The user selects whether the event should be public or private.
5. If the event is private, the user can add participants by searching for and inviting 
specific users.
6. The app validates the input to ensure that all required information is provided and 
in the correct format.
7. Once the user submits the form, the app stores the event details in the Firebase 
database.
8. If the event is public, it becomes visible in the centralized repository of publicly 
posted events.

Event Browsing
Description:
This functionality allows users to browse through a centralized repository of publicly 
posted events. Users can search for events based on various criteria and view detailed event 
descriptions, participant lists, and other relevant information.
Process Flow:
1. The user selects the "Browse Events" option from the app's main menu.
2. The app presents a list of publicly posted events, sorted by date or popularity.
3. The user can use filters to narrow down events based on event type, date, location, 
etc.
4. When the user selects an event from the list, the app displays detailed event 
information, including the event description, date, time, venue, participants, and 
sponsors.
5. The user can choose to request for the event or add it to their list of favorite events 
for future reference.

Reminders and Notifications
Description:
This functionality allows users to set reminders for upcoming events, ensuring that they do 
not miss any important activities. Users will receive reminders based on their preferences 
and receive event-related announcements.
Process Flow:
1. Users can enable notifications and set their preferences for event reminders in the 
app's settings.
2. When an event is created, the app stores the event details, including the date and 
time, in the Firebase database.
3. The app checks the user's reminder preferences and sends push notifications or in-app reminders to the user before the event's start time.
4. Users can receive event-related announcements, such as changes in event details, 
cancellations, or updates, through push notifications or in-app messages.

Personal Invitations
Description:
This functionality allows users to personally invite other users to participate in private 
events. Users can send invitations through the application, and recipients can accept or 
decline them according to preference. Only invitees will be able to read private event 
details.
Process Flow:
1. The event creator selects the option to make the event private during event creation.
2. The event creator enters the email addresses or usernames of the users they wish to 
invite to the private event.
3. The app sends invitation notifications to the selected users, informing them of the event 
and providing a link to the event details.
4. Recipients of the invitations can accept or decline the invitation through the app.
5. If a user accepts the invitation, the app adds them to the list of participants for the 
private event.
6. Users who decline the invitation won't have access to the private event's details.

Inquiries and Feedback
Description:
This functionality allows users to make inquiries about specific events or seek information 
directly through the application. The app also provides a feedback mechanism where users 
can share their opinions, suggestions, and ratings for attended events.
Process Flow:
1. Users can access the "Inquiries" section from the app's main menu to ask questions or 
seek information about specific events.
2. The app provides a form for users to fill out, including the event name and the nature 
of the inquiry.
3. When a user submits an inquiry, the app stores it in the Firebase database and notifies 
the event organizer or relevant personnel.
4. Users can access the "Feedback" section to share their opinions, suggestions, and 
ratings for events they have attended.
5. The app allows users to rate events on a scale or provide written feedback.
6. Feedback and ratings are stored in the database and may be made available to event 
organizers and other users in aggregated form.

Data Archival and Retention
User data are collected on login and registration processes. The login app will collect 
data to grant access privileges which the user who already registered to the system. If The user 
doesn’t have an account, he/she can make an account on the registration. In the registration, the app 
will collect some user data and store them in the database. The database will hold all the user data 
and the event data which organizers list through the app.

User Profiles, Roles, and Privileges
User Roles:
Individual Event Organizers: These users can create and manage their events, send 
invitations, and view event-specific data.
Organizations: Users representing organizations can create and manage events on behalf 
of the organization, invite participants, and view relevant data.
Event Participants: Users who have been invited to private events can view 
event details and interact with event-specific features.
User Access and Privileges:
Event Creation: Individual Event Organizers and Organizations have the privilege to create 
events.
Event Management: Event creators (Individual Organizers and Organizations) have the 
ability to manage their events, make updates, and cancel events.
Event Invitation: Event creators can send personalized invitations to participants for private 
events.
Event Viewing: All users can browse, and view publicly posted events. Participants invited 
to private events can access event details.

Reporting Requirements
Reports to Manage the Application:
1.Event List Report: A list of all publicly posted events, including event details, dates, and 
venues.
2.Event Participants Report: A report showing the list of participants for a specific event, 
along with their status (accepted/declined).
3.User Feedback Report: Feedback and ratings provided by users for attended events.
Display Pages:
1.Event Details Page: A page displaying detailed information about a specific event, 
including the description, date, time, venue, participants, and sponsors.
2.Event Search Results Page: This page presents a list of events based on user search 
criteria, allowing users to click on an event for more details.
3.Invitation Page: Users can view and respond to their event invitations on this page.
4.User Profile Page: Users can view and manage their profiles, including event-related 
information and preferences.
5.Event Creation Page: The page where users can create new events, as described in 
Function 1
