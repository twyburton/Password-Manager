@ECHO OFF

:: Script to open password manager
set "pass_directory=D:\Documents\Projects\PasswordManager\Password-Manager"

d:
cd pass_directory

if exist BasicPasswordManagerUpdate.jar (
    move BasicPasswordManagerUpdate.jar BasicPasswordManager.jar
	
	C:\Windows\System32\WindowsPowerShell\v1.0\powershell.exe java -jar BasicPasswordManager.jar -updated
) else (
	C:\Windows\System32\WindowsPowerShell\v1.0\powershell.exe java -jar BasicPasswordManager.jar
)




