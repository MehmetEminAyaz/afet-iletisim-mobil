# Post-Disaster Communication System

This project aims to enable reliable message delivery after a disaster, especially in areas without internet access. Mobile devices can create and send four types of emergency messages using Bluetooth and Wi-Fi. These messages are stored locally and forwarded to nearby devices when a connection is available. Messages spread from device to device. Once any device gains internet access, it uploads all the collected messages to a central server.

The web interface displays these messages on a map, where they can also be marked as "help delivered" when necessary.

---

## Emergency Message Types

- Search & Rescue  
- Medical Assistance  
- Supply Request  
- "I'm Alive" Notification  

---

## System Overview

The system consists of three separate components:

- **Mobile app**: Android application used to create, store, and forward help messages via Bluetooth/Wi-Fi.  
- **Backend**: API server that stores messages, handles filtering and statistics, and sends emails for “I’m Alive” notifications.  
- **Frontend**: Web interface to view messages on a map, check details, filter by type/status, and mark as fulfilled.  

Each component can run independently and lives in its own repository.

---

### Mobile App

- Written in Kotlin.  
- Uses Nearby Connections API to send messages directly to nearby devices.  
- Stores all messages locally using Room.  
- Sends all stored messages to the backend when internet is available.  

---

### Backend

- Developed with Spring Boot and PostgreSQL.  
- Offers RESTful APIs for all operations.  
- Handles authentication and role-based access with JWT.  
- Automatically sends emails for “I’m Alive” messages.  
- Performs geocoding to pin messages on the map.  

---

### Frontend

- Built with React and Material UI.  
- Uses Leaflet to show messages on a map.  

---

## Repositories

- **Mobile app**: [afet-mobile](https://github.com/MehmetEminAyaz/afet-iletisim-mobil)  
- **Backend API**: [afet-backend](https://github.com/MehmetEminAyaz/afet-iletisim-backend)  
- **Frontend web app**: [afet-frontend](https://github.com/MehmetEminAyaz/afet-iletisim-frontend)  

---

## Contributors

| Name              | GitHub Profile                                                    |
|-------------------|-------------------------------------------------------------------|
| Mehmet Emin AYAZ  | [@MehmetEminAyaz](https://github.com/MehmetEminAyaz)             |
| Emre REYHAN       | [@EmreReyhan](https://github.com/ereyhan60)                       |

---

## Contact

If you have questions, ideas, or would like to contribute, feel free to reach out:

**Mehmet Emin AYAZ** – [meminayaz35@gmail.com](mailto:meminayaz35@gmail.com)

