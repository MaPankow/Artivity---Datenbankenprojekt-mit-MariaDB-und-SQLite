# Aufbau und Testen einer SQLite-Datenbank


Diese Übung dient dazu, die Datenbank SQLite zu benutzen, zu befüllen und Abfragen durchzuführen. Ich benutze dafür das Beispiel Social Network (in abgespeckter Form). Über Sinn und Unsinn einer SQLite-Datenbank für ein soziales Netzwerk werde ich später noch diskutieren. 

## 1. Erstellung des Datenbankmodells

Ich nehme das ERD aus dem Artivity-Projekt als Grundlage. Zur Übung specke ich es ab.
!["Entity-Relationship-Diagramm Social Network"](image.png)

## 2. Operationen durchführen
### 2.1 SQLite-DB anlegen
Dazu öffne ich das Terminal, navigiere in den Ordner, in dem ich die Datenbank abspeichern möchte. Dort erstelle ich die DB mit folgendem Befehl:

```bash
sqlite3 ArtivityLite.db
```
### 2.2 Tabellen erstellen und füllen


Die Tabellen erstelle ich entsprechen des ERD mit folgenden Befehlen:

**users**
```sql
 CREATE TABLE users (
    id_user INTEGER PRIMARY KEY AUTOINCREMENT, 
    username VARCHAR(100) NOT NULL UNIQUE, 
    email VARCHAR(100) NOT NULL UNIQUE, 
    passwort VARCHAR(255) NOT NULL, 
    profilbild BLOB, 
    profilinfo TEXT
    );
```

Eine Datenbank ist geboren!!! Sie wird erst als Datei auf dem eigenen Rechner abgespeichert, wenn eine Tabelle angelegt ist.
Ich gebe einen Datensatz ein:
```sql
insert into users (username, email, passwort, profilbild, profilinfo) VALUES 
('martina', 'email1@emailhausen.de', 'pwd-sicher!', NULL, 'mal kieken, ob et jeht!');
```
Nachdem ich weitere Datensätze eingegeben habe, geht es weiter mit der nächsten Tabelle.

**posts**

```sql
create table posts (
id_post integer primary key autoincrement, 
fid_user integer, 
titel varchar(100), 
body text, 
erstellt_am timestamp default current_timestamp,
foreign key (fid_user) references users(id_user)
);
``` 

```sql
insert into posts(fid_user, titel, body) values(1, "Erster Post", "Willkommen bein Artivity!"), (1, "Zweiter Post", "Halloooooo?"), (2, "Dritter Post", "Architektur ist interessant"), (5, "Vierter Post", "Hi, ich bin User 5!");
```
Verschrieben beim ersten Post:

```sql
update posts set body = "Willkommen bei Artivity!" where titel = "Erster Post";
```


**kommentare**
```sql
create table kommentare (
id_kommentar integer primary key autoincrement, 
fid_post integer,
fid_user integer, 
body text, 
erstellt_am timestamp default current_timestamp,
foreign key (fid_user) references users(id_user),
foreign key (fid_post) references posts(id_post)
);
```
```sql
insert into kommentare (fid_post, fid_user, body) values
(4, 4, "Moinmoin!"), (6, 3, "Hi, ich bin User 3"), (3, 2, "Hallo, freue mich, hier zu sein!");
```
**schlagwoerter**

```sql
create table schlagwoerter(
id_schlagwort integer primary key autoincrement,
schlagwort varchar(50)
);
```
```sql
insert into schlagwoerter(schlagwort) values 
('Malerei'),
('Architektur'),
('Musik'),
('Mode'),
('Film');
```

**schlagwort-bezeichnung**
Diese Tabelle ist eine Hilfstabelle, um eine Many-to-Many-Beziehung aufzulösen, die sonst zwischen users und schlagwoerter bzw. posts und schlagwoerter bestanden hätte. Z. B. kann ein User mehrere Schlagwörter haben und ein Schlagwort kann von mehreren Users ausgesucht werden. 

Darum gibt es die Hilfstabelle schlagwort-bezeichnung, in der die ids der schlagwörter den ids von posts bzw. users zugeordnet werden können.

```sql 
create table schlagwort_bezeichnung(
id_schlagwort_bezeichnung integer primary key autoincrement,
fid_schlagwort integer,
fid_user integer,
fid_post integer,
foreign key(fid_schlagwort) references schlagwoerter(id_schlagwort),
foreign key(fid_user) references users(id_user),
foreign key(fid_post) references Posts(id_post)
);
```
Den users Schlagwörter zuordnen:
```sql
insert into schlagwort_bezeichnung(fid_schlagwort, fid_user) values
(2, 2),
(2, 5),
(1, 2),
(3, 3),
(4, 1),
(5, 4);
```
Den Posts Schlagwörter zuordnen:
```sql
insert into schlagwort_bezeichnung(fid_schlagwort, fid_post) values
(2, 4),
(3, 3),
(1, 2);
```

**Foreign Keys aktivieren**
Muss man bei SQLite machen, da sie sonst nicht funktionieren.

```sql
PRAGMA foreign_keys = ON;
```

### 2.3 Abfragen

**SELECT-Abfragen Beispiele**

```sql
select * from users where profilbild is not null;
```
Gibt die Datensätze wieder, in denen ein Profilbild hinterlegt wurde.

Einen Datensatz löschen:
```sql
delete from posts where id_post = 1;
```
Einen Datensatz verändern:
```sql
update posts set body = "Ich freue mich, euch alle kennenzulernen" where id_post=2;
```

## 3. Performance-Messung

