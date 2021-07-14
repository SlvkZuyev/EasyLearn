<?php

$response = array();

require 'db_connect.php';

require 'db_config.php';
$db = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE) or die(mysql_error());

$result = mysqli_query($db, 'SELECT *FROM test_table') or die(mysqli_error());

$response = array();

if (mysqli_num_rows($result) > 0) {
    $response["test_table"] = array();

    while ($row = mysqli_fetch_array($result)) {
        $pair = array();
        $pair["col_1"] = $row["col_1"];
        $pair["col_2"] = $row["col_2"];

        echo $row["col_1"] . " " . $row["col_2"];
        array_push($response["test_table"], $pair);
    }
    $response["success"] = 1;
    
    echo json_encode($response, JSON_UNESCAPED_UNICODE);
} else {
    $response["success"] = 0;
    $response["message"] = "No test_table found";

    echo json_encode($response);
}

?>




<?php

$response = array();

require 'db_connect.php';

require 'db_config.php';
$db = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE) or die(mysql_error());

$result = mysqli_query($db, 'SELECT *FROM test_table') or die(mysqli_error());


$row = mysqli_fetch_array($result);

$col_1 = $row[0];
$col_2 = $row[1];
//$col_3 = 'hahahahhah';
echo $col_1;
echo $col_2;
//echo $col_3;
?>