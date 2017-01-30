Android is based on a model-view-controller framework. This project clearly shows the three components.
The View class is the basic foundation for the UI components of Android. A ViewGroup is a type of View that allows other "children" views. LinearLayout belongs to the ViewGroup, and can arrange its children in a horizontal row or vertical column. On the other hand, RelativeLayout needs to specify how the children's layout relate to each other. 
In this project, read through the following files:
1) res/activity_main.xml: Notice the EditTexts, TextViews and Button used in different LinearLayout, particularly the text and id values. Text values are defined in res/values/strings.xml
2) MainActivity.java: saveToDb, as well as how the data source is created in a context, opened, and closed and how the values are added into the table.
3) MyDataSource.java: contains the factory methods for connecting to the physical data source on the phone. TODO: Complete the getAllRecords method and use Log.d commands to print out all the records, for now.
4) MySQLiteHelper.java: a helper class to manage database creation and versioning
5) StudentDemo.java : the object representing each record in the table. 
Notice 1 corresponds to the view, 5 to the model and 2-4 are the controller files.
SQL Tutorial:
Structured Query Language (SQL) is the language to access and manipulate databases. The following are the different SQL commands:
SELECT - extracts data from a database
UPDATE - updates data in a database
DELETE - deletes data from a database
INSERT INTO - inserts new data into a database
CREATE DATABASE - creates a new database
ALTER DATABASE - modifies a database
CREATE TABLE - creates a new table
ALTER TABLE - modifies a table
DROP TABLE - deletes a table
CREATE INDEX - creates an index (search key)
DROP INDEX - deletes an index
