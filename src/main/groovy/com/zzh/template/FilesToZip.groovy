package com.zzh.template

String path = "C:/workspace/swse-templates-esi/richemont.templateESI.swse/Prod/Brands_env"
File folder = new File(path)

def status = "cmd /c git status".execute(null, folder).text

def modifieds = []
status.eachLine {
    if(it.contains('modified')){
        def a = it.split("\\s+")
        modifieds << a.last()
    }
}

def m = modifieds.join(" ")
println "cmd /c 7z a template_20160928_IWC.zip $m".execute(null, folder).text
