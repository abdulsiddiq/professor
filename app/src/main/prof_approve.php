<?php
require_once __DIR__ . '/db_connect.php';
$db = new DB_CONNECT();

$requestBody = json_decode(file_get_contents('php://input'), true);

$prof = $requestBody['prof'];
$student = $requestBody['student'];
$venue = $requestBody['venue'];
$subject = $requestBody['subname'];
$stream = $requestBody['stream'];
$approved = $requestBody['isApproved'];

if($approved == 1)
{   
    $query = "insert into enrollment (stream_name,student_name,sub_name,prof,venue) values ('$stream','$student','$subname','$prof','$venue')";
    $secondQuery = "update subjects set enrolled = (enrolled + 1) where name = '$subject' and prof = '$prof' and venue = '$venue'";
    $sql = mysql_query($query);
    $sql = mysql_query($secondQuery);
    
}
    $query = "DELETE from approvals where profname = '$prof' AND studentname = '$student' AND subname = '$subject'";
    $sql = mysql_query($query);

echo json_encode($response);

?>