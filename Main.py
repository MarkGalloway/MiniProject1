## Katherina, run me by typing "python3.3-32 Main.py"
# This enters 32 bit mode, which is needed for cx_oracle or it will explode.
# Maybe if you are not using python 3.3, you can change it to 3.2-32 or something. you get the idea

# mac port forward: ssh -L 1525:gwynne.cs.ualberta.ca:1521 mgallowa@ohaton.cs.ualberta.ca

import cx_Oracle
import getpass

def connection_test():
    #Our usernames, hardcoded for now...
    usr = "mgallowa"
    #usr ="jasniewsk"

    # Get password from command line rather than hardcode senstive info...
    pswd = getpass.getpass()

    # Build connection string
    host = "localhost"
    port = "1525"
    sid = "CRS"
    dsn = cx_Oracle.makedsn (host, port, sid)

    # Connect
    connection = cx_Oracle.connect(usr, pswd, dsn);
    if (connection):
        print ("Connection successful")
    else:
        print ("Connection not successful")

    connection.close()

connection_test()
