Tēma: Pasākumu reģistrācijas sistēma<br>
5.Grupa<br>
Autori :<br>
Artjoms Doktorovs - Kizarov3<br>
Gļebs Vasiļjevs - Destral1x<br>
ATS/PR-32

# Pasākumu reģistrācijas sistēma

Darbvirsmas lietotne (Java Swing + Apache Derby) pasākumu izveidei un dalībnieku reģistrācijai.
Administrators izveido pasākumus un apstiprina vai noraida pieteikumus, dalībnieks piesakās
pasākumiem un seko līdzi sava pieteikuma statusam.

## Vides prasības

- JDK 21 (Derby 10.17 darbojas tikai ar Java 21 vai jaunāku)
- Apache NetBeans IDE (projekts ir NetBeans Ant projekts)
- Visas bibliotēkas jau ir mapē `lib/`: `derby.jar`, `derbyshared.jar`, `derbytools.jar`,
  `junit-4.13.2.jar`, `hamcrest-core-1.3.jar`, papildus neko lejupielādēt nav vajadzīgs

## Instalēšanas instrukcija

1. Nokopējiet projekta mapi uz datoru (vai klonējiet GitHub repozitoriju).
2. NetBeans: **File → Open Project…** un izvēlieties mapi `5.EventRegistrationSystem`.
3. Ja NetBeans ziņo par trūkstošu Java platformu, **Project Properties → Libraries → Java Platform**
   izvēlieties JDK 21.
4. Palaidiet programmu: **Run → Run Project** (F6).

Bez NetBeans (no projekta mapes):

```
ant jar
java -jar dist/5.EventRegistrationSystem.jar
```

Programma jāpalaiž no projekta mapes, jo datu bāze atrodas relatīvajā ceļā `database/eventdb`.

## Datu bāze

- Lokālā (iegultā) Apache Derby datu bāze mapē `database/eventdb`, serveris nav vajadzīgs.
- Ja datu bāzes nav, pirmajā palaišanā tā tiek izveidota automātiski no
  `src/eventregistrationsystem/sql/schema.sql` (tabulas) un `data.sql` (sākuma dati).
- Datu bāzi vienlaikus var atvērt tikai viena programma. Ja tā ir atvērta NetBeans logā
  **Services**, pirms palaišanas nospiediet **Disconnect**.
- Rezerves kopija: aizveriet programmu un nokopējiet mapi `database/eventdb`.
  Lai atjaunotu, iekopējiet to atpakaļ.

## Lietošanas instrukcija

Sākuma lietotāji (no `data.sql`):

| Loma | Lietotājvārds | Parole |
|---|---|---|
| Administrators | `admin` | `admin123` |
| Dalībnieks | `janis@example.com` | `admin123` |

1. **Reģistrēties** - ielogošanās logā nospiediet "Reģistrēties", aizpildiet vārdu, uzvārdu,
   e-pastu, (tālruni) un paroli divreiz. Jauns lietotājs vienmēr ir dalībnieks.
2. **Ielogoties** - ievadiet lietotājvārdu (e-pastu) un paroli, nospiediet "Ielogoties".
3. **Apskatīt un meklēt pasākumus** - galvenajā logā redzami aktīvie pasākumi un brīvo vietu skaits.
   Ievadiet nosaukumu, vietu un/vai datumu un nospiediet "Meklēt"; "Notīrīt" atkal rāda visus.
4. **Pieteikties pasākumam (dalībnieks)** - izvēlieties pasākumu tabulā un nospiediet
   "Reģistrēties dalībai". Ja profilā nav tālruņa, programma to paprasīs.
   Pieteikuma statuss redzams kolonnā "Mans statuss" (Gaida / Apstiprināts / Noraidīts).
   Lai pieteikumu atsauktu, izvēlieties pasākumu un nospiediet "Atcelt pieteikumu".
5. **Izveidot pasākumu (administrators)** - "Pievienot pasākumu", aizpildiet laukus
   (datums dd.mm.gggg, laiks hh:mm) un nospiediet "Saglabāt".
   Esošu pasākumu var izvēlēties tabulā un "Labot" (atveras tas pats logs ar pasākuma datiem)
   vai "Dzēst" (pēc apstiprinājuma tiek dzēsts pasākums kopā ar tā pieteikumiem).
6. **Apstiprināt vai noraidīt pieteikumus (administrators)** - izvēlieties pasākumu un nospiediet
   "Dalībnieki", izvēlieties pieteikumu un nospiediet "Apstiprināt" vai "Noraidīt".
   Apstiprināt var tikai tad, ja ir brīvas vietas.
7. **Dzēst kontu (dalībnieks)** - izvēlne **Fails → Dzēst kontu**, apstipriniet dzēšanu.
8. **Izrakstīties / Iziet** - pogas vai izvēlne **Fails**; abas darbības prasa apstiprinājumu.

Visas darbības pieejamas arī galvenajā izvēlnē (**Fails**, **Pasākumi**).
Īsa lietošanas instrukcija atbilstoši lomai: izvēlne **Palīdzība → Lietošanas instrukcija** (F1).

## Dokumentācija un testi

- Javadoc dokumentācija: `docs/index.html`. Atjaunot: NetBeans **Run → Generate Javadoc**
  (rezultāts tiek saglabāts mapē `docs/`).
- JUnit4 testi: mapē `test/`, palaišana - **Run → Test Project** (Ctrl+F6) vai `ant test`.
- Kļūdu žurnāls: `Kludu_zurnals.docx`.

## Projekta struktūra

| Pakotne | Saturs |
|---|---|
| `eventregistrationsystem` | `EventRegistrationSystemGUI` - galvenais logs (JFrame) un apakšlogi (JDialog) |
| `eventregistrationsystem.model` | `Lietotajs`, `Administrators`, `Dalibnieks`, `Pasakums`, `Registracija` |
| `eventregistrationsystem.dao` | `DatabaseUtil`, `LietotajuDAO`, `PasakumuDAO`, `RegistracijaDAO` (JDBC) |
| `eventregistrationsystem.util` | `PasswordUtil` (SHA-256), `Validacija` (ievaddatu pārbaude) |
| `eventregistrationsystem.sql` | `schema.sql`, `data.sql` |
