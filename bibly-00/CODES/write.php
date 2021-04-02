<?php

if isset($_POST["message"]) 
    $message=$_POST["message"]; 
else 
    $message="no message";

$fh=fopen("database.txt","w");
fwrite($fh,$message);
fclose($fh);

?>