<?php

$invoiceNumber = (int)$_POST['invoiceNumber'];
$totalPrice = (double)$_POST['totalPrice'];
$config = require 'config.php';
$pdo = new PDO('sqlite:' . $config['dblocation']);
$invoiceState = $pdo->query("SELECT * FROM transactions WHERE transactions.deliveryID=$invoiceNumber")->fetch(PDO::FETCH_ASSOC);

if($invoiceState != null) {
    if (!$invoiceState['state']) {
    $result = $pdo->query("SELECT * FROM deliveryProductLine WHERE deliveryId='$invoiceNumber'");
    $totalCost = 0;
    foreach ($result as $row) {
        $productResult = $pdo->query("SELECT * FROM products WHERE productID=$row[2]");
        $totalCost += $productResult->fetchColumn(2) * $row[3];
    }
    if ($totalCost == $totalPrice) {
        $accountState = $pdo->query("SELECT money FROM account")->fetchColumn(0);
        $accountState -= $totalPrice;
        $pdo->query("UPDATE account SET money=$accountState");
        $pdo->query("UPDATE transactions SET state=true WHERE transactions.deliveryID=$invoiceNumber");
        $description = 'payment for delivery '.$invoiceNumber;
        $pdo->query("INSERT INTO cashflows (date, amount, name) values (datetime('now'), -$totalPrice, '$description')");
        echo "transaction is completed";
    } else {
        echo "wrong amount of money";
    }
    } else {
        echo "transaction had been completed";
    }
}
else{
    echo "transaction does not exits";
}
