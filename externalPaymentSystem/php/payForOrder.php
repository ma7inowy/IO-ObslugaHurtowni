<?php

$invoiceNumber = (int)$_POST['invoiceNumber'];
$totalPrice = (double)$_POST['totalPrice'];
$config = require 'config.php';
$pdo = new PDO('sqlite:' . $config['dblocation']);
$invoiceState = $pdo->query("SELECT * FROM transactions WHERE transactions.orderID=$invoiceNumber")->fetch(PDO::FETCH_ASSOC);

    if ($invoiceState != null) {
        if (!$invoiceState['state']) {
            $result = $pdo->query("SELECT * FROM main.transactions WHERE orderID='$invoiceNumber'");
            $totalCost = $result->fetchColumn(3);
            if ($totalCost == $totalPrice) {
                $accountState = $pdo->query("SELECT money FROM account")->fetchColumn(0);
                $accountState += $totalPrice;
                $pdo->query("UPDATE account SET money=$accountState");
                $pdo->query("UPDATE transactions SET state=true WHERE transactions.orderID=$invoiceNumber");
                $description = 'payment for order ' . $invoiceNumber;
                $pdo->query("INSERT INTO cashflows (date, amount, name) values (datetime('now'), $totalPrice, '$description')");
                echo "transaction is completed";
            } else {
                echo "wrong amount of money";
            }
        } else {
            echo "transaction had been completed";
        }
    } else {
        echo "transaction does not exits";
    }