@echo off
:start
echo Starting SmartDashboard...
echo.
java -jar SmartDashboard.jar
echo.
echo Press any key to restart SmartDashboard.
pause
cls
goto start
