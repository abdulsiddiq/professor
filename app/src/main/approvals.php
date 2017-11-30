<?php
require_once __DIR__ . '/db_connect.php';
$db = new DB_CONNECT();

$requestBody = json_decode(file_get_contents('php://input'), true);

$prof = $requestBody['prof'];

       $query = "SELECT * from approvals where profname = '$prof'";
    $sql = mysql_query($query);
                 $newArray = array();   
   						while ($row = mysql_fetch_assoc($sql)) {
                               $col["student"] = $row["studentname"];
                               $col["timing"] = $row["venue"];
                               $col["subject"] = $row["subname"];
                               $col["stream"] = $row["stream"];
                               $newArray[] = $col;
							}


                            $response["subjectList"] = $newArray;

echo json_encode($response);

?>