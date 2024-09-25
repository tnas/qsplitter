# Set-ExecutionPolicy -ExecutionPolicy RemoteSigned

$java = "D:\Java\jdk-21.0.4\bin\java.exe"
$jar = "F:\Projects\qsplitter\target\qsplitter-0.0.1-SNAPSHOT.jar"

$functionIds = @(0, 1, 2, 3, 4, 5)
$numOfIds = @(1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 
 			  10000, 20000, 30000, 40000, 50000, 60000, 70000, 80000, 90000, 100000)
			  
foreach ($fId in $functionIds)
{
	foreach ($nId in $numOfIds) 
	{
		& $java -jar $jar $fId $nId
	}
}