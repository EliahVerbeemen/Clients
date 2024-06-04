# V1

## Pre coaching

### Geschatte Progress (in procent): 75%

### Status

Stefaan heeft de security gedaan , Eliah de MVC.
Voor de andere delen is er heel goed samengewerkt.


### Stories



### Quality

_Acties (refactorings,...) die nog gepland staan om de kwaliteit van je project te verhogen (maak hiervoor issue(s) aan!): [issue nummer]: toelichting_
Issue 32: Als beheerder wil ik verkooprapporten genereren op datum, product of gebruiker.
Issue 26:  Bereik een coderegelsdekking van ten minste 60%.
Issue 27:  Bereid de codebase voor op toekomstige verbeteringen.
Issue 25: Implementeer integratietests voor API-endpoints en functionaliteit
Issue 23: Maak unit tests voor prijscalculaties.
Issue 24. Maak unit tests voor loyaliteitsniveaulogica.
Issue 14: Als gebruiker met grote bestellingen wil ik een batch bestand uploaden via een REST-endpoint.
Issue 15: Als gebruiker met grote bestellingen wil ik de toestand van een purchase order opvragen.

Issue 42:  Als bakker wil ik een productrecept wijzigen, inclusief het (enkel nog) verwijderen  van stappen.
Issue 50. Bereid de codebase voor op toekomstige verbeteringen.
Issue 30: 30. Implementeer ondersteuning voor verschillende magazijnsecties op basis van temperatuurzones en vervaldatumzones.
### Vragen

Wat wordt er verwacht voor issue 32: Als beheerder wil ik verkooprapporten genereren op datum, product of gebruiker.


## Post coaching

### Feedback

1)  Code moet ordelijker
    Logica moet uit de controllers
    Naming conventies volgen: geen underscores en camelcase
    Gebruik van data transfer objecten
    Geen nederlands en engels coombineren
2)  Batchen ==> Jackson bind voor xml
3)  @Transational boven de methods waar database connecie is.
4)  Seed the loyality classes
5)  Rabbit mq credentials injecteren
6)  Testing voor de client applicatie
7)  sl4f loging waar gepast
8)  Proberen om alle rabbitMQ communicatie via json te doen.




# V2

## Pre coaching

### Geschatte Progress (in procent): 99%

### Status

We denken alles goed te geimplementeerd te hebben.
Eliah heeft na V1 de (de)serialisatie van de communicatie op zich genomen.
Stefan heeft zich vooral bezig gehouden met het opruimen van de code en refactorings

### Stories


### Quality

Er kunnen nog extra testen en logging toegevoegd worden.

### Vragen


## Post coaching

### Feedback

_(in te vullen na gesprek)_