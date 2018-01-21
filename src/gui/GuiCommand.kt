package gui
import db.Database
import db.DbNetwork

class GuiCommand {
    fun addDataSet(ipAddress:String, hostname:String, vlan:String, portSwitch:String, macAddress:String){
        val database = Database()
        val entryDb = DbNetwork(0, ipAddress, hostname, vlan.toInt(), portSwitch.toInt(), macAddress)
        database.dbAddEntry(entryDb)
    }
    fun updateDataSet(index:Int, ipAddress:String, hostname:String, vlan:String, portSwitch:String, macAddress:String){
        val database = Database()
        val entryDb = DbNetwork(0, ipAddress, hostname, vlan.toInt(), portSwitch.toInt(), macAddress)
        database.dbUpdateEntry(entryDb, index)
    }
}