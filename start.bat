SET MYSQL_DATABASE=pms

@REM Change user and password accordingly
SET MYSQL_USER=root
SET MYSQL_PASSWORD=password

SET MYSQL_DATASOURCE=pms

call mvn clean package
call target\server\bin\standalone.bat
