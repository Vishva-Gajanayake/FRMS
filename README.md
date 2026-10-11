"# Fleet-Rental-Management-System" 

ITS1033 Object-Oriented Programming coursework - HDSE 77, Institute of Software Engineering (IJSE).

A terminal-based Java application for managing a vehicle rental fleet: users, vehicles (Car, Van, Truck),
customers (Individual, Corporate) and rental agreements. All types live in the package `lk.ijse.fleet`.

## Requirements

* JDK 25 or later

## Project layout

```
src/lk/ijse/fleet/\*.java
```

## Build

From the project root:

```
javac -d bin $(find src -name "\*.java")
```

## Run

```
java -cp bin lk.ijse.fleet.Main
```

## Using the program

1. Choose `\[2] Register New User`, then `\[1] Login`.
2. Use the main menu to add vehicles and customers, create/return rental agreements and view reports.
3. Background daemon threads print the fleet status and overdue alerts every 10 seconds while the menu stays usable.

Passwords are stored in plain text (as required by the brief); this is not a production login system.
