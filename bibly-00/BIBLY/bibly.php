<style>

    * {
        font-family: sans-serif;
        font-size: 20px;
    }

    #form {
        margin: 10px;
        text-align: center;
    }

</style>

<br>

<form name="form" id="form" action="bibly.php" method="post">
    <input type="text" id="search" name="search">
    <input type="submit" value="search">
</form>

<?php 

        if(isset($_POST["search"])) {
        
            $search = $_POST["search"];

            $file = fopen("kjv.vpl","r");

            $n = 0;

            while($line=fgets($file)) {

                if(stristr($line,$search)) { 
                    $n++;
                    echo $n." -> ".$line . "<br><br>"; 
                }

            }

            fclose($file);

        }


?>
