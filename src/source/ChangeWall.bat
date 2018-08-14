echo Executing GoogleWallpaper...

rem change to directory of batch file
cd /d %~dp0

java -cp .;json.jar;jna-4.1.0.jar;jna-platform-4.1.0.jar ChangeWallpaper

echo Finished.
pause

