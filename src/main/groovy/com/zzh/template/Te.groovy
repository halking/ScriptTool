package com.zzh.template


def list = [
    "Exchange_Notice",
    "Newsletter_Unsubscription",
    "Non_Dedicated_Stock",
    "Order_Acknowledgment",
    "Order_Acknowledgment_Confirmation",
    "Order_Acknowledgment_Phone_Sales",
    "Order_Boutique_Pickup_Ready",
    "Order_Cancellation",
    "Order_Confirmation",
    "Order_Credit_Invoice",
    "Order_Draft_Acknowledgment",
    "Order_Draft_Cancellation",
    "Order_Modification",
    "Order_Refund",
    "Order_Refund_RCH_Service",
    "Order_Returns_Received",
    "Return_Process"
]

def l = [
    "Account_Creation",
    "Exchange_Notice",
    "Non_Dedicated_Stock",
    "Order_Acknowledgment",
    "Order_Acknowledgement_Phone_Sales",
    "Order_Boutique_Pickup_Ready",
    "Order_Cancellation",
    "Order_Confirmation",
    "Order_Credit_Invoice",
    "Order_Debit_Invoice",
    "Order_Modification",
    "Return_Process",
    "Order_Returns_Received"
]

def a = list - l

println a
println l - list