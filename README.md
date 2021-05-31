# ExpenditureManagement


**INTRODUCTION**

ExpenditureManagement is a project in partial fulfillment of the requirements of LBYCPD2 EQ1 under Engr. Dino Ligutan. The project is developed Yuan Patrick E. Dumandan, Ivan Daniel C. Porcincula, and Arthur Kenji T. Yoro II. They are students of Engr. Dino Ligutan in De La Salle University-Manila.

The main product of the project is the Monrec software, the name is in short of Money Recorder, which is essentially the essence of the software. The software aims to aid the user with their finances, giving them the ability to input and keep track of their savings, expenses, and income.

The developers made us of Java as their programming language of choice. The code is collaboratively done between the three developers using IntelliJ plugins to their advantage, such as Code With Me which allowed the developers to code together in real time.


**REQUIREMENTS OR INDEPENDENCIES USED**

IntelliJ IDEA

AWS Toolkit

MySQL Workbench

AWS MySQL Free Tier

JavaFX

JavaFX SDK Library

MySQL Connector Java

Scene Builder


**ENVIRONMENT SETUP**

Setting-up IntelliJ IDEA project with JavaFX:
1. Create a new project as JavaFX Application
2. Download JavaFX SDK Library on: https://gluonhq.com/products/javafx/
3. Unzip the JavaFX SDK Library
4. In IntelliJ IDEA go to File > Project Structure > Libraries > Press + icon > Java
5. Locate the folder where the JavaFX SDK Library is extracted. 
6. Inside the folder "javafx-sdk-versionnumber" locate the "lib" folder.
7. Press that folder and on IntelliJ IDEA, press the OK button.
8. Then press Apply button and then OK button
9. If problem persists, add an argument in VM options by editing the configuration. The steps can be followed in this link, see VM Options section: https://www.jetbrains.com/help/idea/javafx.html#vm-options

Setting up AWS RDS and MySQL
For the steps please see: https://aws.amazon.com/getting-started/hands-on/create-mysql-db/

Additional files to access the database through IntelliJ IDEA:
1. Download the MySQL Connector in this link: https://dev.mysql.com/downloads/connector/j/
2. Here, press the drop-down button and choose Platform Independent
3. It's up to the user which one would they download, though the .zip file is recommended.
4. After downloading, extract the zip file.
5. Copy the folder extracted from the zip file and place it inside the IntelliJ IDEA Project. (Same directory as the src folder)
6. In IntelliJ IDEA, go to File > Project Structure > Modules > Dependencies > Press + icon > JARs or Directories
7. Locate the folder of the MySQL Connector and click it once, then press OK.
8. Then press Apply and OK.

Scene Builder can be downloaded here: https://gluonhq.com/products/scene-builder/

**REVISION LOGS AND INFORMATION ABOUT CONTRIBUTORS**


<4/19/2021| v0.1 beta> Added codes for the login, register, with corresponding GUI code for the Dashboard.
<5/03/2021| v0.2 beta> Added income manager and update create account GUI.
<5/20/2021| v1.0> Added codes to add to database for income tracker and expenses tracker, Dashboard backend update, Added income manager and expenses manager GUI, Edited side menu
<5/21/2021| v1.1> Updated GUI for login, dashboard, account creation, income manager, expenses manager, Backend update on monthly income and expenses and user budget, Updated monthly reset of user goals etc., Backend update on monthly income and expenses and user budget.
<5/22/2021| v1.2> UPDATED DATABASE WHEN DELETING, ADDED DELETE FUNCTION ON INCOME AND EXPENSE, Update on query statements of income, expenses, budget left, Update on GUI Display, Proper integration of back end to front end of the software, Updated monthly automatic reset
<5/23/2021| v2.0> User can now fully use the Side Menu to navigate between different screens, User can now edit both of their income and expenses
<5/24/2021| v2.1> User can now edit and save their target savings monthly. The system will provide the requested target savings for daily and weekly. Added add button in income tracker and expenses tracker (the user can now add income and expenses while in income tracker and expenses tracker GUI), Updated monthly savings logical process.
<5/30/2021| v3.0> Daily and Weekly savings goal now update every time an expense or income is added/removed/edited, Added Statistical Report Interface.
<5/31/2021| v3.1> Added Comprehensive View in StatisticalReport.java (functional), Added buttons to the GUI of StatisticalReport


Yuan Patrick E Dumandan - currently taking up BS Computer Engineering in De La Salle University-Manila. He completed his Secondary Education in Marist School Marikina.

Ivan Daniel C. Porcincula - currently taking up BS Computer Engineering in De La Salle University-Manila. He completed his Secondary Education in the University of Santo Tomas Manila.

Arthur Kenji T. Yoro II - currently taking up BS Computer Engineering in De La Salle University-Manila. He completed his Secondary Education in Claret School of Quezon City.
