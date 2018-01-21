package db
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import javax.swing.table.DefaultTableModel
import java.util.Vector
import java.util.*
import java.awt.*
import java.sql.*
import java.util.*
import javax.swing.*
import javax.swing.table.*
import javax.swing.JTable
import com.sun.xml.internal.fastinfoset.alphabet.BuiltInRestrictedAlphabets.table
import com.sun.deploy.config.JREInfo.getAll

class Database {
    private fun dbConnect() : Connection? {
        var connection: Connection? = null
        try {
            val url = "org.sqlite.JDBC"
            val urlcon = "jdbc:sqlite:C:/temp/sqlite/db/dbNetwork.db"
            Class.forName(url)
            connection = DriverManager.getConnection(urlcon)
        } catch (ex: ClassNotFoundException) {
            println("No JDBC"+ex)
        } catch (ex: SQLException) {
            println("Fehler Verbindung"+ex)
        }
        return connection
    }
    fun dbReadEntry(entryNr:Int):DbNetwork{
        var c = DbNetwork(0,"e","e",0,0,"e")
        var entryNrAdd = entryNr+1
        try {
            val st = this.dbConnect()?.createStatement()
            val sql = "Select * From tableNetwork Where _Index = $entryNrAdd"
            val rs = st?.executeQuery(sql)
            while (rs!!.next()) {
                c = DbNetwork(
                        rs.getInt("_index"),
                        rs.getString("ipAddress"),
                        rs.getString("hostname"),
                        rs.getInt("vlan"),
                        rs.getInt("portSwitch"),
                        rs.getString("macAddress")
                )
            }
        } catch (ex: SQLException) {
            println("Error daten"+ex)
        }
        this.dbConnect()?.close()
        return c
    }
    fun dbReadAllEntry():ArrayDbDatabaseNetwork{
        val list = ArrayDbDatabaseNetwork()
        try {
            val st = this.dbConnect()?.createStatement()
            val sql = "Select * From tableNetwork"
            val rs = st?.executeQuery(sql)

            while (rs!!.next()) {
                val c = DbNetwork(
                        rs.getInt("_index"),
                        rs.getString("ipAddress"),
                        rs.getString("hostname"),
                        rs.getInt("vlan"),
                        rs.getInt("portSwitch"),
                        rs.getString("macAddress")
                )
                list.arrayDbDatabaseNetwork.add(c)
            }
        } catch (ex: SQLException) {
            println("Error daten"+ex)
        }
        this.dbConnect()?.close()
        return list
    }
    fun dbAddEntry(dbNetwork:DbNetwork){
        try {
            val sql = "INSERT INTO tableNetwork (ipAddress,hostname,vlan,portSwitch,macAddress)" + "VALUES (" +
                    "'${dbNetwork.ipAddress}','${dbNetwork.hostname}',${dbNetwork.vlan},${dbNetwork.portSwitch},'${dbNetwork.macAddress}')"
            val st = this.dbConnect()?.createStatement()
            st?.executeUpdate(sql)
        } catch (ex: SQLException) {
            println("Error daten"+ex)
        }
        this.dbConnect()?.close()
    }
    fun dbDeleteIndex(index:Int){
        var entryNrAdd = index+1
        try {
        val st = this.dbConnect()?.createStatement()
        st?.executeUpdate("DELETE FROM tableNetwork WHERE _index = $entryNrAdd;")
        } catch (ex: SQLException) {
            println("Error daten"+ex)
        }
        this.dbConnect()?.close()
    }
    fun dbDeleteTable(){
        try {
            val st = this.dbConnect()?.createStatement()
            st?.executeUpdate("DELETE FROM tableNetwork;")
            st?.executeUpdate("DELETE FROM sqlite_sequence WHERE name = 'tableNetwork'")
        } catch (ex: SQLException) {
            println("Error daten"+ex)
        }
        this.dbConnect()?.close()
    }
    fun dbUpdatePk(){
        try {
            val st = this.dbConnect()?.createStatement()
            st?.executeUpdate("ALTER TABLE tableNetwork RENAME TO temp_tableNetwork;")
            st?.executeUpdate("CREATE TABLE `tableNetwork` ('_index' integer primary key autoincrement, `ipAddress` TEXT,`hostname` TEXT,`vlan` INTEGER,`portSwitch`INTEGER,`macAddress`TEXT);")
            st?.executeUpdate("INSERT INTO 'tableNetwork'(ipAddress, hostname, vlan, portSwitch, macAddress) SELECT ipAddress,hostname,vlan,portSwitch,macAddress FROM temp_tableNetwork;")
            st?.executeUpdate("DROP TABLE temp_tableNetwork")
        } catch (ex: SQLException) {
            println("Error daten"+ex)
        }
        this.dbConnect()?.close()
    }
    fun dbUpdateEntry(dbNetwork:DbNetwork, index:Int){
        var entryNrAdd = index+1
        try {
            val sql = "UPDATE tableNetwork SET ipAddress = '${dbNetwork.ipAddress}', hostname = '${dbNetwork.hostname}',vlan = '${dbNetwork.vlan}',portSwitch = '${dbNetwork.portSwitch}', macAddress = '${dbNetwork.macAddress}' WHERE _index = $entryNrAdd"
            val st = this.dbConnect()?.createStatement()
            st?.executeUpdate(sql)
        } catch (ex: SQLException) {
            println("Error daten"+ex)
        }
        this.dbConnect()?.close()
    }
    fun dbTableData():DefaultTableModel{
        val defaultTableModel:DefaultTableModel
        val columnNames = Vector<Any>()
        val data = Vector<Any>()
        try {
            //  Read data from a table
            val sql = "Select * From tableNetwork"
            val st = this.dbConnect()?.createStatement()
            if (st!=null) {
                val rs = st.executeQuery(sql)
                val md = rs.metaData
                val columns = md.columnCount

                //  Get column names
                for (i in 1..columns) {
                    columnNames.addElement(md.getColumnName(i))
                }

                //  Get row data
                while (rs.next()) {
                    val row = Vector<Any>(columns)

                    for (i in 1..columns) {
                        row.addElement(rs.getObject(i))
                    }

                    data.addElement(row)
                }
                rs.close()
                st.close()
            }

        } catch (e: Exception) {
        println(e)
    }
        this.dbConnect()?.close()
        val model = object : DefaultTableModel(data, columnNames){
            override fun getColumnClass(column: Int): Class<*> {
                for (row in 0 until rowCount) {
                    val o = getValueAt(row, column)

                    if (o != null) {
                        return o.javaClass
                    }
                }

                return Any::class.java
            }
        }
        return model
    }
}