@echo off
title SmartDashboard Debugger

:start
echo Starting SmartDashboard...
echo Press Ctrl+C to quit
echo.
java -jar SmartDashboard.jar
echo.
echo Press any key to restart SmartDashboard.
pause
cls
goto start
