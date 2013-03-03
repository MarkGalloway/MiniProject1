## Katherina, run me by typing "python3.3-32 Main.py"

# mac port forward: ssh -L 1525:gwynne.cs.ualberta.ca:1521 mgallowa@ohaton.cs.ualberta.ca

import cx_Oracle
import getpass

def connection_test():
    usr = "mgallowa"
    #usr ="jasneiwski"

    # Get password from command line rather than hardcode senstive info...
    pswd = getpass.getpass()

    ## Build connection string
    host = "localhost"
    port = "1525"
    sid = "CRS"
    dsn = cx_Oracle.makedsn (host, port, sid)

    connection = cx_Oracle.connect(usr, pswd, dsn);

    if (connection):
        print ("Connection successful")
    else:
        print ("Connection not successful")

    connection.close()

connection_test()