# How to run the PMS project in your local machine

### 1. Clone the project to your folder
```
your-folder $ git clone https://gitlab.infoteach.ca/hongg/ca.bcit.comp4911.bugbuster.pms.git
```

### 2. Check out to "development" branch
```
your-folder $ cd ca.bcit.comp4911.bugbuster.pms
ca.bcit.comp4911.bugbuster.pms $ git checkout development
```

### 3. Run the content of "pms.sql" file in your local MySQL
- Location: **/src/main/resources/pms.sql**
- Make sure the database "pms" is created in your local MySQL

### 4. Set up local Wildfly
1. **JDBC Driver** and **Wildfly Server** must be installed
2. Turn on Wildfly Server in the installed wildfly folder
```
wildfly-27.0.0.Beta1 $ ./bin/standalone.sh
```
3. In your browser, open up **localhost:8080**
4. Click **Administration Console** (You need wildfly admin account to access admin console)
5. In **Deployments** tab, deploy **MySQL connector/J** driver
6. In **Configuration >> Subsystems >> Datasources & Drivers >> Datasources**, create a datasource
- JNDI name: **java:jboss/datasources/pms**
- Connection URL: **jdbc:mysql://localhost/pms?serverTimezone=America/Vancouver**
- Username: **bugbuster**
- Password: **bugbuster**
7. Test the connection

### 5. Run the project

**Windows Users:**
- Navigate to the root directory and run the following file with the same user and password you used for MySQL

```
start.bat
```

**Mac Users:**

```
ca.bcit.comp4911.bugbuster.pms $ mvn clean package
ca.bcit.comp4911.bugbuster.pms $ MYSQL_DATABASE=pms MYSQL_USER=bugbuster MYSQL_PASSWORD=bugbuster MYSQL_DATASOURCE=pms target/server/bin/standalone.sh

```

- If you succeed, open up the app **http://localhost:8080/pms**
