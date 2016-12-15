package com.zzh.template

import com.zzh.WriteFile

def folder = new File("C:/workspace/swse-templates-esi/richemont.templateESI.swse/Prod/Brands_env/iwc/en/US")

def findTemplate(File file, Map map){
    if(file.isFile() && file.name == 'template.xml'){
        map.put(file.getParentFile().getName(), file)
    }else{
        file.listFiles().each{ findTemplate(it, map) }
    }
}


def types = [
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


def map = [:]
findTemplate(folder, map)

def newNames = ["christine.auh@iwc.com", "lukas.honold@iwc.com", "bruna.messina@iwc.com"]

println  types - map.keySet()

map.each{key,File file ->
    if(!types.contains(key)){
        println key
        println "!!!!!!!!!!!!!!!"
        return
    }
    def content = file.getText()

    def pattern = ~/\<Bcc\>(.*)\<\/Bcc\>/
    def m = pattern.matcher(content)

    println key
    def s = m.size() as Integer
    def group = m[s-1][0] as String
    def exists = m[s-1][1]
    def set = exists.split(",") as LinkedHashSet
    set.addAll(newNames)

    content =  content.replace(group, "<Bcc>${set.join(',')}</Bcc>")

    def isbom = false;

    file.withInputStream { fis ->
        byte[] openingBytes = new byte[3]
        fis.read( openingBytes )
        if( openingBytes != [0xEF, 0xBB, 0xBF ] as byte[] ) {
            isbom = false
        }else{
            isbom = true
        }
    }
    if(isbom){
        WriteFile.saveBomFile(file, content, true)
    }else{
        file.write(content,"UTF-8");
    }

    println "===================="
}