### Performance bei MariaDB
Ich benutze MariaDB mit XAMPP und im Browser über PHP MyAdmin. Dort wird die Geschwindigkeit einer Query angezeigt:
```sql
SELECT * FROM users WHERE profilinfo IS NOT NULL;
```
Ergebnis:
![MariaDB einfache Query, Abfrage dauerte 0,0003 Sekunden](queryMariaDB.png)

Und hier die Performance für eine kompliziertere Abfrage mit INNER JOINs:
```sql
SELECT p.titel, p.body, s.schlagwort FROM schlagwort_bezeichnung sb INNER JOIN posts p ON sb.fid_post = p.id_post INNER JOIN schlagwoerter s ON s.id_schlagwort = sb.fid_schlagwort WHERE p.id_post = 2; 
```
Ergebnis:
![MariaDB Query mit zwei INNER JOINS, Abfrage dauerte 0.0108 Sekunden](innerJoinMariaDB.png)

### Performance bei SQLite
Bei SQLite kann man einen Timer-Befehl verwenden, um die Abfragegeschwindigkeit zu messen.
Dieser funktioniert in der Shell, indem ich zu dem Ordner navigiere, in dem ich die SQLite.db abgespeichert habe. (Oder zuerst das Terminal öffne und zum entsprechenden Ordner navigiere) In das Terminal gebe ich ein:
```
sqlite3 ArtivityLite.db
```
Dann verwende ich die Timer-Funktion mit folgendem Code:
```sql
.timer ON
SELECT * FROM users WHERE profilinfo IS NOT NULL;
.timer OFF
```
... und bekomme folgendes Ergebnis:
```
Run Time: real 0.000 user 0.000235 sys 0.000000
```
Jetzt probiere ich auch hier die längere Abfrage mit den zwei INNER JOINS:
```sql
sqlite> .timer ON
sqlite> SELECT p.titel, p.body, s.schlagwort FROM schlagwort_bezeichnung sb INNER JOIN posts p ON sb.fid_post = p.id_post INNER JOIN schlagwoerter s ON s.id_schlagwort = sb.fid_schlagwort WHERE p.id_post = 3;      
```
Ich erhalte folgendes Ergebnis:
```
Run Time: real 0.001 user 0.000314 sys 0.000000
```
Der real-Wert ist der, der die "Run Time" im Gesamten wiedergibt. Diese wird in unserem Fall durch die verbrauchte CPU bei der Query, die als user angegeben wird, bestimmt. Ressourcen im System werden kaum verbraucht. 
Gebe ich die beiden Querys öfter ein, bekomme ich unterschiedliche Ergebnisse. Dennoch ist jedes Mal gleich, dass bei dem Wert von user vor dem Komma und an den ersten drei Stellen hinter dem Komma 0 steht. Im Schnitt braucht die längere Query mit den INNER JOINS ca. 0.000200 Sekunden länger als die einfache Query. 

### Fazit
Bei der einfachen Abfrage sind beide MariaDB und SQLite ähnlich schnell, doch wird die Query länger, braucht MariaDB auch länger. Hier wird vor allem das Datenbankdesign wichtig, um sehr lange und komplexe Abfragen zu verhindern. Auch wenn es auf den ersten Blick sehr schnell zu gehen scheint, so wird es umso länger, wenn mehrere Abfragen hintereinander ausgeführt werden sollen und und durch wirres Datenbankdesign die Abfragen über viele JOINS gehen müssen. 

Doch Geschwindigkeit ist nicht alles ...


## 4. SQLite vs. SQL-Datenbank mit Server ... was ist besser für Artivity?

The battle of the Databases ...

SQLite | Serverbasierte DB
--- | ---
in Anwendung integriert, serverlos | erfordert extra Server
läuft direkt beim Client | Datenspeicherung auf Servern
Anwendung speichert Änderungen clientseitig, kein Zugriff durch die Anwendung auf die Daten | die Anwendung speichert die Daten auf dem Server und es erfolgt Zugriff, um z. B. online Nutzerprofile anzeigen zu können
für kleinere Anwendungen | für Anwendungen, die die Speicherung vieler Daten erfordern
wenig skalierbar, Datenbank passt in eine Datei | komplexere Datenstruktur, skalierbar
Operationen und Transaktionen laufen nacheinander | gleichzeitige Lese- und Schreibprozesse möglich
für Anwendungen, die offline laufen müssen | Server stellt die Daten rund um die Uhr zur Verfügung

**Welche Datenbank eignet sich also besser für ein Social Network?**

In diesem Fall nutze ich Artivity, um damit das Modellieren in verschiedenen Datenbanken zu üben.
Sollte ich ernsthaft ein Social Network aufsetzen wollen, wäre die serverbasierte Datenbank das richtige Modell.

Verschiedene Use-Cases liegen hier zugrunde:
- ein soziales Netzwerk muss immer online, der Server immer erreichbar sein
- die Speicherung der Daten sollte nicht lokal clientseitig erfolgen, sondern auf einem Server, damit es überhaupt möglich wird, Daten weltweit zu teilen (z. B. Userprofil, Posts, Kommentare etc.)
- eine serverbasierte Datenbank ist skalierbar, sie kann durch Sharding große Datensätze auf mehrere Server verteilen und durch das Erstellen von Replikationen sicherstellen, dass die Daten erhalten bleiben
- es muss gewähleistet sein, dass mehrere Users gleichzeitig Requests durchführen und Daten eintstellen und abrufen können
(Queue könnte man bei SQLite implementieren, die mehrere Abfragen nacheinander durchführen kann, wenn sie gleichzeitig eintreffen, das könnte aber zu Wartezeiten für die Users führen)

**Fazit**
Ich werde Artivity nur zu Übungszwecken mit einer SQLite erstellen.
Natürlich braucht ein soziales Netzwerk ein serverbasiertes Datenbanksystem.

