IF "%XAMPP_HTDOCS%" == "" GOTO NO_VARIABLE
:YES_VARIABLE
del /s /q %XAMPP_HTDOCS%\*
xcopy /s . %XAMPP_HTDOCS%
del %XAMPP_HTDOCS%\autodeploy.bat
GOTO END
:NO_VARIABLE
@ECHO XAMPP_HTDOCS environment variable was NOT detected.
GOTO END
:END
