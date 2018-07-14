# ISS-Tracker

## Informazioni sulla [Stazione Spaziale Internazionale](https://it.wikipedia.org/wiki/Stazione_Spaziale_Internazionale)
1. Posizione e orbita attuale della ISS
2. Equipaggio a bordo della ISS
3. Prossimi passaggi su una città

I dati aggiornati della ISS sono acceduti tramite servizi REST da http://open-notify.org/Open-Notify-API/ e http://www.celestrak.com/NORAD/elements/stations.txt, le informazioni sulle coordinate delle città sono ottenute da https://developers.google.com/maps/documentation/geocoding/intro, mentre gli orari relativi al tramonto e all'alba in una certa città sono acquisite da https://sunrise-sunset.org/api

## Riferimenti
1. Per la rappresentazione dell'orbita https://github.com/rossengeorgiev/orbits-js
2. Per la rappresentazione della luce solare sulla mappa https://github.com/rossengeorgiev/nite-overlay
