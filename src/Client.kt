import javax.swing.JFrame
import gui.ClientGui
import db.DbNetwork
import db.Database


fun main(args: Array<String>) {
    val database = Database()
    val entryDb = DbNetwork(0,"192.168.1.5","test",55,24, "99:99:99:88:88")

    val frame = JFrame("Verwaltung Netzwerkteilnehmer")
    frame.contentPane = ClientGui().panelMain
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.setSize(800, 800)
    frame.isVisible = true



    //database.dbReadAllEntry()
    //database.dbAddEntry(entryDb)
    //database.dbDeleteIndex(2)
    //database.dbDeleteTable()
    //database.dbUpdatePk()
}
