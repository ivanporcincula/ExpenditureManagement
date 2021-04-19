# ExpenditureManagement


**INTRODUCTION**

ExpenditureManagement is a project in partial fulfillment of the requirements of LBYCPD2 EQ1 under Engr. Dino Ligutan. The project is developed Yuan Patrick E. Dumandan, Ivan Daniel C. Porcincula, and Arthur Kenji T. Yoro II. They are students of Engr. Dino Ligutan in De La Salle University-Manila.

The main product of the project is the Monrec software, the name is in short of Money Recorder, which is essentially the essence of the software. The software aims to aid the user with their finances, giving them the ability to input and keep track of their savings, expenses, and income.

The developers made us of Java as their programming language of choice. The code is collaboratively done between the three developers using IntelliJ plugins to their advantage, such as Code With Me which allowed the developers to code together in real time.


**REQUIREMENTS OR INDEPENDENCIES USED**

-IntelliJ IDEA
-AWS Toolkit
-MySQL Workbench
-AWS MySQL Free Tier
-JavaFX
-JavaFX SDK Library
-MySQL Connector Java
-Scene Builder


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

**REVISION LOGS AND INFORMATION ABOUT CONTRIBUTORS**

<4/19/2021| v0.1 beta> Added codes for the login, register, with corresponding GUI code for the Dashboard.

Yuan Patrick E Dumandan - currently taking up BS Computer Engineering in De La Salle University-Manila

Ivan Daniel C. Porcincula - currently taking up BS Computer Engineering in De La Salle University-Manila

Arthur Kenji T. Yoro II - currently taking up BS Computer Engineering in De La Salle University-Manila
