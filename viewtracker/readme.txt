Instructions:
1. Together with this file I am attaching a zip file, please unzip it in the directory of your preference.
2. After unzip it, run commands listed below
    - cd /path_to_view_tracker
    - mvn clean compile
    - mvn exec:java -Dexec.mainClass="com.viewtracker.app.ViewTrackerApplication" -Dexec.args="server configuration.yml"

The program provides one rest interface with two methods
   -POST: /viewtracker
    Description: Add a new view entry using the viewedOn time provided
    Content type: application/json
    Input format: {"userId":5,"viewerId":9,"viewedOn":1409296931051}
    Output: None

   -GET:/viewtracker/{userId}
    Description: Retrieves a list containing a maximum of 10 profiles views in the last 10 days to the provided user id profile
    Content type: application/json
    Input format: Path variable user id
    Output: None:[{"userId":5,"viewerId":9,"viewedOn":1409296931051}]

About database:
   -Database schema consists of a single table. I've decided not to add a profile table, since it is not clearly stated
   that the system should perform any validation of profile, but I think it can be added easily.
   -View table consists of three columns userId, viewerId and viewedOn. I haven't added any primary key because I do not
   consider that at this stage the table requires one. I've created an index on userId to facilitate search on it.
   -In a production environment I would consider to have two different databases. A main data source that would have
   data of the last 10 days and an archive one that would store older data. It is still open to discuss how archive data
   format would look like (it can be same format or for example an aggregation of data that can be used for analysis later on).
   -I'd partition the view table by viewedOn date so it would be easier to delete/move those partitions when data exceed
   the age requirement.

