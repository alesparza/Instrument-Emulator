# Instrument-Emulator

Instrument-Emulator is a Java library to emulate laboratory instruments.
The main idea is to create a set of results and send them using the ASTM protocol to an LIS.

Some instruments have character limits but that is not really built into the system.
I also do not know if dates need to be in a specific format.

The general framework is based on the ASTM protocol ASTM-1381-02, currently maintained by CLSI as [LIS02-A2](https://clsi.org/standards/products/automation-and-informatics/documents/lis02/).

This is mostly just a proof-of-concept idea, but it does send H, P, O, R, and L records to an LIS.

## Installation

### From Source

Use Maven.

```bash
mvn package
```

### From JAR

Download the jar from [GitHub](https://github.com/alesparza/Instrument-Emulator/releases/latest).

## Usage

### Start the program

Run with Java (require Java 17 or higher).

```bash
java -jar Emulator.jar
```

### Main Window

Use the main window to create a new emulated instrument.

The Instrument Name is mostly to name the window for that instrument.

Instruments can act as a server or client.  Port number always required.

For a client, include the hostname or IP address of the LIS on the other end that will receive results.
For a server, it runs locally.  So there might be firewall issues.

The Type dropdown is mostly not used, right now it just changes the delimiters since DxH uses a different set.
But ASTM really allows any delimiters so the receiving end should be able to process it correctly.

Once created, the emulated instrument appears in the bottom table.  
I haven't figured out how to remove it when the instrument window closes.

### Instrument Window

#### Command Buttons

Each emulated instrument gets a separate window for user management.

Start the connection with the Start button.
Safe shutdown with Stop.
Reset stops and starts the connection.

Listen is only available for "server" type instruments.
This listens for incomming connections for five seconds before reporting failure.
Because this is the only way I could figure out how to do this.

The Check button sends a basic ENQ and wait for an ACK before sending EOT. 
Just a basic consistency check for the connection.

Send will send all the data in the tabs to the other end using ASTM protocol.

Clear Comms clears the two log windows.

#### Data Tabs

Connection: mostly a reminder of the settings used for the connection.

Device: Mostly stuff that goes in H record.

Patient: Mostly stuff that goes in P record.

Sample: Mostly stuff that goes in O record.

Results: Mostly stuff that goes in an R record.

#### Results Table

The bottom shows a list of current assays.  

To create a new assay unlock the Current Assay Field, enter the specific data, then click Add As New Assay.

Change the displayed assay by unlocking the Current Assay field, 
entering the index in the Current Assay field, lock it, then click Change Loaded Assay.

Assays that already exist can be modified by changing the locked displayed assay and clicking Update This Assay.

Delete an assay by locking the current assay and clicking Delete This Assay.
If the index does not match the current displayed assay, this will update and ask you to click delete a second time.


## Known Bugs/Future Feature List

1. Comment records do not exist.
1. Closing the emulation window does not remove from the main frame table.
1. Really need a custom exception to throw and print instead of returning Strings.
1. Does not receive anything, just checks for ACK (so no Q records).
1. No unit tests.

## Contributing

Pull requests are welcome.
For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[MIT](https://choosealicense.com/licenses/mit/)
