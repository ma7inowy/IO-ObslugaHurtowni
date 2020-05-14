<?php
    $clientNumber = (int)$_POST['client'];
    $employeeNumber = (int)$_POST['employeeNumber'];
    $comment = $_POST['comment'];
    $config = require 'config.php';
    $pdo = new PDO('sqlite:' . $config['dblocation']);
    $result = $pdo->query("SELECT * FROM clients WHERE clientID=$clientNumber");
    if($comment != ""){
        if($result->fetchColumn(0) == $clientNumber && $clientNumber != 0){
                if($pdo->query("SELECT * FROM employees WHERE employeeID=$employeeNumber")->fetchColumn(0) == $employeeNumber && $employeeNumber != 0){
                   $status = $pdo->query("INSERT INTO comments (comment, clientID, userID) VALUES ('$comment', $clientNumber, $employeeNumber)");
                   echo "comment add successfully";
                }
            else{
                echo "employee doesnt exist";
            }
        }
        else{
            echo "client doesnt not exit";
        }
    }else{
        echo "comment is empty";
    }
