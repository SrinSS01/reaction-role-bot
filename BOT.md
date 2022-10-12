# How to create a bot application
## get started

- Open [discord developer portal](https://discord.com/developers/applications)
- This should open something like this  
   ![dev1](/img/dev1.png)
   _you might have to log-in if not logged in already_
- Next click on `New Application`  
   ![dev2](/img/dev2.png)
   _provide a name of your choice, then click create_
- This should open something like this 
   ![dev3](/img/dev3.png)
   _select `Bot` from the sidebar_
- Then click on `Add bot`
  ![dev4](/img/dev4.png)
- This will create a bot application
   ![dev5](/img/dev5.png)
- Now click on `Reset Token` to generate a new token for the bot  
  this token will be required later to start the bot  
  so copy and save it somewhere else
   ![dev6token](/img/dev6token.png)
- Next scroll down to the `Privileged Gateway Intents` section  
  and enable `PRESENCE INTENT` and `SERVER MEMBER INTENT` then save the changes
   ![dev6](/img/dev6.png)
- Next click on `Url Generator` under `OAuth2` from the sidebar
   ![dev7](/img/dev7.png)
- From the check-boxes in the right click on `bot` and `application.commands`
   ![dev8](/img/dev8.png)
- Next scroll down to `BOT PERMISSIONS` section and select the required permissions for the bot  
  **NOTE: The following permissions must be selected**
  1. `Manage Roles`
  2. `Send Messages`
  3. `Use Slash Commands`
   ![dev9](/img/dev9.png)
- Next Scroll down a bit and you'll see an url being generated  
  copy and paste it in a new tab in your browser
   ![dev10](/img/dev10url.png)
- This will show the following
   ![dev11](/img/dev11.png)
   _select a server from the option and click continue_  
   There might be some verification steps.  
   Then the bot application will be added to the server you selected