#JONAS-Student-Management

En JAVA EE applikation med CRUD-funktionalitet.

(CREATE)
Skapa ny student via POST-request via endpoint:
http://localhost:8080/student-management-system/api/v1/students
i JSON-formatet:

{
"firstName" : "Lars",
"lastName" : "Kongo",
"email" : "lasse.kongo@tallinn.ee",
"phoneNumber" : "0736483434"
}

Alla fälten är obligatoriska förutom telefonnummer, och email måste vara unik.

(READ)
Visa alla studenter med en GET-request via endpoint:

http://localhost:8080/student-management-system/api/v1/students

(READ)
Visa en student med specifikt ID med en GET-request via endpoint:

http://localhost:8080/student-management-system/api/v1/students/{id}

Där {id} är id:t på studenten du letar efter.

(READ)
Visa alla studenter med ett specifikt efternamn med en GET-request och query-parameter via endpoint:

http://localhost:8080/student-management-system/api/v1/students/query?lastname=Kongo

där du ersätter "Kongo" med efternamnet du vill söka efter.

(UPDATE)
Uppdatera en student med en PATCH-request via endpoint:

http://localhost:8080/student-management-system/api/v1/students/{id}

Där du skickar in id:t på studenten du vill uppdatera samt ett JSON-objekt med det fältet som skall updateras, ex:

{
"email" : "lassesnya@tallinn.ee"
}

(DELETE)
Ta bort en student med en DELETE-request via endpoint:

http://localhost:8080/student-management-system/api/v1/students/{id}

Där {id} är id:t på studenten du vill ta bort.