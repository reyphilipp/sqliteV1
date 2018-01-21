package db

data class DbNetwork (
    val index:Int,
    var ipAddress:String,
    val hostname:String,
    val vlan:Int,
    val portSwitch:Int,
    val macAddress:String
)