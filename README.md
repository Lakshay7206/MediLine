# MediLine – Online Hospital Appointment Booking App  

MediLine is a modern healthcare appointment management app built with **Kotlin + Jetpack Compose**, designed to simplify doctor bookings, manage hospital workflows, and provide role-based access for Users, Admins, and Super Admins.

---

## 🚀 Features

### 👤 User Features
- Browse departments and doctors  
- Book appointments with preferred time slots  
- Secure online payments using **Razorpay**  
- View booking history  
- Download appointment PDFs  
- Manage personal profile  

### 🛠 Admin Features
- Manage departments and doctor schedules  
- Track all appointments in real-time  
- Update booking statuses  
- Onboard new admins via email invites  

### 🔐 Super Admin Features
- Full system control  
- Manage Admin permissions  
- Access logs and administrative actions  

---

## 🧩 Tech Stack

### **Android**
- Kotlin  
- Jetpack Compose  
- MVVM + StateFlow + Coroutines  
- Material 3  
- Coil  

### **Cloud & Backend**
- Firebase Authentication  
- Firebase Firestore  
- Firebase Cloud Messaging  

### **Integrations**
- Razorpay Payment Gateway  
- PDF Generation (Android official APIs)  

---

## 🗂 Architecture Overview

The project follows **Clean MVVM Architecture**:

```UI (Jetpack Compose)
↓
ViewModel (StateFlow / Coroutines)
↓
Repository (Data orchestration)
↓
Remote (Firestore, Razorpay)
Local (Room / DataStore)
```

---

## 📥 Installation

### Requirements
- Android Studio Ladybug or newer  
- JDK 17  
- Firebase project setup  
- Razorpay keys  

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/mediline.git
2.Add google-services.json

3.Insert Razorpay keys in local.properties

4.Build and run the project
```
